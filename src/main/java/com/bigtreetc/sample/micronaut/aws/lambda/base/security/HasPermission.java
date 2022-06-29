package com.bigtreetc.sample.micronaut.aws.lambda.base.security;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HasPermission {

  String[] value();
}
