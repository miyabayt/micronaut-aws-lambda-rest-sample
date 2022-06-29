package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.uploadfiles;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.helper.FileHelper;
import com.bigtreetc.sample.micronaut.aws.lambda.base.security.HasPermission;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import io.micronaut.context.annotation.*;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.StreamingFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.file.Paths;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import reactor.core.publisher.Mono;

@Tag(name = "ファイルアップロード")
@RequiredArgsConstructor
@Validated
@Controller("/system")
@Slf4j
public class UploadFileController extends AbstractRestController {

  @Value("${application.fileUploadLocation}")
  String fileUploadLocation;

  @NonNull final FileHelper fileHelper;

  /**
   * ファイルの一覧を返します。
   *
   * @return
   */
  @Operation(summary = "ファイル一覧取得", description = "ファイルの一覧を取得します。")
  @HasPermission("uploadFile")
  @Get("/files")
  public Mono<ApiResponse> listFiles() {
    return Mono.fromCallable(
            () -> {
              // ファイル名のリストを作成する
              val location = Paths.get(fileUploadLocation);
              return fileHelper.listAllFiles(location).stream()
                  .map(path -> path.getFileName().toString())
                  .toList();
            })
        .flatMap(this::toApiResponse);
  }

  /**
   * ファイル内容をレスポンスします。
   *
   * @param filename
   * @return
   */
  @Operation(summary = "ファイル内容表示", description = "ファイルの内容を表示します。")
  @HasPermission("uploadFile")
  @Get(uri = "/file/{filename:.+}", produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> serveFile(@PathVariable String filename) {
    return Mono.fromCallable(() -> fileHelper.loadFile(Paths.get(fileUploadLocation), filename))
        .map(resource -> toHttpResponse(resource, filename));
  }

  /**
   * ファイルをダウンロードします。
   *
   * @param filename
   * @return
   */
  @Operation(summary = "ファイルダウンロード", description = "ファイルをダウンロードします。")
  @HasPermission("uploadFile")
  @Get(uri = "/file/download/{filename:.+}", produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> downloadFile(@PathVariable String filename) {
    return Mono.fromCallable(() -> fileHelper.loadFile(Paths.get(fileUploadLocation), filename))
        .map(streamedFile -> toHttpResponse(streamedFile, filename, true));
  }

  /**
   * ファイルをアップロードします。
   *
   * @param file
   * @return
   */
  @Operation(summary = "ファイルアップロード", description = "ファイルをアップロードします。")
  @HasPermission("uploadFile")
  @Post(uri = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA)
  public Mono<HttpResponse<ApiResponse>> uploadFile(StreamingFileUpload file) {
    val filename = file.getFilename();
    val resolvedPath = Paths.get(fileUploadLocation).resolve(filename);
    val outputFile = resolvedPath.toFile();
    return Mono.from(file.transferTo(outputFile))
        .thenReturn(HttpResponse.ok(createSimpleApiResponse()));
  }
}
