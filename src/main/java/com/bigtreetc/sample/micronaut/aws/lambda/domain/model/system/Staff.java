package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("staffs")
@Getter
@Setter
public class Staff extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  @Id @AutoPopulated UUID id;

  @JsonIgnore String password;

  // 名前
  String firstName;

  // 苗字
  String lastName;

  // メールアドレス
  @Email String email;

  // 電話番号
  @Digits(fraction = 0, integer = 10)
  String tel;

  // パスワードリセットトークン
  @JsonIgnore String passwordResetToken;

  // トークン失効日
  @JsonIgnore LocalDateTime tokenExpiresAt;
}
