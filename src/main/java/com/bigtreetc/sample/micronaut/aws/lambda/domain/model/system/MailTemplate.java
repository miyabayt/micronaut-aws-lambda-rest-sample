package com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("mail_templates")
@Getter
@Setter
public class MailTemplate extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  @Id @AutoPopulated UUID id;

  // カテゴリコード
  String categoryCode;

  // メールテンプレートコード
  String templateCode;

  // メールタイトル
  String subject;

  // メール本文
  String templateBody;
}
