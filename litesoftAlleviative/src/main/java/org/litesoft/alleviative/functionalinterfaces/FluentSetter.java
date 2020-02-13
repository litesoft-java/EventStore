package org.litesoft.alleviative.functionalinterfaces;

@SuppressWarnings("UnusedReturnValue")
@FunctionalInterface
public interface FluentSetter<T, U> {
  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param u the second input argument
   */
  T apply( T t, U u );
}
