package com.bigtreetc.sample.micronaut.aws.lambda.controller.mailtemplates;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Introspected
public class SearchMailTemplateRequest {

  private static final long serialVersionUID = -1L;

  @Nullable UUID id;

  // カテゴリコード
  @Nullable String categoryCode;

  // メールテンプレートコード
  @Nullable String templateCode;

  // メールタイトル
  @Nullable String subject;

  // メール本文
  @Nullable String templateBody;
}
