package com.bigtreetc.sample.micronaut.aws.lambda.controller.codes;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class SearchCodeRequest {

  private static final long serialVersionUID = -1L;

  @Nullable UUID id;

  @Nullable String categoryCode;

  @Nullable String codeName;

  @Nullable String codeValue;
}
