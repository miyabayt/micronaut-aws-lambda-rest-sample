package com.bigtreetc.sample.micronaut.aws.lambda.controller.auth;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.StaffRepository;
import com.nimbusds.jwt.JWTParser;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.handlers.LoginHandler;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import io.micronaut.security.token.validator.RefreshTokenValidator;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import reactor.core.publisher.Mono;

@Tag(name = "認証")
@RequiredArgsConstructor
@Validated
@Controller("/auth")
@Slf4j
public class AuthController extends AbstractRestController {

  @NonNull final StaffRepository staffRepository;

  @NonNull final RefreshTokenPersistence refreshTokenPersistence;
  @NonNull final RefreshTokenValidator refreshTokenValidator;
  @NonNull final LoginHandler loginHandler;

  /**
   * アクセストークンを再発行します。
   *
   * @param request
   * @param refreshTokenRequest
   * @return
   */
  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post("/refresh")
  public Mono<MutableHttpResponse<?>> refreshToken(
      HttpRequest<?> request, @Body @Valid RefreshTokenRequest refreshTokenRequest) {
    val accessToken = refreshTokenRequest.getAccessToken();
    if (!isValidToken(accessToken)) {
      throw new OauthErrorResponseException(
          IssuingAnAccessTokenErrorCode.INVALID_GRANT, "Access token is invalid", null);
    }

    val refreshToken = refreshTokenRequest.getRefreshToken();
    val validRefreshToken = refreshTokenValidator.validate(refreshToken);
    if (validRefreshToken.isEmpty()) {
      throw new OauthErrorResponseException(
          IssuingAnAccessTokenErrorCode.INVALID_GRANT, "Refresh token is invalid", null);
    }
    return Mono.from(refreshTokenPersistence.getAuthentication(validRefreshToken.get()))
        .map(authentication -> loginHandler.loginRefresh(authentication, refreshToken, request));
  }

  /**
   * ログインユーザを取得します。
   *
   * @return
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get("/me")
  public Mono<ApiResponse> getMe(Authentication authentication) {
    val username = authentication.getName();
    return staffRepository
        .findById(UUID.fromString(username))
        .switchIfEmpty(Mono.error(new NoDataFoundException("データが見つかりません。")))
        .flatMap(this::toApiResponse);
  }

  private boolean isValidToken(String token) {
    try {
      JWTParser.parse(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
