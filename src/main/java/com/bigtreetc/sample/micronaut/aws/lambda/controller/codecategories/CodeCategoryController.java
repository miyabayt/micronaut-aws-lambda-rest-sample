package com.bigtreetc.sample.micronaut.aws.lambda.controller.codecategories;

import com.bigtreetc.sample.micronaut.aws.lambda.base.security.HasPermission;
import com.bigtreetc.sample.micronaut.aws.lambda.base.util.CsvUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.swagger.PageableAsQueryParam;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.CodeCategory;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.CodeCategoryCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.CodeCategoryService;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import reactor.core.publisher.Mono;

@Tag(name = "コード分類")
@RequiredArgsConstructor
@Validated
@Controller("/system")
@Slf4j
public class CodeCategoryController extends AbstractRestController {

  @NonNull final CodeCategoryService codeCategoryService;

  /**
   * コード分類を登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "コード分類登録", description = "コード分類を登録します。")
  @HasPermission("codeCategory:save")
  @Post("/codeCategory")
  public Mono<ApiResponse> create(@Body @Valid CodeCategoryRequest request) {
    val codeCategory = new CodeCategory();
    codeCategory.setCategoryCode(request.getCategoryCode());
    codeCategory.setCategoryName(request.getCategoryName());
    return codeCategoryService.create(codeCategory).flatMap(this::toApiResponse);
  }

  /**
   * コード分類を一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード分類一括登録", description = "コード分類を一括登録します。")
  @HasPermission("codeCategory:save")
  @Post(value = "/codeCategories")
  public Mono<ApiResponse> createAll(@Body @Valid Requests<CodeCategoryRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val codeCategory = new CodeCategory();
                  codeCategory.setCategoryCode(request.getCategoryCode());
                  codeCategory.setCategoryName(request.getCategoryName());
                  return codeCategory;
                })
            .toList();
    return codeCategoryService.create(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * コード分類を検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "コード分類検索", description = "コード分類を検索します。")
  @HasPermission("codeCategory:read")
  @Get("/codeCategories")
  public Mono<ApiResponse> index(
      @RequestBean SearchCodeCategoryRequest request, @RequestBody Pageable pageable) {
    val codeCategory = new CodeCategoryCriteria();
    codeCategory.setCategoryCode(request.getCategoryCode());
    codeCategory.setCategoryName(request.getCategoryName());
    return codeCategoryService.findAll(codeCategory, pageable).flatMap(this::toApiResponse);
  }

  /**
   * コード分類を取得します。
   *
   * @param codeCategoryId
   * @return
   */
  @Operation(summary = "コード分類取得", description = "コード分類を取得します。")
  @HasPermission("codeCategory:read")
  @Get("/codeCategory/{codeCategoryId}")
  public Mono<ApiResponse> show(@PathVariable UUID codeCategoryId) {
    return codeCategoryService.findById(codeCategoryId).flatMap(this::toApiResponse);
  }

  /**
   * コード分類を更新します。
   *
   * @param codeCategoryId
   * @param request
   * @return
   */
  @Operation(summary = "コード分類更新", description = "コード分類を更新します。")
  @HasPermission("codeCategory:save")
  @Put("/codeCategory/{codeCategoryId}")
  public Mono<ApiResponse> update(
      @PathVariable UUID codeCategoryId, @Body @Valid CodeCategoryRequest request) {
    return codeCategoryService
        .findById(codeCategoryId)
        .map(
            codeCategory -> {
              codeCategory.setCategoryCode(request.getCategoryCode());
              codeCategory.setCategoryName(request.getCategoryName());
              return codeCategory;
            })
        .flatMap(codeCategoryService::update)
        .flatMap(this::toApiResponse);
  }

  /**
   * コード分類を一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード分類一括更新", description = "コード分類を一括更新します。")
  @HasPermission("codeCategory:save")
  @Put(value = "/codeCategories")
  public Mono<ApiResponse> update(@Body @Valid Requests<CodeCategoryRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val codeCategory = new CodeCategory();
                  codeCategory.setCategoryCode(request.getCategoryCode());
                  codeCategory.setCategoryName(request.getCategoryName());
                  return codeCategory;
                })
            .toList();
    return codeCategoryService.update(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * コード分類を削除します。
   *
   * @param codeCategoryId
   * @return
   */
  @Operation(summary = "コード分類削除", description = "コード分類を削除します。")
  @HasPermission("codeCategory:save")
  @Delete("/codeCategory/{codeCategoryId}")
  public Mono<ApiResponse> delete(@PathVariable UUID codeCategoryId) {
    return codeCategoryService
        .findById(codeCategoryId)
        .flatMap(
            codeCategory -> codeCategoryService.delete(codeCategoryId).thenReturn(codeCategory))
        .flatMap(this::toApiResponse);
  }

  /**
   * コード分類を一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード分類一括削除", description = "コード分類を一括削除します。")
  @HasPermission("codeCategory:save")
  @Delete(value = "/codeCategories")
  public Mono<ApiResponse> delete(@Body @Valid Requests<DeleteCodeCategoryRequest> requests) {
    val ids = requests.stream().map(DeleteCodeCategoryRequest::getId).toList();
    return codeCategoryService.delete(ids).thenReturn(createSimpleApiResponse());
  }

  /**
   * CSV出力
   *
   * @param filename
   * @return
   */
  @Operation(summary = "コード分類CSV出力", description = "CSVファイルを出力します。")
  @HasPermission("codeCategory:read")
  @Get(
      uri = "/codeCategories/export/{filename:.+\\.csv}",
      produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> downloadCsv(@PathVariable String filename) {
    return codeCategoryService
        .findAll(new CodeCategoryCriteria(), Pageable.unpaged())
        .map(
            pages -> {
              val csvList =
                  pages.getContent().stream()
                      .map(
                          codeCategory -> {
                            val csv = new CodeCategoryCsv();
                            csv.setId(codeCategory.getId());
                            csv.setCategoryCode(codeCategory.getCategoryCode());
                            csv.setCategoryName(codeCategory.getCategoryName());
                            return csv;
                          })
                      .toList();
              val outputStream = CsvUtils.writeCsv(CodeCategoryCsv.class, csvList);
              return new StreamedFile(
                  outputStream.toInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            })
        .map(streamedFile -> toHttpResponse(streamedFile, filename, true));
  }
}
