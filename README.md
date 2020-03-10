# Reactive Programming with Reactor and RxJava

### Description

This project contains various examples that demonstrate how to use reactive streams from [Reactor](https://projectreactor.io/)
and [RxJava](https://www.rxjava.com/). Most of the are based on Reactor reactive streams, but there is also an example of a RxJava `Subscriber`. The
examples show how to create `Flux` and `Mono` instances and how to work on (merge/transform/verify) on them. The project tech stack is:

```
  Java          11
  Maven         3.6.2
  Reactor       Californium-SR8
  RxJava        2.2.12
  Spock         1.3-groovy-2.5   
```

As it can be seen from the tech stack, the source code is implemented in Java 11 and the unit tests are written in Groovy (Spock framework).

### Building the project & running the examples

The project can be built by using the provided Maven wrapper with the following command:

```
./mvnw clean install
```

The generated classes can be found in the Maven ``target`` directory. With the next command all unit tests (examples) can be run:

```
./mvnw clean test
```
