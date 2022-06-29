package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.users;

import io.micronaut.core.annotation.Introspected;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Introspected
public class UserCriteria extends User {

  // 顧客シーケンスID（複数指定）
  List<String> ids;
}
