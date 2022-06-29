package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.codes;

import com.bigtreetc.sample.micronaut.aws.lambda.base.security.HasPermission;
import com.bigtreetc.sample.micronaut.aws.lambda.base.util.CsvUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.swagger.PageableAsQueryParam;
import com.bigtreetc.sample.micronaut.aws.lambda.controller.system.codecategories.CodeCategoryCsv;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Code;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.CodeCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.system.CodeCategoryService;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.system.CodeService;
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

@Tag(name = "コード")
@RequiredArgsConstructor
@Validated
@Controller("/system")
@Slf4j
public class CodeController extends AbstractRestController {

  @NonNull final CodeCategoryService codeCategoryService;

  @NonNull final CodeService codeService;

  /**
   * コードを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "コード登録", description = "コードを登録します。")
  @HasPermission("code:save")
  @Post("/code")
  public Mono<ApiResponse> create(@Body @Valid CodeRequest request) {
    val code = new Code();
    code.setCategoryCode(request.getCategoryCode());
    code.setCodeName(request.getCodeName());
    code.setCodeValue(request.getCodeValue());
    code.setCodeAlias(request.getCodeAlias());
    code.setDisplayOrder(request.getDisplayOrder());
    code.setIsInvalid(request.getIsInvalid());
    return codeService.create(code).flatMap(this::toApiResponse);
  }

  /**
   * コードを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード一括登録", description = "コードを一括登録します。")
  @HasPermission("code:save")
  @Post(value = "/codes")
  public Mono<ApiResponse> createAll(@Body @Valid Requests<CodeRequest> requests) {
    val codes =
        requests.stream()
            .map(
                request -> {
                  val code = new Code();
                  code.setCategoryCode(request.getCategoryCode());
                  code.setCodeName(request.getCodeName());
                  code.setCodeValue(request.getCodeValue());
                  code.setCodeAlias(request.getCodeAlias());
                  code.setDisplayOrder(request.getDisplayOrder());
                  code.setIsInvalid(request.getIsInvalid());
                  return code;
                })
            .toList();
    return codeService.create(codes).collectList().flatMap(this::toApiResponse);
  }

  /**
   * コードを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "コード検索", description = "コードを検索します。")
  @HasPermission("code:read")
  @Get("/codes")
  public Mono<ApiResponse> index(
      @RequestBean SearchCodeRequest request, @RequestBody Pageable pageable) {
    val code = new CodeCriteria();
    code.setCategoryCode(request.getCategoryCode());
    code.setCodeName(request.getCodeName());
    code.setCodeValue(request.getCodeValue());
    return codeService.findAll(code, pageable).flatMap(this::toApiResponse);
  }

  /**
   * コードを取得します。
   *
   * @param codeId
   * @return
   */
  @Operation(summary = "コード取得", description = "コードを取得します。")
  @HasPermission("code:read")
  @Get("/code/{codeId}")
  public Mono<ApiResponse> show(@PathVariable UUID codeId) {
    return codeService.findById(codeId).flatMap(this::toApiResponse);
  }

  /**
   * コードを更新します。
   *
   * @param codeId
   * @param request
   * @return
   */
  @Operation(summary = "コード更新", description = "コードを更新します。")
  @HasPermission("code:save")
  @Put("/code/{codeId}")
  public Mono<ApiResponse> update(@PathVariable UUID codeId, @Body @Valid CodeRequest request) {
    return codeService
        .findById(codeId)
        .map(
            code -> {
              code.setCategoryCode(request.getCategoryCode());
              code.setCodeName(request.getCodeName());
              code.setCodeValue(request.getCodeValue());
              code.setCodeAlias(request.getCodeAlias());
              code.setDisplayOrder(request.getDisplayOrder());
              code.setIsInvalid(request.getIsInvalid());
              return code;
            })
        .flatMap(codeService::update)
        .flatMap(this::toApiResponse);
  }

  /**
   * コードを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード一括更新", description = "コードを一括更新します。")
  @HasPermission("code:save")
  @Put(value = "/codes")
  public Mono<ApiResponse> update(@Body @Valid Requests<CodeRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val code = new Code();
                  code.setCategoryCode(request.getCategoryCode());
                  code.setCodeName(request.getCodeName());
                  code.setCodeValue(request.getCodeValue());
                  code.setCodeAlias(request.getCodeAlias());
                  code.setDisplayOrder(request.getDisplayOrder());
                  code.setIsInvalid(request.getIsInvalid());
                  return code;
                })
            .toList();
    return codeService.update(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * コードを削除します。
   *
   * @param codeId
   * @return
   */
  @Operation(summary = "コード削除", description = "コードを削除します。")
  @HasPermission("code:save")
  @Delete("/code/{codeId}")
  public Mono<ApiResponse> delete(@PathVariable UUID codeId) {
    return codeService
        .findById(codeId)
        .flatMap(code -> codeService.delete(codeId).thenReturn(code))
        .flatMap(this::toApiResponse);
  }

  /**
   * コードを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "コード一括削除", description = "コードを一括削除します。")
  @HasPermission("code:save")
  @Delete(value = "/codes")
  public Mono<ApiResponse> delete(@Body @Valid Requests<DeleteCodeRequest> requests) {
    val ids = requests.stream().map(DeleteCodeRequest::getId).toList();
    return codeService.delete(ids).thenReturn(createSimpleApiResponse());
  }

  /**
   * CSV出力
   *
   * @param filename
   * @return
   */
  @Operation(summary = "コードCSV出力", description = "CSVファイルを出力します。")
  @HasPermission("code:read")
  @Get(uri = "/codes/export/{filename:.+\\.csv}", produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> downloadCsv(@PathVariable String filename) {
    return codeService
        .findAll(new CodeCriteria(), Pageable.unpaged())
        .map(
            pages -> {
              val csvList =
                  pages.getContent().stream()
                      .map(
                          code -> {
                            val csv = new CodeCsv();
                            csv.setCategoryCode(code.getCategoryCode());
                            csv.setCodeName(code.getCodeName());
                            csv.setCodeValue(code.getCodeValue());
                            csv.setCodeAlias(code.getCodeAlias());
                            csv.setDisplayOrder(code.getDisplayOrder());
                            csv.setIsInvalid(code.getIsInvalid());
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
