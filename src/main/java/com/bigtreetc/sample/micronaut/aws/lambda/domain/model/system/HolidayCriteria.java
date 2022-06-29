package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HolidayCriteria extends Holiday {

  // 祝日ID（複数指定）
  List<String> ids;

  // 日付From
  LocalDate holidayDateFrom;

  // 日付To
  LocalDate holidayDateTo;
}
