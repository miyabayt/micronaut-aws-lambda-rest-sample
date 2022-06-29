package com.bigtreetc.sample.micronaut.aws.lambda.domain.service.system;

import static com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.MailTemplateSpecifications.*;
import static io.micronaut.data.repository.jpa.criteria.QuerySpecification.where;

import com.bigtreetc.sample.micronaut.aws.lambda.base.exception.NoDataFoundException;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.MailTemplate;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.MailTemplateCriteria;
import com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system.MailTemplateRepository;
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

/** メールテンプレートサービス */
@RequiredArgsConstructor
@Singleton
@Transactional(rollbackOn = Throwable.class)
public class MailTemplateService {

  @NonNull final MailTemplateRepository mailTemplateRepository;

  /**
   * メールテンプレートを検索します。
   *
   * @return
   */
  @ReadOnly
  public Mono<Page<MailTemplate>> findAll(MailTemplateCriteria mailTemplate, Pageable pageable) {
    Objects.requireNonNull(mailTemplate, "mailTemplate must not be null");
    val categoryCode = mailTemplate.getCategoryCode();
    val templateCode = mailTemplate.getTemplateCode();
    val subject = mailTemplate.getSubject();
    return mailTemplateRepository.findAll(
        where(categoryCodeEquals(categoryCode))
            .and(templateCodeEquals(templateCode))
            .and(subjectContains(subject)),
        pageable);
  }

  /**
   * メールテンプレートを取得します。
   *
   * @param mailTemplate
   * @return
   */
  @ReadOnly
  public Mono<MailTemplate> findOne(MailTemplateCriteria mailTemplate) {
    Objects.requireNonNull(mailTemplate, "mailTemplate must not be null");
    val categoryCode = mailTemplate.getCategoryCode();
    val templateCode = mailTemplate.getTemplateCode();
    val subject = mailTemplate.getSubject();
    return mailTemplateRepository.findOne(
        where(categoryCodeEquals(categoryCode))
            .and(templateCodeEquals(templateCode))
            .and(subjectContains(subject)));
  }

  /**
   * メールテンプレートを取得します。
   *
   * @param id
   * @return
   */
  @ReadOnly
  public Mono<MailTemplate> findById(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return mailTemplateRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new NoDataFoundException("id=" + id + " のデータが見つかりません。")));
  }

  /**
   * メールテンプレートを登録します。
   *
   * @param mailTemplate
   * @return
   */
  public Mono<MailTemplate> create(final MailTemplate mailTemplate) {
    Objects.requireNonNull(mailTemplate, "mailTemplate must not be null");
    return mailTemplateRepository.save(mailTemplate);
  }

  /**
   * メールテンプレートを登録します。
   *
   * @param mailTemplates
   * @return
   */
  public Flux<MailTemplate> create(final List<MailTemplate> mailTemplates) {
    Objects.requireNonNull(mailTemplates, "mailTemplates must not be null");
    return mailTemplateRepository.saveAll(mailTemplates);
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param mailTemplate
   * @return
   */
  public Mono<MailTemplate> update(final MailTemplate mailTemplate) {
    Objects.requireNonNull(mailTemplate, "mailTemplate must not be null");
    return mailTemplateRepository.update(mailTemplate);
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param mailTemplates
   * @return
   */
  public Flux<MailTemplate> update(final List<MailTemplate> mailTemplates) {
    Objects.requireNonNull(mailTemplates, "mailTemplate must not be null");
    return mailTemplateRepository.updateAll(mailTemplates);
  }

  /**
   * メールテンプレートを削除します。
   *
   * @return
   */
  public Mono<Void> delete(final UUID id) {
    Objects.requireNonNull(id, "id must not be null");
    return mailTemplateRepository.deleteById(id).then();
  }

  /**
   * メールテンプレートを削除します。
   *
   * @return
   */
  public Mono<Void> delete(final List<UUID> ids) {
    Objects.requireNonNull(ids, "id must not be null");
    return mailTemplateRepository.deleteAllById(ids);
  }
}
