package org.litesoft.alleviative;

@SuppressWarnings("unused")
@FunctionalInterface
public interface Disposable {
  Disposable NOOP = () -> { /* No Op */ };

  void dispose();

  /**
   * @param pDisposable nullable
   *
   * @return !null
   */
  static Disposable deNull( Disposable pDisposable ) {
    return (pDisposable != null) ? pDisposable : NOOP;
  }
}