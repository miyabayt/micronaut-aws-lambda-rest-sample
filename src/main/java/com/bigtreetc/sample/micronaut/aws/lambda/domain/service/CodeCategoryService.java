package com.bigtreetc.sample.micronaut.aws.lambda.domain.service;

import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.CodeCategorySpecifications.categoryCodeEquals;
import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.CodeCategorySpecifications.categoryNameContains;
import static io.micronaut.data.repository.jpa.criteria.QuerySpecification.where;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.CodeCategory;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.CodeCategoryCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.CodeCategoryRepository;
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

/** コード分類サービス */
@RequiredArgsConstructor
@Singleton
@Transactional(rollbackOn = Throwable.class)
public class CodeCategoryService {

  @NonNull final CodeCategoryRepository codeCategoryRepository;

  /**
   * コード分類を検索します。
   *
   * @param codeCategory
   * @param pageable
   * @return
   */
  @ReadOnly
  public Mono<Page<CodeCategory>> findAll(
      final CodeCategoryCriteria codeCategory, final Pageable pageable) {
    Objects.requireNonNull(codeCategory, "codeCategory must not be null");
    val categoryCode = codeCategory.getCategoryCode();
    val categoryName = codeCategory.getCategoryName();
    return codeCategoryRepository.findAll(
        where(categoryCodeEquals(categoryCode)).and(categoryNameContains(categoryName)), pageable);
  }

  /**
   * コード分類を取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<CodeCategory> findOne(final CodeCategoryCriteria codeCategory) {
    Objects.requireNonNull(codeCategory, "criteria must not be null");
    val categoryCode = codeCategory.getCategoryCode();
    val categoryName = codeCategory.getCategoryName();
    return codeCategoryRepository
        .findOne(where(categoryCodeEquals(categoryCode)).and(categoryNameContains(categoryName)))
        .switchIfEmpty(Mono.error(new NoDataFoundException("データが見つかりません。")));
  }

  /**
   * コード分類を取得します。
   *
   * @param id
   * @return
   */
  @ReadOnly
  public Mono<CodeCategory> findById(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return codeCategoryRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new NoDataFoundException("id=" + id + " のデータが見つかりません。")));
  }

  /**
   * コード分類を登録します。
   *
   * @param codeCategory
   * @return
   */
  public Mono<CodeCategory> create(final CodeCategory codeCategory) {
    Objects.requireNonNull(codeCategory, "codeCategory must not be null");
    return codeCategoryRepository.save(codeCategory);
  }

  /**
   * コード分類を登録します。
   *
   * @param codeCategories
   * @return
   */
  public Flux<CodeCategory> create(final List<CodeCategory> codeCategories) {
    Objects.requireNonNull(codeCategories, "codeCategories must not be null");
    return codeCategoryRepository.saveAll(codeCategories);
  }

  /**
   * コード分類を更新します。
   *
   * @param codeCategory
   * @return
   */
  public Mono<CodeCategory> update(final CodeCategory codeCategory) {
    Objects.requireNonNull(codeCategory, "codeCategory must not be null");
    return codeCategoryRepository.update(codeCategory);
  }

  /**
   * コード分類を更新します。
   *
   * @param codeCategories
   * @return
   */
  public Flux<CodeCategory> update(final List<CodeCategory> codeCategories) {
    Objects.requireNonNull(codeCategories, "codeCategory must not be null");
    return codeCategoryRepository.updateAll(codeCategories);
  }

  /**
   * コード分類を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return codeCategoryRepository.deleteById(id).then();
  }

  /**
   * コード分類を削除します。
   *
   * @return
   */
  public Mono<Void> delete(final List<UUID> ids) {
    Objects.requireNonNull(ids, "id must not be null");
    return codeCategoryRepository.deleteAllById(ids);
  }
}
