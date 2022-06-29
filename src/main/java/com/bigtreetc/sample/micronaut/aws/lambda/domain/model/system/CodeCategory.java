package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("code_categories")
@Getter
@Setter
public class CodeCategory extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  // コード分類ID
  @Id @AutoPopulated UUID id;

  // カテゴリコード
  String categoryCode;

  // カテゴリ名
  String categoryName;
}
