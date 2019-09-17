package com.bzb.reactive.flux

import reactor.core.Exceptions
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

class FluxErrorHandlingTest extends Specification {

  def "onErrorResume subscribes to an fallback publisher (function returning a flux that emits one element)"() {
    given:
    def errorFlux = Flux.error(new RuntimeException())

    when:
    def result = errorFlux.onErrorResume({
      exception -> Flux.just(10)
    })

    then:
    StepVerifier.create(result).expectNext(10).verifyComplete()
  }

  def "map emits an error that contains a ReactiveException exception"() {
    given:
    def flux = Flux.just(1, 2, 3, 4)

    when:
    def result = flux.map({
      i ->
        try {
          if (i % 2 == 1) {
            throw new ReactiveException()
          }
          return i * 2
        } catch (ReactiveException e) {
          Exceptions.propagate(e)
        }
    })

    then:
    StepVerifier.create(result).expectError(ReactiveException.class)
  }

  private static class ReactiveException extends Exception {
  }
}
