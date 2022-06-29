package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.RolePermission;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class RolePermissionSpecifications {

  public static PredicateSpecification<RolePermission> roleCodeEquals(String roleCode) {
    return isEmpty(roleCode) ? null : (root, cb) -> cb.equal(root.get("roleCode"), roleCode);
  }

  public static PredicateSpecification<RolePermission> permissionCodeEquals(String permissionCode) {
    return isEmpty(permissionCode)
        ? null
        : (root, cb) -> cb.equal(root.get("permissionCode"), permissionCode);
  }
}
