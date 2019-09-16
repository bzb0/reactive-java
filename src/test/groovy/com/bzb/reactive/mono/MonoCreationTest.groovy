package com.bzb.reactive.mono

import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class MonoCreationTest extends Specification {

  def "empty Mono creation"() {
    when:
    def mono = Mono.empty()

    then:
    StepVerifier.create(mono).verifyComplete()
  }

  def "creating Mono from a value (Mono.just)"() {
    when:
    def mono = Mono.just("Test")

    then:
    StepVerifier.create(mono).expectNext("Test").verifyComplete()
  }

  def "creating Mono from a CompletableFuture (Mono.fromFuture)"() {
    when:
    def mono = Mono.fromFuture(CompletableFuture.completedFuture("Test"))

    then:
    StepVerifier.create(mono).expectNext("Test").verifyComplete()
  }

  def "creating Mono from an exception (Mono.error)"() {
    when:
    def mono = Mono.error(new RuntimeException())

    then:
    StepVerifier.create(mono).verifyError(RuntimeException.class)
  }

  def "Mono.justOrEmpty creates a Mono that only emits onComplete" () {
    when:
    def mono = Mono.justOrEmpty(null)

    then:
    StepVerifier.create(mono).verifyComplete()
  }
}
