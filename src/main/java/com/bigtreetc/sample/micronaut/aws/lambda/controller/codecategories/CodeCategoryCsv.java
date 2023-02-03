package com.bigtreetc.sample.micronaut.aws.lambda.controller.codecategories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.core.annotation.Introspected;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"コードID", "コード分類コード", "コード分類名"}) // CSVのヘッダ順
@Getter
@Setter
@Introspected
public class CodeCategoryCsv implements Serializable {

  private static final long serialVersionUID = -1L;

  @JsonProperty("コード分類ID")
  UUID id;

  @JsonProperty("コード分類コード")
  String categoryCode;

  @JsonProperty("コード分類名")
  String categoryName;
}
