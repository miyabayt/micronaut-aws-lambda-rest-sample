package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Staff;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class StaffSpecifications {

  public static PredicateSpecification<Staff> firstNameContains(String firstName) {
    return isEmpty(firstName)
        ? null
        : (root, cb) -> cb.like(root.get("firstName"), "%" + firstName + "%");
  }

  public static PredicateSpecification<Staff> lastNameContains(String lastName) {
    return isEmpty(lastName)
        ? null
        : (root, cb) -> cb.like(root.get("lastName"), "%" + lastName + "%");
  }

  public static PredicateSpecification<Staff> emailContains(String email) {
    return isEmpty(email) ? null : (root, cb) -> cb.like(root.get("email"), "%" + email + "%");
  }
}
