package com.bigtreetc.sample.micronaut.aws.lambda.controller.mailtemplates;

import com.bigtreetc.sample.micronaut.aws.lambda.base.security.HasPermission;
import com.bigtreetc.sample.micronaut.aws.lambda.base.util.CsvUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.swagger.PageableAsQueryParam;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.MailTemplate;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.MailTemplateCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.MailTemplateService;
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

@Tag(name = "メールテンプレート")
@RequiredArgsConstructor
@Validated
@Controller("/system")
@Slf4j
public class MailTemplateController extends AbstractRestController {

  @NonNull final MailTemplateService mailTemplateService;

  /**
   * メールテンプレートを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "メールテンプレート登録", description = "メールテンプレートを登録します。")
  @HasPermission("mailTemplate:save")
  @Post("/mailTemplate")
  public Mono<ApiResponse> create(@Body @Valid MailTemplateRequest request) {
    val mailTemplate = new MailTemplate();
    mailTemplate.setCategoryCode(request.getCategoryCode());
    mailTemplate.setTemplateCode(request.getTemplateCode());
    mailTemplate.setSubject(request.getSubject());
    mailTemplate.setTemplateBody(request.getTemplateBody());
    return mailTemplateService.create(mailTemplate).flatMap(this::toApiResponse);
  }

  /**
   * メールテンプレートを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "メールテンプレート一括登録", description = "メールテンプレートを一括登録します。")
  @HasPermission("mailTemplate:save")
  @Post(value = "/mailTemplates")
  public Mono<ApiResponse> createAll(@Body @Valid Requests<MailTemplateRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val mailTemplate = new MailTemplate();
                  mailTemplate.setCategoryCode(request.getCategoryCode());
                  mailTemplate.setTemplateCode(request.getTemplateCode());
                  mailTemplate.setSubject(request.getSubject());
                  mailTemplate.setTemplateBody(request.getTemplateBody());
                  return mailTemplate;
                })
            .toList();
    return mailTemplateService.create(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * メールテンプレートを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "メールテンプレート検索", description = "メールテンプレートを検索します。")
  @HasPermission("mailTemplate:read")
  @Get("/mailTemplates")
  public Mono<ApiResponse> index(
      @RequestBean SearchMailTemplateRequest request, @RequestBody Pageable pageable) {
    val mailTemplate = new MailTemplateCriteria();
    mailTemplate.setCategoryCode(request.getCategoryCode());
    mailTemplate.setTemplateCode(request.getTemplateCode());
    mailTemplate.setSubject(request.getSubject());
    mailTemplate.setTemplateBody(request.getTemplateBody());
    return mailTemplateService.findAll(mailTemplate, pageable).flatMap(this::toApiResponse);
  }

  /**
   * メールテンプレートを取得します。
   *
   * @param mailTemplateId
   * @return
   */
  @Operation(summary = "メールテンプレート取得", description = "メールテンプレートを取得します。")
  @HasPermission("mailTemplate:read")
  @Get("/mailTemplate/{mailTemplateId}")
  public Mono<ApiResponse> show(@PathVariable UUID mailTemplateId) {
    return mailTemplateService.findById(mailTemplateId).flatMap(this::toApiResponse);
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param mailTemplateId
   * @param request
   * @return
   */
  @Operation(summary = "メールテンプレート更新", description = "メールテンプレートを更新します。")
  @HasPermission("mailTemplate:save")
  @Put("/mailTemplate/{mailTemplateId}")
  public Mono<ApiResponse> update(
      @PathVariable UUID mailTemplateId, @Body @Valid MailTemplateRequest request) {
    return mailTemplateService
        .findById(mailTemplateId)
        .map(
            mailTemplate -> {
              mailTemplate.setCategoryCode(request.getCategoryCode());
              mailTemplate.setTemplateCode(request.getTemplateCode());
              mailTemplate.setSubject(request.getSubject());
              mailTemplate.setTemplateBody(request.getTemplateBody());
              return mailTemplate;
            })
        .flatMap(mailTemplateService::update)
        .flatMap(this::toApiResponse);
  }

  /**
   * メールテンプレートを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "メールテンプレート一括更新", description = "メールテンプレートを一括更新します。")
  @HasPermission("mailTemplate:save")
  @Put(value = "/mailTemplates")
  public Mono<ApiResponse> update(@Body @Valid Requests<MailTemplateRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val mailTemplate = new MailTemplate();
                  mailTemplate.setCategoryCode(request.getCategoryCode());
                  mailTemplate.setTemplateCode(request.getTemplateCode());
                  mailTemplate.setSubject(request.getSubject());
                  mailTemplate.setTemplateBody(request.getTemplateBody());
                  return mailTemplate;
                })
            .toList();
    return mailTemplateService.update(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * メールテンプレートを削除します。
   *
   * @param mailTemplateId
   * @return
   */
  @Operation(summary = "メールテンプレート削除", description = "メールテンプレートを削除します。")
  @HasPermission("mailTemplate:save")
  @Delete("/mailTemplate/{mailTemplateId}")
  public Mono<ApiResponse> delete(@PathVariable UUID mailTemplateId) {
    return mailTemplateService
        .findById(mailTemplateId)
        .flatMap(
            mailTemplate -> mailTemplateService.delete(mailTemplateId).thenReturn(mailTemplate))
        .flatMap(this::toApiResponse);
  }

  /**
   * メールテンプレートを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "メールテンプレート一括削除", description = "メールテンプレートを一括削除します。")
  @HasPermission("mailTemplate:save")
  @Delete(value = "/mailTemplates")
  public Mono<ApiResponse> delete(@Body @Valid Requests<DeleteMailTemplateRequest> requests) {
    val ids = requests.stream().map(DeleteMailTemplateRequest::getId).toList();
    return mailTemplateService.delete(ids).thenReturn(createSimpleApiResponse());
  }

  /**
   * CSV出力
   *
   * @param filename
   * @return
   */
  @Operation(summary = "メールテンプレートCSV出力", description = "CSVファイルを出力します。")
  @HasPermission("mailTemplate:read")
  @Get(
      uri = "/mailTemplates/export/{filename:.+\\.csv}",
      produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> downloadCsv(@PathVariable String filename) {
    return mailTemplateService
        .findAll(new MailTemplateCriteria(), Pageable.unpaged())
        .map(
            pages -> {
              val csvList =
                  pages.getContent().stream()
                      .map(
                          mailTemplate -> {
                            val csv = new MailTemplateCsv();
                            csv.setId(mailTemplate.getId());
                            csv.setCategoryCode(mailTemplate.getCategoryCode());
                            csv.setTemplateCode(mailTemplate.getTemplateCode());
                            csv.setSubject(mailTemplate.getSubject());
                            csv.setTemplateBody(mailTemplate.getTemplateBody());
                            return csv;
                          })
                      .toList();
              val outputStream = CsvUtils.writeCsv(MailTemplateCsv.class, csvList);
              return new StreamedFile(
                  outputStream.toInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            })
        .map(streamedFile -> toHttpResponse(streamedFile, filename, true));
  }
}
