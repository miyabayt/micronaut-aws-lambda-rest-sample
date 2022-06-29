package com.bigtreetc.sample.micronaut.aws.lambda;

import io.micronaut.runtime.Micronaut;

public class Application {

  public static void main(String[] args) {
    Micronaut.build(args).mainClass(Application.class).defaultEnvironments("local").start();
  }
}
