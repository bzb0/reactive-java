package com.bzb.reactive.flux

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class FluxTransformationTest extends Specification {

  def "map transforms/maps flux elements (ever flux element is multiplied by hundred)"() {
    given:
    def flux = Flux.just(1, 2, 3, 4, 5)

    when:
    def result = flux.map({
      m -> m * 100
    })

    then:
    StepVerifier.create(result)
        .expectNext(100, 200, 300, 400, 500)
        .verifyComplete()
  }

  def "flatMap asynchronously transforms/maps flux elements into publishers (Mono)"() {
    given:
    def flux = Flux.just(1, 2, 3, 4, 5)

    when:
    def result = flux.flatMap({
      m -> Mono.just(m - 1)
    })

    then:
    StepVerifier.create(result)
        .expectNext(0, 1, 2, 3, 4)
        .verifyComplete()
  }

  def "transform an input Flux into a target Flux"() {
    given:
    def flux = Flux.just("red", "yellow", "green")

    when:
    def result = flux.transform({
      f ->
        f.filter({
          color -> (color != "red")
        }).map({
          color -> color.toUpperCase()
        })
    })

    then:
    StepVerifier.create(result).expectNext("YELLOW", "GREEN").verifyComplete()
  }
}
