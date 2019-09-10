package com.bzb.reactive.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicInteger;

public final class SimpleElementConsumer<T> implements Flow.Subscriber<T> {

  private static final int SINGLE_ELEMENT = 1;

  private final AtomicInteger capacity;
  private final List<T> receivedElements = new ArrayList<>();
  private Throwable encounteredError;
  private Flow.Subscription subscription;

  public SimpleElementConsumer(int capacity) {
    this.capacity = new AtomicInteger(capacity);
  }

  @Override
  public void onSubscribe(Flow.Subscription subscription) {
    this.subscription = subscription;
    this.subscription.request(SINGLE_ELEMENT);
  }

  @Override
  public void onNext(T item) {
    receivedElements.add(item);
    if (capacity.decrementAndGet() > 0) {
      subscription.request(SINGLE_ELEMENT);
    }
  }

  @Override
  public void onComplete() {
    // nothing to do
  }

  @Override
  public void onError(final Throwable throwable) {
    encounteredError = throwable;
  }

  public List<T> getReceivedElements() {
    return receivedElements;
  }

  public Optional<Throwable> getEncounteredError() {
    return Optional.ofNullable(encounteredError);
  }
}
