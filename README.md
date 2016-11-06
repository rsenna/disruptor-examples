# LMAX Disruptor Examples

Simple Gradle project to show example usages of the Disruptor library.

There are two simple examples (which were not originally developed by me):

- ValueEvent
  The original fork from https://github.com/trevorbernard/disruptor-examples.
  A quite simple usage of LMAX Disruptor.
  Run with `./gradlew runValueEvent`.

- AccountStore
  Obtained from http://www.wjblackburn.me/resources/LMAX-disruptor-example.html.
  A somewhat more complex implementation, using event-sourcing, a simple key-value in-memory store and specialized classes.
  Run with `./gradlew runAccountStore`.

Main changes introduced by me (@rsenna):

* Use gradle 3.1.
* Upgrade Disruptor to the last available version (3.3.6).
* Use Lombok project.
* Many refactors in order to make the examples better designed/easier to understand (IMHO).
