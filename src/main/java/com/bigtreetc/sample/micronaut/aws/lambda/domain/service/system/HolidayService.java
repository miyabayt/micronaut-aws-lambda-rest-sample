package com.bigtreetc.sample.micronaut.aws.lambda.domain.service.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.HolidaySpecifications.holidayDateBetween;
import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.HolidaySpecifications.holidayNameContains;
import static io.micronaut.data.repository.jpa.criteria.QuerySpecification.where;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Holiday;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.HolidayCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.HolidayRepository;
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

/** 祝日サービス */
@RequiredArgsConstructor
@Singleton
@Transactional(rollbackOn = Throwable.class)
public class HolidayService {

  @NonNull final HolidayRepository holidayRepository;

  /**
   * 祝日を検索します。
   *
   * @param holiday
   * @param pageable
   * @return
   */
  @ReadOnly
  public Mono<Page<Holiday>> findAll(HolidayCriteria holiday, Pageable pageable) {
    Objects.requireNonNull(holiday, "holiday must not be null");
    val holidayName = holiday.getHolidayName();
    val holidayDateFrom = holiday.getHolidayDateFrom();
    val holidayDateTo = holiday.getHolidayDateTo();
    return holidayRepository.findAll(
        where(holidayNameContains(holidayName))
            .and(holidayDateBetween(holidayDateFrom, holidayDateTo)),
        pageable);
  }

  /**
   * 祝日を取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Holiday> findOne(HolidayCriteria holiday) {
    Objects.requireNonNull(holiday, "holiday must not be null");
    val holidayName = holiday.getHolidayName();
    val holidayDateFrom = holiday.getHolidayDateFrom();
    val holidayDateTo = holiday.getHolidayDateTo();
    return holidayRepository.findOne(
        where(holidayNameContains(holidayName))
            .and(holidayDateBetween(holidayDateFrom, holidayDateTo)));
  }

  /**
   * 祝日を取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Holiday> findById(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return holidayRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new NoDataFoundException("id=" + id + " のデータが見つかりません。")));
  }

  /**
   * 祝日を登録します。
   *
   * @param holiday
   * @return
   */
  public Mono<Holiday> create(final Holiday holiday) {
    Objects.requireNonNull(holiday, "holiday must not be null");
    return holidayRepository.save(holiday);
  }

  /**
   * 祝日を登録します。
   *
   * @param holidays
   * @return
   */
  public Flux<Holiday> create(final List<Holiday> holidays) {
    Objects.requireNonNull(holidays, "holidays must not be null");
    return holidayRepository.saveAll(holidays);
  }

  /**
   * 祝日を更新します。
   *
   * @param holiday
   * @return
   */
  public Mono<Holiday> update(final Holiday holiday) {
    Objects.requireNonNull(holiday, "holiday must not be null");
    return holidayRepository.update(holiday);
  }

  /**
   * 祝日を更新します。
   *
   * @param holidays
   * @return
   */
  public Flux<Holiday> update(final List<Holiday> holidays) {
    Objects.requireNonNull(holidays, "holiday must not be null");
    return holidayRepository.saveAll(holidays);
  }

  /**
   * 祝日を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return holidayRepository.deleteById(id).then();
  }

  /**
   * 祝日を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final List<UUID> ids) {
    Objects.requireNonNull(ids, "id must not be null");
    return holidayRepository.deleteAllById(ids);
  }
}
