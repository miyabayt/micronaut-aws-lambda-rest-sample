package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.Staff;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.jpa.reactive.ReactorJpaSpecificationExecutor;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface StaffRepository
    extends ReactorPageableRepository<Staff, UUID>, ReactorJpaSpecificationExecutor<Staff> {

  Mono<Staff> findByEmail(String email);

  Mono<Void> deleteAllById(List<UUID> ids);
}
