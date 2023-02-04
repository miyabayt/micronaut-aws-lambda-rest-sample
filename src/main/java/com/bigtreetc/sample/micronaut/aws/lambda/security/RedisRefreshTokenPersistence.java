package com.bigtreetc.sample.micronaut.aws.lambda.security;

import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT;

import com.bigtreetc.sample.micronaut.aws.lambda.base.util.JacksonUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.RolePermission;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.StaffRole;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.RolePermissionRepository;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.StaffRepository;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.StaffRoleRepository;
import io.lettuce.core.api.StatefulRedisConnection;
import io.micronaut.scheduling.annotation.Async;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
@Slf4j
public class RedisRefreshTokenPersistence implements RefreshTokenPersistence {

  @NonNull final StaffRepository staffRepository;

  @NonNull final StaffRoleRepository staffRoleRepository;

  @NonNull final RolePermissionRepository rolePermissionRepository;

  @NonNull final StatefulRedisConnection<String, String> connection;

  @Setter private int refreshTokenTimeoutHours = 2;

  @Async
  @Override
  public void persistToken(RefreshTokenGeneratedEvent event) {
    if (event != null
        && event.getRefreshToken() != null
        && event.getAuthentication() != null
        && event.getAuthentication().getName() != null) {
      val username = event.getAuthentication().getName();
      val refreshToken = event.getRefreshToken();

      val refreshTokenDto = new RefreshTokenDto();
      refreshTokenDto.setUsername(username);
      refreshTokenDto.setRefreshToken(refreshToken);

      storeRefreshToken(refreshToken, refreshTokenDto);
    }
  }

  @Override
  public Publisher<Authentication> getAuthentication(String refreshToken) {
    val refreshTokenOpt = getRefreshToken(refreshToken);
    if (refreshTokenOpt.isEmpty()) {
      return Mono.error(
          new OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null));
    }
    val dto = refreshTokenOpt.get();
    val username = dto.getUsername();
    return buildAuthentication(username);
  }

  protected Optional<RefreshTokenDto> getRefreshToken(String refreshToken) {
    val redisCommand = connection.sync();
    val payload = redisCommand.get(refreshToken);
    if (payload != null) {
      val refreshTokenDto = JacksonUtils.readValue(payload, RefreshTokenDto.class);
      return Optional.of(refreshTokenDto);
    }
    return Optional.empty();
  }

  protected void storeRefreshToken(String key, RefreshTokenDto dto) {
    try {
      val payload = JacksonUtils.writeValueAsString(dto);
      val redisCommand = connection.sync();
      redisCommand.multi();
      redisCommand.set(key, payload);
      redisCommand.expire(key, Duration.ofHours(refreshTokenTimeoutHours));
      redisCommand.exec();
      log.info("refresh token has stored. [key={}, payload={}]", key, payload);
    } catch (Throwable e) {
      log.warn("failed to store refresh token. [cause={}]", e.getMessage());
    }
  }

  protected Mono<Authentication> buildAuthentication(String username) {
    return staffRepository
        .findById(UUID.fromString(username))
        .flatMap(
            staff ->
                staffRoleRepository
                    .findByStaffId(staff.getId())
                    .map(StaffRole::getRoleCode)
                    .collectList()
                    .flatMap(
                        roleCodes ->
                            rolePermissionRepository
                                .findByRoleCodeIn(roleCodes)
                                .filter(RolePermission::getIsEnabled)
                                .map(RolePermission::getPermissionCode)
                                .collectList()
                                .zipWith(Mono.just(roleCodes)))
                    .map(
                        tuple2 ->
                            Authentication.build(
                                staff.getId().toString(),
                                tuple2.getT2(),
                                Map.of("permissions", tuple2.getT1()))));
  }
}
