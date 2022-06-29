package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.CodeCategory;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class CodeCategorySpecifications {

  public static PredicateSpecification<CodeCategory> categoryCodeEquals(String categoryCode) {
    return isEmpty(categoryCode)
        ? null
        : (root, cb) -> cb.equal(root.get("categoryCode"), categoryCode);
  }

  public static PredicateSpecification<CodeCategory> categoryNameContains(String categoryName) {
    return isEmpty(categoryName)
        ? null
        : (root, cb) -> cb.like(root.get("categoryName"), "%" + categoryName + "%");
  }
}
