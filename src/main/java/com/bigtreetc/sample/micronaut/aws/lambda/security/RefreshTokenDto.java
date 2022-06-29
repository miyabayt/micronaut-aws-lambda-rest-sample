package com.bigtreetc.sample.micronaut.aws.lambda.security;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class RefreshTokenDto {

  private String username;

  private String refreshToken;
}
