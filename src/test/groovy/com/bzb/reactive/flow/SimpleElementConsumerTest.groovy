package com.bzb.reactive.flow

import spock.lang.Specification

import java.util.concurrent.SubmissionPublisher
import java.util.stream.IntStream

class SimpleElementConsumerTest extends Specification {

  def "SimpleElementConsumer with capacity for 10 elements subscribes to an integer publisher and consumes all elements without error"() {
    given:
    def sut = new SimpleElementConsumer(10)
    def publisher = new SubmissionPublisher()
    publisher.subscribe(sut)

    when:
    IntStream.rangeClosed(1, 5).forEach({
      item -> publisher.submit(item)
    })
    publisher.close()

    then:
    sut.getReceivedElements() == [1, 2, 3, 4, 5]
    !sut.getEncounteredError().isPresent()
  }

  def "SimpleElementConsumer with capacity for 2 elements subscribes to a publisher with hundred elements and only consumes two elements"() {
    given:
    def sut = new SimpleElementConsumer(2)
    def publisher = new SubmissionPublisher()
    publisher.subscribe(sut)

    when:
    IntStream.rangeClosed(1, 100).forEach({
      item -> publisher.submit(item)
    })
    publisher.close()

    then:
    sut.getReceivedElements() == [1, 2]
    !sut.getEncounteredError().isPresent()
  }

  def "SimpleElementConsumer subscribes to a publisher consumes three elements and encounters an error"() {
    given:
    def sut = new SimpleElementConsumer(5)
    def publisher = new SubmissionPublisher()
    publisher.subscribe(sut)

    when:
    IntStream.rangeClosed(1, 3).forEach({
      item -> publisher.submit(item)
    })
    publisher.closeExceptionally(new RuntimeException())
    publisher.close()

    then:
    sut.getReceivedElements() == [1, 2, 3]
    sut.getEncounteredError()
    sut.getEncounteredError().get().class == RuntimeException.class
  }
}
