package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Permission;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class PermissionSpecifications {

  public static PredicateSpecification<Permission> permissionCodeEquals(String permissionCode) {
    return isEmpty(permissionCode)
        ? null
        : (root, cb) -> cb.equal(root.get("permissionCode"), permissionCode);
  }

  public static PredicateSpecification<Permission> permissionNameContains(String permissionName) {
    return isEmpty(permissionName)
        ? null
        : (root, cb) -> cb.like(root.get("permissionName"), "%" + permissionName + "%");
  }
}
