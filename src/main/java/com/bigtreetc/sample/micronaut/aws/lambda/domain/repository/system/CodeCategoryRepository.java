package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.CodeCategory;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.jpa.reactive.ReactorJpaSpecificationExecutor;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

/** コード分類リポジトリ */
@R2dbcRepository(dialect = Dialect.MYSQL)
public interface CodeCategoryRepository
    extends ReactorPageableRepository<CodeCategory, UUID>,
        ReactorJpaSpecificationExecutor<CodeCategory> {

  Mono<CodeCategory> findByCategoryCode(String categoryCode);

  Mono<Void> deleteAllById(List<UUID> ids);
}
