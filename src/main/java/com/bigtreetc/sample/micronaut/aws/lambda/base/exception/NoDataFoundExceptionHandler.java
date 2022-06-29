package com.bigtreetc.sample.micronaut.aws.lambda.base.exception;

import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ErrorApiResponseImpl;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import javax.inject.Singleton;
import lombok.val;

@Singleton
@Requires(classes = NoDataFoundException.class)
public class NoDataFoundExceptionHandler
    implements ExceptionHandler<NoDataFoundException, HttpResponse> {

  @Override
  public HttpResponse handle(HttpRequest request, NoDataFoundException exception) {
    val response = new ErrorApiResponseImpl();
    response.setMessage(exception.getMessage());
    return HttpResponse.notFound(response);
  }
}
