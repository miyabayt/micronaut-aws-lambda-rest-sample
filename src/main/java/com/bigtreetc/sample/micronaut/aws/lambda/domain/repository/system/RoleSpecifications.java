package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Role;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class RoleSpecifications {

  public static PredicateSpecification<Role> roleCodeEquals(String roleCode) {
    return isEmpty(roleCode) ? null : (root, cb) -> cb.equal(root.get("roleCode"), roleCode);
  }

  public static PredicateSpecification<Role> roleNameContains(String roleName) {
    return isEmpty(roleName)
        ? null
        : (root, cb) -> cb.like(root.get("roleName"), "%" + roleName + "%");
  }
}
