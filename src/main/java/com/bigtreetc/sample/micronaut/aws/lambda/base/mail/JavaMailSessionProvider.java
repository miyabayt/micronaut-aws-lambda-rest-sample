package com.bigtreetc.sample.micronaut.aws.lambda.base.mail;

import io.micronaut.email.javamail.sender.MailPropertiesProvider;
import io.micronaut.email.javamail.sender.SessionProvider;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class JavaMailSessionProvider implements SessionProvider {

  private final Properties properties;
  private final String username;
  private final String password;

  public JavaMailSessionProvider(
      MailPropertiesProvider properties, String username, String password) {
    this.properties = properties.mailProperties();
    this.username = username;
    this.password = password;
  }

  @Override
  public Session session() {
    return Session.getInstance(
        properties,
        new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });
  }
}
