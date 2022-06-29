package com.bigtreetc.sample.micronaut.aws.lambda.controller.auth;

import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class RefreshTokenRequest {

  // アクセストークン
  @NotEmpty String accessToken;

  // リフレッシュトークン
  @NotEmpty String refreshToken;
}
