package com.bigtreetc.sample.micronaut.aws.lambda.controller.holidays;

import io.micronaut.core.annotation.Introspected;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class DeleteHolidayRequest {

  private static final long serialVersionUID = -1L;

  UUID id;
}
