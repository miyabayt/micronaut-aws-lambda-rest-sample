package com.bigtreetc.sample.micronaut.aws.lambda.base.domain.helper;

import io.micronaut.email.Body;
import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.javamail.sender.JavaxEmailSender;
import io.micronaut.email.template.TemplateBody;
import io.micronaut.views.ModelAndView;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/** メール送信ヘルパー */
@Singleton
@Slf4j
public class SendMailHelper {

  @Inject JavaxEmailSender javaMailSender;

  /**
   * メールを送信します。
   *
   * @param fromAddress
   * @param toAddress
   * @param subject
   * @param body
   */
  public void sendMail(String fromAddress, String[] toAddress, String subject, Body body) {
    val builder = new Email.Builder().from(fromAddress).subject(subject).body(body);
    if (toAddress != null) {
      for (val to : toAddress) {
        builder.to(to);
      }
    }

    try {
      val email = builder.build();
      javaMailSender.send(email);
    } catch (EmailException e) {
      log.error("failed to send mail.", e);
      throw e;
    }
  }

  /**
   * 指定したテンプレートのメール本文を返します。
   *
   * @param template
   * @param objects
   * @return
   */
  public Body getMailBody(String template, Map<String, Object> objects) {
    return new TemplateBody<>(BodyType.TEXT, new ModelAndView<>(template, objects));
  }
}
