package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.holidays;

import com.bigtreetc.sample.micronaut.aws.lambda.base.security.HasPermission;
import com.bigtreetc.sample.micronaut.aws.lambda.base.util.CsvUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.swagger.PageableAsQueryParam;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Holiday;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.HolidayCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.system.HolidayService;
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

@Tag(name = "祝日")
@RequiredArgsConstructor
@Validated
@Controller("/system")
@Slf4j
public class HolidayController extends AbstractRestController {

  @NonNull final HolidayService holidayService;

  /**
   * 祝日を登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "祝日登録", description = "祝日を登録します。")
  @HasPermission("holiday:save")
  @Post("/holiday")
  public Mono<ApiResponse> create(@Body @Valid HolidayRequest request) {
    val holiday = new Holiday();
    holiday.setHolidayName(request.getHolidayName());
    holiday.setHolidayDate(request.getHolidayDate());
    return holidayService.create(holiday).flatMap(this::toApiResponse);
  }

  /**
   * 祝日を一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "祝日一括登録", description = "祝日を一括登録します。")
  @HasPermission("holiday:save")
  @Post(value = "/holidays")
  public Mono<ApiResponse> createAll(@Body @Valid Requests<HolidayRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val holiday = new Holiday();
                  holiday.setHolidayName(request.getHolidayName());
                  holiday.setHolidayDate(request.getHolidayDate());
                  return holiday;
                })
            .toList();
    return holidayService.create(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * 祝日を検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "祝日検索", description = "祝日を検索します。")
  @HasPermission("holiday:read")
  @Get("/holidays")
  public Mono<ApiResponse> index(
      @RequestBean SearchHolidayRequest request, @RequestBody Pageable pageable) {
    val holiday = new HolidayCriteria();
    holiday.setHolidayName(request.getHolidayName());
    holiday.setHolidayDate(request.getHolidayDate());
    return holidayService.findAll(holiday, pageable).flatMap(this::toApiResponse);
  }

  /**
   * 祝日を取得します。
   *
   * @param holidayId
   * @return
   */
  @Operation(summary = "祝日取得", description = "祝日を取得します。")
  @HasPermission("holiday:read")
  @Get("/holiday/{holidayId}")
  public Mono<ApiResponse> show(@PathVariable UUID holidayId) {
    return holidayService.findById(holidayId).flatMap(this::toApiResponse);
  }

  /**
   * 祝日を更新します。
   *
   * @param holidayId
   * @param request
   * @return
   */
  @Operation(summary = "祝日更新", description = "祝日を更新します。")
  @HasPermission("holiday:save")
  @Put("/holiday/{holidayId}")
  public Mono<ApiResponse> update(
      @PathVariable UUID holidayId, @Body @Valid HolidayRequest request) {
    return holidayService
        .findById(holidayId)
        .map(
            holiday -> {
              holiday.setHolidayName(request.getHolidayName());
              holiday.setHolidayDate(request.getHolidayDate());
              return holiday;
            })
        .flatMap(holidayService::update)
        .flatMap(this::toApiResponse);
  }

  /**
   * 祝日を一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "祝日一括更新", description = "祝日を一括更新します。")
  @HasPermission("holiday:save")
  @Put(value = "/holidays")
  public Mono<ApiResponse> update(@Body @Valid Requests<HolidayRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val holiday = new Holiday();
                  holiday.setHolidayName(request.getHolidayName());
                  holiday.setHolidayDate(request.getHolidayDate());
                  return holiday;
                })
            .toList();
    return holidayService.update(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * 祝日を削除します。
   *
   * @param holidayId
   * @return
   */
  @Operation(summary = "祝日削除", description = "祝日を削除します。")
  @HasPermission("holiday:save")
  @Delete("/holiday/{holidayId}")
  public Mono<ApiResponse> delete(@PathVariable UUID holidayId) {
    return holidayService
        .findById(holidayId)
        .flatMap(holiday -> holidayService.delete(holidayId).thenReturn(holiday))
        .flatMap(this::toApiResponse);
  }

  /**
   * 祝日を一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "祝日一括削除", description = "祝日を一括削除します。")
  @HasPermission("holiday:save")
  @Delete(value = "/holidays")
  public Mono<ApiResponse> delete(@Body @Valid Requests<DeleteHolidayRequest> requests) {
    val ids = requests.stream().map(DeleteHolidayRequest::getId).toList();
    return holidayService.delete(ids).thenReturn(createSimpleApiResponse());
  }

  /**
   * CSV出力
   *
   * @param filename
   * @return
   */
  @Operation(summary = "祝日CSV出力", description = "CSVファイルを出力します。")
  @HasPermission("holiday:read")
  @Get(uri = "/holidays/export/{filename:.+\\.csv}", produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> downloadCsv(@PathVariable String filename) {
    return holidayService
        .findAll(new HolidayCriteria(), Pageable.unpaged())
        .map(
            pages -> {
              val csvList =
                  pages.getContent().stream()
                      .map(
                          holiday -> {
                            val csv = new HolidayCsv();
                            csv.setId(holiday.getId());
                            csv.setHolidayName(holiday.getHolidayName());
                            csv.setHolidayDate(holiday.getHolidayDate());
                            return csv;
                          })
                      .toList();
              val outputStream = CsvUtils.writeCsv(HolidayCsv.class, csvList);
              return new StreamedFile(
                  outputStream.toInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            })
        .map(streamedFile -> toHttpResponse(streamedFile, filename, true));
  }
}
