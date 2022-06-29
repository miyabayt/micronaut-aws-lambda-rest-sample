package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("staff_roles")
@Getter
@Setter
public class StaffRole extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  // 担当者ロールID
  @Id @AutoPopulated UUID id;

  // 担当者ID
  UUID staffId;

  // ロールコード
  String roleCode;
}
