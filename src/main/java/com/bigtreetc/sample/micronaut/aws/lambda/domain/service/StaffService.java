package com.bigtreetc.sample.micronaut.aws.lambda.domain.service;

import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.StaffSpecifications.*;
import static io.micronaut.data.repository.jpa.criteria.QuerySpecification.where;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.Staff;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.StaffCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.StaffRepository;
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
public class StaffService {

  @NonNull final StaffRepository staffRepository;

  /**
   * 担当者を検索します。
   *
   * @param staff
   * @param pageable
   * @return
   */
  @ReadOnly
  public Mono<Page<Staff>> findAll(final StaffCriteria staff, final Pageable pageable) {
    Objects.requireNonNull(staff, "staff must not be null");
    val firstName = staff.getFirstName();
    val lastName = staff.getLastName();
    val email = staff.getEmail();
    return staffRepository.findAll(
        where(firstNameContains(firstName))
            .and(lastNameContains(lastName))
            .and(emailContains(email)),
        pageable);
  }

  /**
   * 担当者を取得します。
   *
   * @param staff
   * @return
   */
  @ReadOnly
  public Mono<Staff> findOne(StaffCriteria staff) {
    Objects.requireNonNull(staff, "staff must not be null");
    val firstName = staff.getFirstName();
    val lastName = staff.getLastName();
    val email = staff.getEmail();
    return staffRepository.findOne(
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
  public Mono<Staff> findById(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return staffRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new NoDataFoundException("id=" + id + " のデータが見つかりません。")));
  }

  /**
   * 担当者を登録します。
   *
   * @param staff
   * @return
   */
  public Mono<Staff> create(final Staff staff) {
    Objects.requireNonNull(staff, "staff must not be null");
    return staffRepository.save(staff);
  }

  /**
   * 担当者を登録します。
   *
   * @param staffs
   * @return
   */
  public Flux<Staff> create(final List<Staff> staffs) {
    Objects.requireNonNull(staffs, "staffs must not be null");
    return staffRepository.saveAll(staffs);
  }

  /**
   * 担当者を更新します。
   *
   * @param staff
   * @return
   */
  public Mono<Staff> update(final Staff staff) {
    Objects.requireNonNull(staff, "staff must not be null");
    return staffRepository.update(staff);
  }

  /**
   * 担当者を更新します。
   *
   * @param staffs
   * @return
   */
  public Flux<Staff> update(final List<Staff> staffs) {
    Objects.requireNonNull(staffs, "staff must not be null");
    return staffRepository.updateAll(staffs);
  }

  /**
   * 担当者を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return staffRepository.deleteById(id).then();
  }

  /**
   * 担当者を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final List<UUID> ids) {
    Objects.requireNonNull(ids, "id must not be null");
    return staffRepository.deleteAllById(ids);
  }
}
