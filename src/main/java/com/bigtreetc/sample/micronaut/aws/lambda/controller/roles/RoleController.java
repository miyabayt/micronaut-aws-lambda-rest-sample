package com.bigtreetc.sample.micronaut.aws.lambda.controller.roles;

import com.bigtreetc.sample.micronaut.aws.lambda.base.security.HasPermission;
import com.bigtreetc.sample.micronaut.aws.lambda.base.util.CsvUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.swagger.PageableAsQueryParam;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.Role;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.RoleCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.RolePermission;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.PermissionService;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.RoleService;
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

@Tag(name = "ロール")
@RequiredArgsConstructor
@Validated
@Controller("/system")
@Slf4j
public class RoleController extends AbstractRestController {

  @NonNull final RoleService roleService;

  @NonNull final PermissionService permissionService;

  /**
   * ロールを登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "ロール登録", description = "ロールを登録します。")
  @HasPermission("role:save")
  @Post("/role")
  public Mono<ApiResponse> create(@Body @Valid RoleRequest request) {
    val role = new Role();
    role.setRoleCode(request.getRoleCode());
    role.setRoleName(request.getRoleName());
    request
        .getPermissions()
        .forEach(
            (permissionCode, isEnabled) -> {
              val rp = new RolePermission();
              rp.setRoleCode(request.getRoleCode());
              rp.setPermissionCode(permissionCode);
              rp.setIsEnabled(isEnabled);
              role.getRolePermissions().add(rp);
            });
    return roleService.create(role).flatMap(this::toApiResponse);
  }

  /**
   * ロールを一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ロール一括登録", description = "ロールを一括登録します。")
  @HasPermission("role:save")
  @Post(value = "/roles")
  public Mono<ApiResponse> createAll(@Body @Valid Requests<RoleRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val role = new Role();
                  role.setRoleCode(request.getRoleCode());
                  role.setRoleName(request.getRoleName());
                  request
                      .getPermissions()
                      .forEach(
                          (permissionCode, isEnabled) -> {
                            val rp = new RolePermission();
                            rp.setRoleCode(request.getRoleCode());
                            rp.setPermissionCode(permissionCode);
                            rp.setIsEnabled(isEnabled);
                            role.getRolePermissions().add(rp);
                          });
                  return role;
                })
            .toList();
    return roleService.create(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * ロールを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "ロール検索", description = "ロールを検索します。")
  @HasPermission("role:read")
  @Get("/roles")
  public Mono<ApiResponse> index(
      @RequestBean SearchRoleRequest request, @RequestBody Pageable pageable) {
    val role = new RoleCriteria();
    role.setRoleCode(request.getRoleCode());
    role.setRoleName(request.getRoleName());
    return roleService.findAll(role, pageable).flatMap(this::toApiResponse);
  }

  /**
   * ロールを取得します。
   *
   * @param roleId
   * @return
   */
  @Operation(summary = "ロール取得", description = "ロールを取得します。")
  @HasPermission("role:read")
  @Get("/role/{roleId}")
  public Mono<ApiResponse> show(@PathVariable UUID roleId) {
    return roleService.findById(roleId).flatMap(this::toApiResponse);
  }

  /**
   * ロールを更新します。
   *
   * @param roleId
   * @param request
   * @return
   */
  @Operation(summary = "ロール更新", description = "ロールを更新します。")
  @HasPermission("role:save")
  @Put("/role/{roleId}")
  public Mono<ApiResponse> update(@PathVariable UUID roleId, @Body @Valid RoleRequest request) {
    return roleService
        .findById(roleId)
        .map(
            role -> {
              role.setRoleCode(request.getRoleCode());
              role.setRoleName(request.getRoleName());
              role.getRolePermissions()
                  .forEach(
                      rp -> {
                        val permissionCode = rp.getPermissionCode();
                        val isEnabled = request.getPermissions().get(permissionCode);
                        rp.setIsEnabled(isEnabled);
                      });
              return role;
            })
        .flatMap(roleService::update)
        .flatMap(this::toApiResponse);
  }

  /**
   * ロールを一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ロール一括更新", description = "ロールを一括更新します。")
  @HasPermission("role:save")
  @Put(value = "/roles")
  public Mono<ApiResponse> update(@Body @Valid Requests<RoleRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val role = new Role();
                  role.setRoleCode(request.getRoleCode());
                  role.setRoleName(request.getRoleName());
                  role.getRolePermissions()
                      .forEach(
                          rp -> {
                            val permissionCode = rp.getPermissionCode();
                            val isEnabled = request.getPermissions().get(permissionCode);
                            rp.setIsEnabled(isEnabled);
                          });
                  return role;
                })
            .toList();
    return roleService.update(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * ロールを削除します。
   *
   * @param roleId
   * @return
   */
  @Operation(summary = "ロール削除", description = "ロールを削除します。")
  @HasPermission("role:save")
  @Delete("/role/{roleId}")
  public Mono<ApiResponse> delete(@PathVariable UUID roleId) {
    return roleService
        .findById(roleId)
        .flatMap(role -> roleService.delete(roleId).thenReturn(role))
        .flatMap(this::toApiResponse);
  }

  /**
   * ロールを一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "ロール一括削除", description = "ロールを一括削除します。")
  @HasPermission("role:save")
  @Delete(value = "/roles")
  public Mono<ApiResponse> delete(@Body @Valid Requests<DeleteRoleRequest> requests) {
    val ids = requests.stream().map(DeleteRoleRequest::getId).toList();
    return roleService.delete(ids).thenReturn(createSimpleApiResponse());
  }

  /**
   * CSV出力
   *
   * @param filename
   * @return
   */
  @Operation(summary = "ロールCSV出力", description = "CSVファイルを出力します。")
  @HasPermission("role:read")
  @Get(uri = "/roles/export/{filename:.+\\.csv}", produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> downloadCsv(@PathVariable String filename) {
    return roleService
        .findAll(new RoleCriteria(), Pageable.unpaged())
        .map(
            pages -> {
              val csvList =
                  pages.getContent().stream()
                      .map(
                          role -> {
                            val csv = new RoleCsv();
                            csv.setId(role.getId());
                            csv.setRoleCode(role.getRoleCode());
                            csv.setRoleName(role.getRoleName());
                            return csv;
                          })
                      .toList();
              val outputStream = CsvUtils.writeCsv(RoleCsv.class, csvList);
              return new StreamedFile(
                  outputStream.toInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            })
        .map(streamedFile -> toHttpResponse(streamedFile, filename, true));
  }
}
