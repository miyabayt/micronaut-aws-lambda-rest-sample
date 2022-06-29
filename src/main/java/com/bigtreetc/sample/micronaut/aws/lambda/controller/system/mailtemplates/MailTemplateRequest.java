package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.mailtemplates;

import io.micronaut.core.annotation.Introspected;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class MailTemplateRequest {

  private static final long serialVersionUID = -1L;

  UUID id;

  // カテゴリコード
  @NotEmpty String categoryCode;

  // メールテンプレートコード
  @NotEmpty String templateCode;

  // メールタイトル
  @NotEmpty String subject;

  // メール本文
  @NotEmpty String templateBody;

  // 改定番号
  Integer version;
}
