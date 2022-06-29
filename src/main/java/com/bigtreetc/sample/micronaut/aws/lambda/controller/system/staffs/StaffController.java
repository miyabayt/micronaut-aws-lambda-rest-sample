package com.bigtreetc.sample.micronaut.aws.lambda.controller.system.staffs;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isNotEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.base.security.HasPermission;
import com.bigtreetc.sample.micronaut.aws.lambda.base.util.CsvUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.swagger.PageableAsQueryParam;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Staff;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.StaffCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.system.StaffService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

@Tag(name = "担当者")
@RequiredArgsConstructor
@Validated
@Controller("/system")
@Slf4j
public class StaffController extends AbstractRestController {

  @NonNull final StaffService staffService;

  @NonNull final PasswordEncoder passwordEncoder;

  /**
   * 担当者を登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "担当者登録", description = "担当者を登録します。")
  @HasPermission("staff:save")
  @Post("/staff")
  public Mono<ApiResponse> create(@Body @Valid StaffRequest request) {
    val staff = new Staff();
    staff.setPassword(passwordEncoder.encode(request.getPassword()));
    staff.setFirstName(request.getFirstName());
    staff.setLastName(request.getLastName());
    staff.setEmail(request.getEmail());
    staff.setTel(request.getTel());
    return staffService.create(staff).flatMap(this::toApiResponse);
  }

  /**
   * 担当者を一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "担当者一括登録", description = "担当者を一括登録します。")
  @HasPermission("staff:save")
  @Post(value = "/staffs")
  public Mono<ApiResponse> createAll(@Body @Valid Requests<StaffRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val staff = new Staff();
                  staff.setPassword(passwordEncoder.encode(request.getPassword()));
                  staff.setFirstName(request.getFirstName());
                  staff.setLastName(request.getLastName());
                  staff.setEmail(request.getEmail());
                  staff.setTel(request.getTel());
                  return staff;
                })
            .toList();
    return staffService.create(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * 担当者を検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "担当者検索", description = "担当者を検索します。")
  @HasPermission("staff:read")
  @Get("/staffs")
  public Mono<ApiResponse> index(
      @RequestBean SearchStaffRequest request, @RequestBody Pageable pageable) {
    val staff = new StaffCriteria();
    staff.setFirstName(request.getFirstName());
    staff.setLastName(request.getLastName());
    staff.setEmail(request.getEmail());
    staff.setTel(request.getTel());
    return staffService.findAll(staff, pageable).flatMap(this::toApiResponse);
  }

  /**
   * 担当者を取得します。
   *
   * @param staffId
   * @return
   */
  @Operation(summary = "担当者取得", description = "担当者を取得します。")
  @HasPermission("staff:read")
  @Get("/staff/{staffId}")
  public Mono<ApiResponse> show(@PathVariable UUID staffId) {
    return staffService.findById(staffId).flatMap(this::toApiResponse);
  }

  /**
   * 担当者を更新します。
   *
   * @param staffId
   * @param request
   * @return
   */
  @Operation(summary = "担当者更新", description = "担当者を更新します。")
  @HasPermission("staff:save")
  @Put("/staff/{staffId}")
  public Mono<ApiResponse> update(@PathVariable UUID staffId, @Body @Valid StaffRequest request) {
    return staffService
        .findById(staffId)
        .map(
            staff -> {
              val password = request.getPassword();
              if (isNotEmpty(password)) {
                staff.setPassword(passwordEncoder.encode(request.getPassword()));
              }
              staff.setFirstName(request.getFirstName());
              staff.setLastName(request.getLastName());
              staff.setEmail(request.getEmail());
              staff.setTel(request.getTel());
              return staff;
            })
        .flatMap(staffService::update)
        .flatMap(this::toApiResponse);
  }

  /**
   * 担当者を一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "担当者一括更新", description = "担当者を一括更新します。")
  @HasPermission("staff:save")
  @Put(value = "/staffs")
  public Mono<ApiResponse> update(@Body @Valid Requests<StaffRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val staff = new Staff();
                  staff.setPassword(passwordEncoder.encode(request.getPassword()));
                  staff.setFirstName(request.getFirstName());
                  staff.setLastName(request.getLastName());
                  staff.setEmail(request.getEmail());
                  staff.setTel(request.getTel());
                  return staff;
                })
            .toList();
    return staffService.update(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * 担当者を削除します。
   *
   * @param staffId
   * @return
   */
  @Operation(summary = "担当者削除", description = "担当者を削除します。")
  @HasPermission("staff:save")
  @Delete("/staff/{staffId}")
  public Mono<ApiResponse> delete(@PathVariable UUID staffId) {
    return staffService
        .findById(staffId)
        .flatMap(staff -> staffService.delete(staffId).thenReturn(staff))
        .flatMap(this::toApiResponse);
  }

  /**
   * 担当者を一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "担当者一括削除", description = "担当者を一括削除します。")
  @HasPermission("staff:save")
  @Delete(value = "/staffs")
  public Mono<ApiResponse> delete(@Body @Valid Requests<DeleteStaffRequest> requests) {
    val ids = requests.stream().map(DeleteStaffRequest::getId).toList();
    return staffService.delete(ids).thenReturn(createSimpleApiResponse());
  }

  /**
   * CSV出力
   *
   * @param filename
   * @return
   */
  @Operation(summary = "担当者CSV出力", description = "CSVファイルを出力します。")
  @HasPermission("staff:read")
  @Get(uri = "/staffs/export/{filename:.+\\.csv}", produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> downloadCsv(@PathVariable String filename) {
    return staffService
        .findAll(new StaffCriteria(), Pageable.unpaged())
        .map(
            pages -> {
              val csvList =
                  pages.getContent().stream()
                      .map(
                          staff -> {
                            val csv = new StaffCsv();
                            csv.setId(staff.getId());
                            csv.setFirstName(staff.getFirstName());
                            csv.setLastName(staff.getLastName());
                            csv.setEmail(staff.getEmail());
                            csv.setTel(staff.getTel());
                            return csv;
                          })
                      .toList();
              val outputStream = CsvUtils.writeCsv(StaffCsv.class, csvList);
              return new StreamedFile(
                  outputStream.toInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            })
        .map(streamedFile -> toHttpResponse(streamedFile, filename, true));
  }
}
