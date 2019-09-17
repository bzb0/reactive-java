package com.bzb.reactive.flux

import org.junit.Assert
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.Duration

class StepVerifierTest extends Specification {

  def "StepVerifier checks that the flux emits three elements and completes"() {
    when:
    def flux = Flux.just("Hello", "World", "Test")

    then:
    StepVerifier.create(flux).expectNext("Hello", "World", "Test").verifyComplete()
  }

  def "StepVerifier checks that the flux emits an even number followed by the number 5001 and completes"() {
    when:
    def flux = Flux.just(100, 5001)

    then:
    StepVerifier.create(flux)
        .expectNextMatches({
          i -> i % 2 == 0
        })
        .assertNext({
          i -> Assert.assertEquals(5001, i)
        })
        .verifyComplete()
  }

  def "StepVerifier checks that the flux emits five elements for five hundred milliseconds and then completes"() {
    when:
    def flux = Flux.interval(Duration.ofMillis(100L)).take(5)

    then:
    StepVerifier.create(flux)
        .expectNextCount(5)
        .verifyComplete()
        .compareTo(Duration.ofMillis(500)) > 0
  }

  def "StepVerifier with virtual time checks that the flux emits 24 elements every hour and completes" () {
    when:
    def fluxSupplier = {
      -> return Flux.interval(Duration.ofHours(1)).take(24)
    }

    then:
    StepVerifier.withVirtualTime(fluxSupplier).thenAwait(Duration.ofDays(1)).expectNextCount(24).verifyComplete()
  }
}
