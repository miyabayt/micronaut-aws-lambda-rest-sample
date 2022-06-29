package com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
@Introspected
public abstract class BaseEntityImpl implements BaseEntity, Auditable, Serializable {

  // 作成者
  @JsonIgnore String createdBy;

  // 作成日時
  @DateCreated LocalDateTime createdAt;

  // 更新者
  @JsonIgnore String updatedBy;

  // 更新日時
  @DateUpdated LocalDateTime updatedAt;

  // 楽観的排他制御で使用する改定番号
  @Version Integer version;
}
