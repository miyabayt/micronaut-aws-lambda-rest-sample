package com.bigtreetc.sample.micronaut.aws.lambda.domain.service;

import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.RoleSpecifications.roleCodeEquals;
import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.RoleSpecifications.roleNameContains;
import static io.micronaut.data.repository.jpa.criteria.QuerySpecification.where;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.Role;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.RoleCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.RolePermission;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.PermissionRepository;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.RolePermissionRepository;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.RoleRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** ロールサービス */
@RequiredArgsConstructor
@Singleton
@Transactional(rollbackOn = Throwable.class)
public class RoleService {

  @NonNull final RoleRepository roleRepository;

  @NonNull final RolePermissionRepository rolePermissionRepository;

  @NonNull final PermissionRepository permissionRepository;

  /**
   * ロールを検索します。
   *
   * @param role
   * @param pageable
   * @return
   */
  @ReadOnly
  public Mono<Page<Role>> findAll(RoleCriteria role, Pageable pageable) {
    Objects.requireNonNull(role, "role must not be null");
    val roleCode = role.getRoleCode();
    val roleName = role.getRoleName();
    return roleRepository.findAll(
        where(roleCodeEquals(roleCode)).and(roleNameContains(roleName)), pageable);
  }

  /**
   * ロールを取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Role> findOne(RoleCriteria role) {
    Objects.requireNonNull(role, "role must not be null");
    val roleCode = role.getRoleCode();
    val roleName = role.getRoleName();
    return roleRepository
        .findOne(where(roleCodeEquals(roleCode)).and(roleNameContains(roleName)))
        .flatMap(this::getRolePermissions)
        .flatMap(this::getPermissions);
  }

  /**
   * ロールを取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Role> findById(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return roleRepository
        .findById(id)
        .flatMap(this::getRolePermissions)
        .flatMap(this::getPermissions)
        .switchIfEmpty(Mono.error(new NoDataFoundException("id=" + id + " のデータが見つかりません。")));
  }

  /**
   * ロールを登録します。
   *
   * @param role
   * @return
   */
  public Mono<Role> create(final Role role) {
    Objects.requireNonNull(role, "role must not be null");
    return roleRepository
        .save(role)
        .flatMapMany(r -> Flux.fromIterable(r.getRolePermissions()))
        .collectList()
        .flatMapMany(rolePermissionRepository::saveAll)
        .collectList()
        .thenReturn(role);
  }

  /**
   * ロールを登録します。
   *
   * @param roles
   * @return
   */
  public Flux<Role> create(final List<Role> roles) {
    Objects.requireNonNull(roles, "roles must not be null");
    return roleRepository.saveAll(roles);
  }

  /**
   * ロールを更新します。
   *
   * @param role
   * @return
   */
  public Mono<Role> update(final Role role) {
    Objects.requireNonNull(role, "role must not be null");
    return roleRepository
        .update(role)
        .flatMapMany(r -> Flux.fromIterable(r.getRolePermissions()))
        .collectList()
        .flatMapMany(rolePermissionRepository::updateAll)
        .collectList()
        .thenReturn(role);
  }

  /**
   * ロールを更新します。
   *
   * @param roles
   * @return
   */
  public Flux<Role> update(final List<Role> roles) {
    Objects.requireNonNull(roles, "role must not be null");
    return roleRepository.updateAll(roles);
  }

  /**
   * ロールを削除します。
   *
   * @return
   */
  public Mono<Void> delete(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return roleRepository
        .findById(id)
        .flatMap(
            role -> {
              val roleCode = role.getRoleCode();
              return roleRepository
                  .deleteByRoleCode(roleCode)
                  .then(rolePermissionRepository.deleteByRoleCode(roleCode));
            });
  }

  /**
   * ロールを削除します。
   *
   * @return
   */
  public Mono<Void> delete(final List<UUID> ids) {
    Objects.requireNonNull(ids, "id must not be null");
    return roleRepository.deleteAllById(ids);
  }

  private Mono<Role> getRolePermissions(Role r) {
    return rolePermissionRepository
        .findByRoleCode(r.getRoleCode())
        .collectList()
        .map(
            rp -> {
              r.getRolePermissions().addAll(rp);
              return r;
            });
  }

  private Mono<Role> getPermissions(Role role) {
    val permissionCodes =
        role.getRolePermissions().stream()
            .filter(RolePermission::getIsEnabled)
            .map(RolePermission::getPermissionCode)
            .collect(Collectors.toList());
    return permissionRepository
        .findByPermissionCodeIn(permissionCodes)
        .collectList()
        .flatMap(
            p -> {
              role.getPermissions().addAll(p);
              return Mono.just(role);
            });
  }
}
