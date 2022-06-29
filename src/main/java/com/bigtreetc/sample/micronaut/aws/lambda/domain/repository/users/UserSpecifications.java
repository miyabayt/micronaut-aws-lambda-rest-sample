package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.users;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.users.User;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class UserSpecifications {

  public static PredicateSpecification<User> firstNameContains(String firstName) {
    return isEmpty(firstName)
        ? null
        : (root, cb) -> cb.like(root.get("firstName"), "%" + firstName + "%");
  }

  public static PredicateSpecification<User> lastNameContains(String lastName) {
    return isEmpty(lastName)
        ? null
        : (root, cb) -> cb.like(root.get("lastName"), "%" + lastName + "%");
  }

  public static PredicateSpecification<User> emailContains(String email) {
    return isEmpty(email) ? null : (root, cb) -> cb.like(root.get("email"), "%" + email + "%");
  }
}
