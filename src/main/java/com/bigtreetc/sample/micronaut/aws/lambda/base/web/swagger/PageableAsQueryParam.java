package com.bigtreetc.sample.micronaut.aws.lambda.base.web.swagger;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
    in = ParameterIn.QUERY,
    name = "page",
    schema = @Schema(type = "integer", defaultValue = "0"))
@Parameter(
    in = ParameterIn.QUERY,
    name = "perpage",
    schema = @Schema(type = "integer", defaultValue = "20"))
@Parameter(
    in = ParameterIn.QUERY,
    name = "sort",
    array = @ArraySchema(schema = @Schema(type = "string")))
public @interface PageableAsQueryParam {}
