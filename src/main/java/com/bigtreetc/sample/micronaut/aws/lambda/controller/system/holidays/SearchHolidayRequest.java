package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.holidays;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class SearchHolidayRequest {

  private static final long serialVersionUID = -1L;

  @Nullable UUID id;

  // 祝日名
  @Nullable String holidayName;

  // 日付
  @Nullable LocalDate holidayDate;
}
