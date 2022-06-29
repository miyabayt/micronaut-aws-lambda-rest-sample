package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.users;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("user_roles")
@Getter
@Setter
public class UserRole extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  // 担当者ロールID
  @Id @AutoPopulated UUID id;

  // ユーザID
  Long userId;

  // ロールコード
  String roleCode;

  // ロール名
  String roleName;

  // 権限コード
  String permissionCode;

  // 権限名
  String permissionName;
}
