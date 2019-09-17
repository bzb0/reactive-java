package com.bzb.reactive.flux

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

class FluxMergeTest extends Specification {

  def "mergeWith merges two fluxes into an interleaved merged sequence"() {
    given:
    def flux1 = Flux.just(1, 2, 3)
    def flux2 = Flux.just(4, 5, 6)

    when:
    def result = flux1.mergeWith(flux2)

    then:
    StepVerifier.create(result)
        .expectNext(1, 2, 3, 4, 5, 6)
        .verifyComplete()
  }

  def "concatWith concatenates the emitted elements from one flux with another"() {
    given:
    def flux1 = Flux.just(1, 2, 3)
    def flux2 = Flux.just(4, 5, 6)

    when:
    def result = flux1.concatWith(flux2)

    then:
    StepVerifier.create(result)
        .expectNext(1, 2, 3, 4, 5, 6)
        .verifyComplete()
  }
}
