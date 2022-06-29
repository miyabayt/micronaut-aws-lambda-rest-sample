package com.bigtreetc.sample.micronaut.aws.lambda;

import com.bigtreetc.sample.micronaut.aws.lambda.base.util.FileUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.base.util.MessageUtils;
import io.micronaut.context.MessageSource;
import io.micronaut.context.annotation.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.nio.file.Paths;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Factory
@OpenAPIDefinition(info = @Info(title = "Micronaut AWS Lambda Rest Sample", version = "0.0.1"))
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
@SecurityRequirement(name = "bearerAuth")
public class AppConfig {

  @Value("${application.fileUploadLocation}")
  public String fileUploadLocation;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Inject
  public void initUtils(MessageSource messageSource) {
    MessageUtils.init(messageSource);
  }

  @PostConstruct
  public void init() {
    FileUtils.createDirectories(Paths.get(fileUploadLocation));
  }
}
