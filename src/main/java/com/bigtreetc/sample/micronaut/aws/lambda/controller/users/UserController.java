package com.bigtreetc.sample.micronaut.aws.lambda.controller.users;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isNotEmpty;

import com.bigtreetc.sample.micronaut.aws.lambda.base.security.HasPermission;
import com.bigtreetc.sample.micronaut.aws.lambda.base.util.CsvUtils;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.AbstractRestController;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.request.Requests;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response.ApiResponse;
import com.bigtreetc.sample.micronaut.aws.lambda.base.web.swagger.PageableAsQueryParam;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.User;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.UserCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.service.UserService;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Controller;
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

@Tag(name = "顧客")
@RequiredArgsConstructor
@Validated
@Controller("/user")
@Slf4j
public class UserController extends AbstractRestController {

  @NonNull final UserService userService;

  @NonNull final PasswordEncoder passwordEncoder;

  /**
   * 顧客を登録します。
   *
   * @param request
   * @return
   */
  @Operation(summary = "顧客登録", description = "顧客を登録します。")
  @HasPermission("user:save")
  @Post("/user")
  public Mono<ApiResponse> create(@Body @Valid UserRequest request) {
    val user = new User();
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setTel(request.getTel());
    return userService.create(user).flatMap(this::toApiResponse);
  }

  /**
   * 顧客を一括登録します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "顧客一括登録", description = "顧客を一括登録します。")
  @HasPermission("user:save")
  @Post(value = "/users")
  public Mono<ApiResponse> createAll(@Body @Valid Requests<UserRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val user = new User();
                  user.setPassword(passwordEncoder.encode(request.getPassword()));
                  user.setFirstName(request.getFirstName());
                  user.setLastName(request.getLastName());
                  user.setEmail(request.getEmail());
                  user.setTel(request.getTel());
                  return user;
                })
            .toList();
    return userService.create(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * 顧客を検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PageableAsQueryParam
  @Operation(summary = "顧客検索", description = "顧客を検索します。")
  @HasPermission("user:read")
  @Get("/users")
  public Mono<ApiResponse> index(
      @RequestBean SearchUserRequest request, @RequestBody Pageable pageable) {
    val user = new UserCriteria();
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setTel(request.getTel());
    return userService.findAll(user, pageable).flatMap(this::toApiResponse);
  }

  /**
   * 顧客を取得します。
   *
   * @param userId
   * @return
   */
  @Operation(summary = "顧客取得", description = "顧客を取得します。")
  @HasPermission("user:read")
  @Get("/user/{userId}")
  public Mono<ApiResponse> show(@PathVariable UUID userId) {
    return userService.findById(userId).flatMap(this::toApiResponse);
  }

  /**
   * 顧客を更新します。
   *
   * @param userId
   * @param request
   * @return
   */
  @Operation(summary = "顧客更新", description = "顧客を更新します。")
  @HasPermission("user:save")
  @Put("/user/{userId}")
  public Mono<ApiResponse> update(@PathVariable UUID userId, @Body @Valid UserRequest request) {
    return userService
        .findById(userId)
        .map(
            user -> {
              val password = request.getPassword();
              if (isNotEmpty(password)) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
              }
              user.setFirstName(request.getFirstName());
              user.setLastName(request.getLastName());
              user.setEmail(request.getEmail());
              user.setTel(request.getTel());
              return user;
            })
        .flatMap(userService::update)
        .flatMap(this::toApiResponse);
  }

  /**
   * 顧客を一括更新します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "顧客一括更新", description = "顧客を一括更新します。")
  @HasPermission("user:save")
  @Put(value = "/users")
  public Mono<ApiResponse> update(@Body @Valid Requests<UserRequest> requests) {
    val codeCategories =
        requests.stream()
            .map(
                request -> {
                  val user = new User();
                  user.setPassword(passwordEncoder.encode(request.getPassword()));
                  user.setFirstName(request.getFirstName());
                  user.setLastName(request.getLastName());
                  user.setEmail(request.getEmail());
                  user.setTel(request.getTel());
                  return user;
                })
            .toList();
    return userService.update(codeCategories).collectList().flatMap(this::toApiResponse);
  }

  /**
   * 顧客を削除します。
   *
   * @param userId
   * @return
   */
  @Operation(summary = "顧客削除", description = "顧客を削除します。")
  @HasPermission("user:save")
  @Delete("/user/{userId}")
  public Mono<ApiResponse> delete(@PathVariable UUID userId) {
    return userService
        .findById(userId)
        .flatMap(user -> userService.delete(userId).thenReturn(user))
        .flatMap(this::toApiResponse);
  }

  /**
   * 顧客を一括削除します。
   *
   * @param requests
   * @return
   */
  @Operation(summary = "顧客一括削除", description = "顧客を一括削除します。")
  @HasPermission("user:save")
  @Delete(value = "/users")
  public Mono<ApiResponse> delete(@Body @Valid Requests<DeleteUserRequest> requests) {
    val ids = requests.stream().map(DeleteUserRequest::getId).toList();
    return userService.delete(ids).thenReturn(createSimpleApiResponse());
  }

  /**
   * CSV出力
   *
   * @param filename
   * @return
   */
  @Operation(summary = "顧客CSV出力", description = "CSVファイルを出力します。")
  @HasPermission("user:read")
  @Get(uri = "/users/export/{filename:.+\\.csv}", produces = MediaType.APPLICATION_OCTET_STREAM)
  public Mono<HttpResponse<StreamedFile>> downloadCsv(@PathVariable String filename) {
    return userService
        .findAll(new UserCriteria(), Pageable.unpaged())
        .map(
            pages -> {
              val csvList =
                  pages.getContent().stream()
                      .map(
                          user -> {
                            val csv = new UserCsv();
                            csv.setId(user.getId());
                            csv.setFirstName(user.getFirstName());
                            csv.setLastName(user.getLastName());
                            csv.setEmail(user.getEmail());
                            csv.setTel(user.getTel());
                            return csv;
                          })
                      .toList();
              val outputStream = CsvUtils.writeCsv(UserCsv.class, csvList);
              return new StreamedFile(
                  outputStream.toInputStream(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            })
        .map(streamedFile -> toHttpResponse(streamedFile, filename, true));
  }
}
