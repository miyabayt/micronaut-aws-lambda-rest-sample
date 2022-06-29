package com.bigtreetc.sample.micronaut.aws.lambda.domain.service.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.base.util.ValidateUtils.isEquals;
import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.CodeSpecifications.categoryCodeEquals;
import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.CodeSpecifications.codeNameContains;
import static io.micronaut.data.repository.jpa.criteria.QuerySpecification.where;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Code;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.CodeCategory;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.CodeCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.CodeCategoryRepository;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.CodeRepository;
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
import reactor.util.function.Tuple2;

/** コードサービス */
@RequiredArgsConstructor
@Singleton
@Transactional(rollbackOn = Throwable.class)
public class CodeService {

  @NonNull final CodeRepository codeRepository;

  @NonNull final CodeCategoryRepository codeCategoryRepository;

  /**
   * コードを検索します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Page<Code>> findAll(CodeCriteria code, Pageable pageable) {
    Objects.requireNonNull(code, "code must not be null");
    val categoryCode = code.getCategoryCode();
    val codeName = code.getCodeName();
    return codeRepository
        .findAll(where(categoryCodeEquals(categoryCode)).and(codeNameContains(codeName)), pageable)
        .zipWith(codeCategoryRepository.findAll().collectList())
        .map(this::mergeCodesAndCodeCategories);
  }

  /**
   * コードを取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Code> findOne(CodeCriteria code) {
    Objects.requireNonNull(code, "code must not be null");
    val categoryCode = code.getCategoryCode();
    val codeName = code.getCodeName();
    return codeRepository
        .findOne(where(categoryCodeEquals(categoryCode)).and(codeNameContains(codeName)))
        .zipWith(codeCategoryRepository.findAll().collectList())
        .map(this::mergeCodeAndCodeCategories);
  }

  /**
   * コードを取得します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Code> findById(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return codeRepository
        .findById(id)
        .zipWith(codeCategoryRepository.findAll().collectList())
        .map(this::mergeCodeAndCodeCategories)
        .switchIfEmpty(Mono.error(new NoDataFoundException("id=" + id + " のデータが見つかりません。")));
  }

  /**
   * コードを登録します。
   *
   * @param code
   * @return
   */
  public Mono<Code> create(final Code code) {
    Objects.requireNonNull(code, "code must not be null");
    return codeRepository.save(code);
  }

  /**
   * コードを登録します。
   *
   * @param codes
   * @return
   */
  public Flux<Code> create(final List<Code> codes) {
    Objects.requireNonNull(codes, "codes must not be null");
    return codeRepository.saveAll(codes);
  }

  /**
   * コードを更新します。
   *
   * @param code
   * @return
   */
  public Mono<Code> update(final Code code) {
    Objects.requireNonNull(code, "code must not be null");
    return codeRepository.update(code);
  }

  /**
   * コードを更新します。
   *
   * @param codes
   * @return
   */
  public Flux<Code> update(final List<Code> codes) {
    Objects.requireNonNull(codes, "code must not be null");
    return codeRepository.updateAll(codes);
  }

  /**
   * コードを削除します。
   *
   * @return
   */
  public Mono<Void> delete(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return codeRepository.deleteById(id).then();
  }

  /**
   * コードを削除します。
   *
   * @return
   */
  public Mono<Void> delete(final List<UUID> ids) {
    Objects.requireNonNull(ids, "id must not be null");
    return codeRepository.deleteAllById(ids);
  }

  private Page<Code> mergeCodesAndCodeCategories(Tuple2<Page<Code>, List<CodeCategory>> tuple2) {
    val codes = tuple2.getT1();
    val codeCategories = tuple2.getT2();
    codes.forEach(
        c ->
            codeCategories.stream()
                .filter(cc -> isEquals(cc.getCategoryCode(), c.getCategoryCode()))
                .findFirst()
                .ifPresent(cc -> c.setCategoryName(cc.getCategoryName())));
    return codes;
  }

  private Code mergeCodeAndCodeCategories(Tuple2<Code, List<CodeCategory>> tuple2) {
    val code = tuple2.getT1();
    val codeCategories = tuple2.getT2();
    codeCategories.stream()
        .filter(cc -> isEquals(cc.getCategoryCode(), code.getCategoryCode()))
        .findFirst()
        .ifPresent(cc -> code.setCategoryName(cc.getCategoryName()));
    return code;
  }
}
