package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Code;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class CodeSpecifications {

  public static PredicateSpecification<Code> categoryCodeEquals(String categoryCode) {
    return isEmpty(categoryCode)
        ? null
        : (root, cb) -> cb.equal(root.get("categoryCode"), categoryCode);
  }

  public static PredicateSpecification<Code> codeNameContains(String codeName) {
    return isEmpty(codeName)
        ? null
        : (root, cb) -> cb.like(root.get("codeName"), "%" + codeName + "%");
  }
}
