package com.bzb.reactive.flux

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

import java.util.stream.Stream

class FluxCreationTest extends Specification {

  def "Flux.empty creates an empty Flux"() {
    when:
    def flux = Flux.empty()

    then:
    StepVerifier.create(flux).verifyComplete()
  }

  def "Flux.just creates a Flux from an array"() {
    when:
    def flux = Flux.just("Hello", "World", "Test")

    then:
    StepVerifier.create(flux).expectNext("Hello", "World", "Test").verifyComplete()
  }

  def "Flux.fromIterable creates a Flux from a list"() {
    given:
    def fluxElements = ["List", "test", "!"]

    when:
    def flux = Flux.fromIterable(fluxElements)

    then:
    StepVerifier.create(flux).expectNext(fluxElements[0], fluxElements[1], fluxElements[2]).verifyComplete()
  }

  def "Flux.fromStream creates a Flux from a stream with only the first five elements"() {
    when:
    def flux = Flux.fromStream(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).take(5)

    then:
    StepVerifier.create(flux).expectNext(1, 2, 3, 4, 5).verifyComplete()
  }

  def "Flux.error creates Flux from an exception"() {
    when:
    def flux = Flux.error(new RuntimeException())

    then:
    StepVerifier.create(flux).verifyError(RuntimeException.class)
  }

  def "Flux.from creates a Flux from a RxJava publisher"() {
    given:
    def flow = Flowable.just(1, 2, 3, 4)

    when:
    def result = Flux.from(flow)

    then:
    StepVerifier.create(result).expectNext(1, 2, 3, 4).verifyComplete()
  }

  def "Flux.from creates a Flux from a RxJava observable"() {
    given:
    def observable = Observable.just(1, 2, 3)

    when:
    def result = Flux.from(observable.toFlowable(BackpressureStrategy.DROP))

    then:
    StepVerifier.create(result).expectNext(1, 2, 3).verifyComplete()
  }

  def "Flux.generate creates a Flux via consumer callback"() {
    when:
    def result = Flux.generate({sink ->
      sink.next(1)
      sink.complete()
    })

    then:
    StepVerifier.create(result).expectNext(1).verifyComplete()
  }

  def "Flux.generate creates a Flux via consumer callback and some state"() {
    when:
    def result = Flux.generate({
      -> 1
    }, {state, sink ->
      sink.next(state + 1)
      if (state == 5) {
        sink.complete()
      }
      return state + 1
    })

    then:
    StepVerifier.create(result).expectNext(2, 3, 4, 5, 6).verifyComplete()
  }

  def "Flux.create creates a Flux with the FluxSink API in a synchronous manner"() {
    when:
    def result = Flux.create({sink ->
      sink.next(1)
      sink.next(2)
      sink.complete()
    })

    then:
    StepVerifier.create(result).expectNext(1, 2).verifyComplete()
  }
}
