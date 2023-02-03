package com.bigtreetc.sample.micronaut.aws.lambda.controller.roles;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class SearchRoleRequest {

  private static final long serialVersionUID = -1L;

  @Nullable UUID id;

  // ロールコード
  @Nullable String roleCode;

  // ロール名
  @Nullable String roleName;
}
