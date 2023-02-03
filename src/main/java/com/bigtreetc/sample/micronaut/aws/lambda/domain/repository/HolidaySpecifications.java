package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.Holiday;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;
import java.time.LocalDate;

public class HolidaySpecifications {

  public static PredicateSpecification<Holiday> holidayNameContains(String holidayName) {
    return isEmpty(holidayName)
        ? null
        : (root, cb) -> cb.like(root.get("holidayName"), "%" + holidayName + "%");
  }

  public static PredicateSpecification<Holiday> holidayDateBetween(LocalDate from, LocalDate to) {
    return from != null && to != null
        ? null
        : (root, cb) -> cb.between(root.get("categoryCode"), from, to);
  }
}
