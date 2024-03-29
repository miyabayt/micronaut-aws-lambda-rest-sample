package com.bigtreetc.sample.micronaut.aws.lambda.controller.codes;

import io.micronaut.core.annotation.Introspected;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class CodeRequest {

  private static final long serialVersionUID = -1L;

  UUID id;

  // 分類コード
  @NotEmpty String categoryCode;

  // コード名
  @NotEmpty String codeValue;

  // コード名
  @NotEmpty String codeName;

  // コードエイリアス
  String codeAlias;

  // 表示順
  @NotNull Integer displayOrder;

  // 無効フラグ
  Boolean isInvalid;

  // 改定番号
  Integer version;
}
