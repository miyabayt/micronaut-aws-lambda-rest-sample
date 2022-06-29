package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.MailTemplate;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

public class MailTemplateSpecifications {

  public static PredicateSpecification<MailTemplate> categoryCodeEquals(String categoryCode) {
    return isEmpty(categoryCode)
        ? null
        : (root, cb) -> cb.equal(root.get("categoryCode"), categoryCode);
  }

  public static PredicateSpecification<MailTemplate> templateCodeEquals(String templateCode) {
    return isEmpty(templateCode)
        ? null
        : (root, cb) -> cb.equal(root.get("templateCode"), templateCode);
  }

  public static PredicateSpecification<MailTemplate> subjectContains(String subject) {
    return isEmpty(subject)
        ? null
        : (root, cb) -> cb.like(root.get("subject"), "%" + subject + "%");
  }
}
