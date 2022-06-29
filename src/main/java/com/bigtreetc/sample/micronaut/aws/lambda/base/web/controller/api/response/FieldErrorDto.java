package com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
public class FieldErrorDto {

  // 項目名
  String fieldName;

  // 入力値
  Object rejectedValue;

  // エラーメッセージ
  String errorMessage;
}
