package com.bigtreetc.sample.micronaut.aws.lambda.domain.service;

import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.PermissionSpecifications.permissionCodeEquals;
import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.PermissionSpecifications.permissionNameContains;
import static io.micronaut.data.repository.jpa.criteria.PredicateSpecification.where;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.Permission;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.PermissionCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.PermissionRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import java.util.*;
import javax.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** 権限サービス */
@RequiredArgsConstructor
@Singleton
@Transactional(rollbackOn = Throwable.class)
public class PermissionService {

  @NonNull final PermissionRepository permissionRepository;

  /**
   * 権限を検索します。
   *
   * @param permission
   * @param pageable
   * @return
   */
  @ReadOnly
  public Mono<Page<Permission>> findAll(PermissionCriteria permission, Pageable pageable) {
    Objects.requireNonNull(permission, "permission must not be null");
    val permissionCode = permission.getPermissionCode();
    val permissionName = permission.getPermissionName();
    return permissionRepository.findAll(
        where(permissionCodeEquals(permissionCode)).and(permissionNameContains(permissionName)),
        pageable);
  }

  /**
   * 権限を取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Permission> findOne(PermissionCriteria permission) {
    Objects.requireNonNull(permission, "permission must not be null");
    val permissionCode = permission.getPermissionCode();
    val permissionName = permission.getPermissionName();
    return permissionRepository.findOne(
        where(permissionCodeEquals(permissionCode)).and(permissionNameContains(permissionName)));
  }

  /**
   * 権限を取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Permission> findById(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return permissionRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new NoDataFoundException("id=" + id + " のデータが見つかりません。")));
  }

  /**
   * 権限を登録します。
   *
   * @param permission
   * @return
   */
  public Mono<Permission> create(final Permission permission) {
    Objects.requireNonNull(permission, "permission must not be null");
    return permissionRepository.save(permission);
  }

  /**
   * 権限を登録します。
   *
   * @param permissions
   * @return
   */
  public Flux<Permission> create(final List<Permission> permissions) {
    Objects.requireNonNull(permissions, "permissions must not be null");
    return permissionRepository.saveAll(permissions);
  }

  /**
   * 権限を更新します。
   *
   * @param permission
   * @return
   */
  public Mono<Permission> update(final Permission permission) {
    Objects.requireNonNull(permission, "permission must not be null");
    return permissionRepository.update(permission);
  }

  /**
   * 権限を更新します。
   *
   * @param permissions
   * @return
   */
  public Flux<Permission> update(final List<Permission> permissions) {
    Objects.requireNonNull(permissions, "permission must not be null");
    return permissionRepository.updateAll(permissions);
  }

  /**
   * 権限を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return permissionRepository.deleteById(id).then();
  }

  /**
   * 権限を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final List<UUID> ids) {
    Objects.requireNonNull(ids, "id must not be null");
    return permissionRepository.deleteAllById(ids);
  }
}
