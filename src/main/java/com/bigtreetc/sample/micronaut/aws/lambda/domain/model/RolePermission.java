package com.bigtreetc.sample.micronaut.aws.lambda.domain.model;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("role_permissions")
@Getter
@Setter
public class RolePermission extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  @Id @AutoPopulated UUID id;

  // ロールコード
  String roleCode;

  // 権限コード
  String permissionCode;

  // 有効
  Boolean isEnabled;
}
