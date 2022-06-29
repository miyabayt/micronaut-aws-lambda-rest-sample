package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("holidays")
@Getter
@Setter
public class Holiday extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  // 祝日ID
  @Id @AutoPopulated UUID id;

  // 祝日名
  String holidayName;

  // 日付
  LocalDate holidayDate;
}
