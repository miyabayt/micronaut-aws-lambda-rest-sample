package com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api;

import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.*;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.types.files.StreamedFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import reactor.core.publisher.Mono;

@Slf4j
public class AbstractRestController {

  protected Mono<ApiResponse> toApiResponse(int count) {
    return Mono.just(new CountApiResponseImpl().success(count));
  }

  protected Mono<ApiResponse> toApiResponse(List<?> data) {
    return Mono.just(new ListApiResponseImpl().success(data));
  }

  protected Mono<ApiResponse> toApiResponse(Page<?> data) {
    return Mono.just(new PageableApiResponseImpl().success(data));
  }

  protected Mono<ApiResponse> toApiResponse(Object data) {
    return Mono.just(new SimpleApiResponseImpl().success(data));
  }

  protected ApiResponse createSimpleApiResponse() {
    return new SimpleApiResponseImpl().success();
  }

  @SneakyThrows
  protected HttpResponse<StreamedFile> toHttpResponse(StreamedFile streamedFile, String filename) {
    return toHttpResponse(streamedFile, filename, false);
  }

  @SneakyThrows
  protected HttpResponse<StreamedFile> toHttpResponse(
      StreamedFile streamedFile, String filename, boolean isAttachment) {

    val httpResponse = HttpResponse.ok();

    if (isAttachment) {
      val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.name());
      val contentDisposition = String.format("attachment; filename*=UTF-8''%s", encodedFilename);
      httpResponse.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
    }

    return httpResponse.body(streamedFile);
  }
}
