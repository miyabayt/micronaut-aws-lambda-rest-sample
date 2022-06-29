package com.bigtreetc.sample.micronaut.aws.lambda;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.Auditable;
import io.micronaut.context.annotation.Factory;
import io.micronaut.data.event.listeners.PrePersistEventListener;
import io.micronaut.data.event.listeners.PreUpdateEventListener;
import io.micronaut.security.utils.SecurityService;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Factory
public class AuditableEntityListenerFactory {

  @NonNull final SecurityService securityService;

  @Singleton
  public PrePersistEventListener<Auditable> auditablePrePersist() {
    return (auditable) -> {
      auditable.setCreatedBy(securityService.username().orElse(null));
      return true;
    };
  }

  @Singleton
  public PreUpdateEventListener<Auditable> auditablePreUpdate() {
    return (auditable) -> {
      auditable.setUpdatedBy(securityService.username().orElse(null));
      return true;
    };
  }
}
