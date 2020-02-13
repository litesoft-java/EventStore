package org.litesoft.alleviative;

@SuppressWarnings("unused")
@FunctionalInterface
public interface MillisecTimeSource {
  MillisecTimeSource SYSTEM = System::currentTimeMillis;

  long now();

  /**
   * @param pTimeSource nullable
   *
   * @return !null
   */
  static MillisecTimeSource deNull( MillisecTimeSource pTimeSource ) {
    return (pTimeSource != null) ? pTimeSource : SYSTEM;
  }
}