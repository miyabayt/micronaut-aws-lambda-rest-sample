package com.bigtreetc.sample.micronaut.aws.lambda.domain.service;

import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.UserSpecifications.*;
import static io.micronaut.data.repository.jpa.criteria.QuerySpecification.where;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.User;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.UserCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.UserRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** 担当者サービス */
@RequiredArgsConstructor
@Singleton
@Transactional(rollbackOn = Throwable.class)
public class UserService {

  @NonNull final UserRepository userRepository;

  /**
   * 担当者を検索します。
   *
   * @param user
   * @param pageable
   * @return
   */
  @ReadOnly
  public Mono<Page<User>> findAll(final UserCriteria user, final Pageable pageable) {
    Objects.requireNonNull(user, "user must not be null");
    val firstName = user.getFirstName();
    val lastName = user.getLastName();
    val email = user.getEmail();
    return userRepository.findAll(
        where(firstNameContains(firstName))
            .and(lastNameContains(lastName))
            .and(emailContains(email)),
        pageable);
  }

  /**
   * 担当者を取得します。
   *
   * @param user
   * @return
   */
  @ReadOnly
  public Mono<User> findOne(UserCriteria user) {
    Objects.requireNonNull(user, "user must not be null");
    val firstName = user.getFirstName();
    val lastName = user.getLastName();
    val email = user.getEmail();
    return userRepository.findOne(
        where(firstNameContains(firstName))
            .and(lastNameContains(lastName))
            .and(emailContains(email)));
  }

  /**
   * 担当者を取得します。
   *
   * @param id
   * @return
   */
  @ReadOnly
  public Mono<User> findById(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return userRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new NoDataFoundException("id=" + id + " のデータが見つかりません。")));
  }

  /**
   * 担当者を登録します。
   *
   * @param user
   * @return
   */
  public Mono<User> create(final User user) {
    Objects.requireNonNull(user, "user must not be null");
    return userRepository.save(user);
  }

  /**
   * 担当者を登録します。
   *
   * @param users
   * @return
   */
  public Flux<User> create(final List<User> users) {
    Objects.requireNonNull(users, "users must not be null");
    return userRepository.saveAll(users);
  }

  /**
   * 担当者を更新します。
   *
   * @param user
   * @return
   */
  public Mono<User> update(final User user) {
    Objects.requireNonNull(user, "user must not be null");
    return userRepository.update(user);
  }

  /**
   * 担当者を更新します。
   *
   * @param users
   * @return
   */
  public Flux<User> update(final List<User> users) {
    Objects.requireNonNull(users, "user must not be null");
    return userRepository.updateAll(users);
  }

  /**
   * 担当者を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return userRepository.deleteById(id).then();
  }

  /**
   * 担当者を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final List<UUID> ids) {
    Objects.requireNonNull(ids, "id must not be null");
    return userRepository.deleteAllById(ids);
  }
}
