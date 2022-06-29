package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Transient;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("codes")
@Getter
@Setter
public class Code extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  // コードID
  @Id @AutoPopulated UUID id;

  // 分類コード
  String categoryCode;

  // 分類名
  @Transient String categoryName;

  // コード値
  String codeValue;

  // コード名
  String codeName;

  // エイリアス
  String codeAlias;

  // 表示順
  Integer displayOrder;

  // 無効フラグ
  Boolean isInvalid;
}
