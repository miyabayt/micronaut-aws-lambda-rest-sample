package com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model;

public interface Auditable {

  String getCreatedBy();

  void setCreatedBy(String createdBy);

  String getUpdatedBy();

  void setUpdatedBy(String updatedBy);
}
