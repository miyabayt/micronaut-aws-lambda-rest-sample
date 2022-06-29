package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.system;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.system.Code;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.jpa.reactive.ReactorJpaSpecificationExecutor;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

/** コードリポジトリ */
@R2dbcRepository(dialect = Dialect.MYSQL)
public interface CodeRepository
    extends ReactorPageableRepository<Code, UUID>, ReactorJpaSpecificationExecutor<Code> {

  Mono<Void> deleteAllById(List<UUID> ids);
}
