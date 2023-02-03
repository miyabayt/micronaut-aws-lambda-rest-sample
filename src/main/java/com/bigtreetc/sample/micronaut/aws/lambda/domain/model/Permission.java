package com.bigtreetc.sample.micronaut.aws.lambda.domain.model;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("permissions")
@Getter
@Setter
public class Permission extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  // 権限ID
  @Id @AutoPopulated UUID id;

  // 権限コード
  String permissionCode;

  // 権限名
  String permissionName;
}
