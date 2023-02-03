package com.bigtreetc.sample.micronaut.aws.lambda.controller.codecategories;

import io.micronaut.core.annotation.Introspected;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class CodeCategoryRequest {

  private static final long serialVersionUID = -1L;

  UUID id;

  // コード分類コード
  @NotEmpty String categoryCode;

  // コード分類名
  @NotEmpty String categoryName;

  // 改定番号
  Integer version;
}
