package com.bigtreetc.sample.micronaut.aws.lambda.domain.repository.users;

import com.bigtreetc.sample.micronaut.aws.lambda.domain.model.users.User;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.jpa.reactive.ReactorJpaSpecificationExecutor;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.MYSQL)
public interface UserRepository
    extends ReactorPageableRepository<User, UUID>, ReactorJpaSpecificationExecutor<User> {

  Mono<User> findByEmail(String email);

  Mono<Void> deleteAllById(List<UUID> ids);
}
