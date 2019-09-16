package com.bzb.reactive.mono

import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.publisher.PublisherProbe
import spock.lang.Specification

import java.time.Duration

class MonoOperationsTest extends Specification {

  def "Mono.first returns the first Mono that will emit a signal (value, error or an empty completion)"() {
    given:
    def delayedMono = Mono.delay(Duration.ofMillis(50)).flatMap({
      t -> Mono.just(10)
    })
    def fastMono = Mono.just(20)

    when:
    def result = Mono.first(delayedMono, fastMono)

    then:
    StepVerifier.create(result).expectNext(20).verifyComplete()
  }

  def "Mono.then returns a Mono<Void> that replays the complete signal of the flux"() {
    given:
    def mono = Mono.just(1)
    def probe = PublisherProbe.of(mono)

    when:
    def result = probe.mono().then()

    then:
    StepVerifier.create(result)
        .verifyComplete()
    probe.assertWasRequested()
  }

  def "defaultIfEmpty provides a default value if a mono completes without emitting any data"() {
    given:
    def emptyMono = Mono.empty()

    when:
    def result = emptyMono.defaultIfEmpty(10)

    then:
    StepVerifier.create(result).expectNext(10).verifyComplete()
  }

  def "Mono.zip zips two Monos together into a Tuple2 elements"() {
    given:
    def mono1 = Mono.just(100)
    def mono2 = Mono.just(200)

    when:
    def result = Mono.zip(mono1, mono2).map({
      tuple -> return tuple.t1 * tuple.t2
    })

    then:
    StepVerifier.create(result).expectNext(20000).verifyComplete()
  }
}
