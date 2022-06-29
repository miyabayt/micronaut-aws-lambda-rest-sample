package com.bigtreetc.sample.micronaut.aws.lambda.security;

import static io.micronaut.security.authentication.AuthenticationFailureReason.USER_NOT_FOUND;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.RolePermission;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.StaffRole;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.RolePermissionRepository;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.StaffRepository;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.StaffRoleRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.reactivestreams.Publisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class StaffRoleAuthenticationProvider implements AuthenticationProvider {

  @NonNull final StaffRepository staffRepository;

  @NonNull final StaffRoleRepository staffRoleRepository;

  @NonNull final RolePermissionRepository rolePermissionRepository;

  @NonNull final PasswordEncoder passwordEncoder;

  @Override
  public Publisher<AuthenticationResponse> authenticate(
      HttpRequest httpRequest, AuthenticationRequest authenticationRequest) {

    val username = authenticationRequest.getIdentity().toString();
    val password = authenticationRequest.getSecret().toString();

    return staffRepository
        .findByEmail(username)
        .filter(staff -> passwordEncoder.matches(password, staff.getPassword()))
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
                                .map(RolePermission::getPermissionCode)
                                .collectList()
                                .zipWith(Mono.just(roleCodes)))
                    .map(
                        tuple2 ->
                            AuthenticationResponse.success(
                                staff.getId().toString(),
                                tuple2.getT2(),
                                Map.of("permissions", tuple2.getT1()))))
        .switchIfEmpty(Mono.just(AuthenticationResponse.failure(USER_NOT_FOUND)));
  }
}
