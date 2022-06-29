package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.staffs;

import io.micronaut.core.annotation.Introspected;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class DeleteStaffRequest {

  private static final long serialVersionUID = -1L;

  UUID id;
}
