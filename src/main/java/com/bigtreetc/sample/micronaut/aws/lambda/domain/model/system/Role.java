package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEquals;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("roles")
@Getter
@Setter
public class Role extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  // ロールID
  @Id @AutoPopulated UUID id;

  // ロールコード
  String roleCode;

  // ロール名
  String roleName;

  final @Transient List<RolePermission> rolePermissions = new ArrayList<>();

  final @Transient List<Permission> permissions = new ArrayList<>();

  public boolean hasPermission(String permissionCode) {
    return rolePermissions.stream()
        .anyMatch(rp -> isEquals(rp.getPermissionCode(), permissionCode) && rp.getIsEnabled());
  }
}
