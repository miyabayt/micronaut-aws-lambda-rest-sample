package com.bigtreetc.sample.micronaut.aws.lambda.domain.model;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.data.annotation.*;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.util.UUID;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("users")
@Getter
@Setter
public class User extends BaseEntityImpl {
  // ユーザID
  @Id @AutoPopulated UUID id;

  // ユーザ名（名）
  String firstName;

  // ユーザ名（姓）
  String lastName;

  // メールアドレス
  @NotNull String email;

  // 電話番号
  @Digits(fraction = 0, integer = 10)
  String tel;

  // パスワード
  @JsonIgnore String password;
}
