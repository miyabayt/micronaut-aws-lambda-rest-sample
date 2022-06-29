package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.roles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.micronaut.core.annotation.Introspected;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({"ロールID", "ロールコード", "ロール名"}) // CSVのヘッダ順
@Getter
@Setter
@Introspected
public class RoleCsv implements Serializable {

  private static final long serialVersionUID = -1L;

  @JsonProperty("ロールID")
  UUID id;

  @JsonProperty("ロールコード")
  String roleCode;

  @JsonProperty("ロール名")
  String roleName;
}
