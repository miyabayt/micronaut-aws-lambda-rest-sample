package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.StaffRole;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class StaffRoleSpecifications {

  public static PredicateSpecification<StaffRole> roleCodeEquals(String roleCode) {
    return isEmpty(roleCode) ? null : (root, cb) -> cb.equal(root.get("roleCode"), roleCode);
  }
}
