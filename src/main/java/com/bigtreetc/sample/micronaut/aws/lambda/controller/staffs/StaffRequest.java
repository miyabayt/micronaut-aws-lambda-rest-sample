package com.bigtreetc.sample.micronaut.aws.lambda.controller.staffs;

import io.micronaut.core.annotation.Introspected;
import java.util.UUID;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class StaffRequest {

  private static final long serialVersionUID = -1L;

  UUID id;

  // 名前
  @NotEmpty String firstName;

  // 苗字
  @NotEmpty String lastName;

  @NotEmpty String password;

  @NotEmpty String passwordConfirm;

  // メールアドレス
  @NotEmpty @Email String email;

  // 電話番号
  @Digits(fraction = 0, integer = 10)
  String tel;

  // 改定番号
  Integer version;
}
