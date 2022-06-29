package com.bigtreetc.sample.micronaut.aws.lambda.base.web.validator.annotation;

import io.micronaut.http.multipart.StreamingFileUpload;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.val;

/** 入力チェック（コンテンツタイプ） */
public class ContentTypeValidator implements ConstraintValidator<ContentType, StreamingFileUpload> {

  private final List<String> allowed = new ArrayList<>();
  private final List<String> rejected = new ArrayList<>();

  @Override
  public void initialize(ContentType fileExtension) {
    allowed.addAll(Arrays.asList(fileExtension.allowed()));
    rejected.addAll(Arrays.asList(fileExtension.rejected()));
  }

  @Override
  public boolean isValid(StreamingFileUpload value, ConstraintValidatorContext context) {
    boolean isValid = false;

    try {
      if (value == null || value.getContentType().isEmpty()) {
        return true;
      }

      val contentType = value.getContentType().get().getType();

      if (allowed.contains(contentType)) {
        isValid = true;
      }
      if (rejected.contains(contentType)) {
        isValid = false;
      }
    } catch (final Exception ignore) {
      // ignore
    }

    return isValid;
  }
}
