package com.bzb.reactive.flux

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

class FluxDemandTest extends Specification {

  def "StepVerifier requests all elements and expects five values to be received"() {
    when:
    def flux = Flux.just(1, 2, 3, 4, 5)

    then:
    StepVerifier.create(flux)
        .expectNextCount(5)
        .expectComplete()
        .verify()
  }

  def "StepVerifier requests two elements and expects [1,2], then requests one element and expects 3, then requests one element and expects 4"() {
    when:
    def flux = Flux.just(1, 2, 3, 4)

    then:
    StepVerifier.create(flux, 2)
        .expectNext(1)
        .expectNext(2)
        .thenRequest(1)
        .expectNext(3)
        .thenRequest(1)
        .expectNext(4)
        .thenCancel()
  }

  def "doOnSubscribe, doOnNext, doOnComplete execute a consumer/runnable when a flux is subscribed, emits an item and complete successfully"() {
    given:
    def subscribeFlag = false
    def completeFlag = false
    def nextCounter = 0
    def flux = Flux.just(1, 2, 3, 4)

    when:
    def result = flux.doOnSubscribe({
      i -> subscribeFlag = true
    }).doOnNext({
      i -> nextCounter++
    }).doOnComplete({
      -> completeFlag = true
    })

    then:
    StepVerifier.create(result)
        .expectNextCount(4)
        .verifyComplete()
    subscribeFlag
    completeFlag
    nextCounter == 4
  }
}
