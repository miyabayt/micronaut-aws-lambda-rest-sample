package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.staffs;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class SearchStaffRequest implements Serializable {

  private static final long serialVersionUID = -1L;

  @Nullable UUID id;

  @Nullable String firstName;

  @Nullable String lastName;

  @Nullable String email;

  @Nullable String tel;
}
