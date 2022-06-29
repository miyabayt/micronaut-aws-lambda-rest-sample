package com.bigtreetc.sample.micronaut.aws.lambda.base.util;

import io.micronaut.context.MessageSource;
import io.micronaut.context.exceptions.NoSuchMessageException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class MessageUtils {

  private static MessageSource messageSource;

  public static void init(MessageSource messageSource) {
    MessageUtils.messageSource = messageSource;
  }

  /**
   * メッセージを取得します。
   *
   * @param code
   * @param args
   * @return
   */
  public static String getMessage(String code, Object... args) {
    val locale = Locale.getDefault();
    return MessageUtils.messageSource
        .getMessage(code, locale, args)
        .orElseThrow(() -> new NoSuchMessageException(code));
  }

  /**
   * ロケールを指定してメッセージを取得します。
   *
   * @param code
   * @param locale
   * @param args
   * @return
   */
  public static String getMessage(String code, Locale locale, Object... args) {
    return MessageUtils.messageSource
        .getMessage(code, locale, args)
        .orElseThrow(() -> new NoSuchMessageException(code));
  }
}
