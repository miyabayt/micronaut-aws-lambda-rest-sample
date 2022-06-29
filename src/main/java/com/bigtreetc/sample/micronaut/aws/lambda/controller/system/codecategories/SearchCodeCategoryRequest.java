package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.codecategories;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class SearchCodeCategoryRequest {

  private static final long serialVersionUID = -1L;

  @Nullable UUID id;

  @Nullable String categoryCode;

  @Nullable String categoryName;
}
