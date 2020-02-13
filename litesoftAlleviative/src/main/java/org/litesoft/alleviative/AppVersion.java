package org.litesoft.alleviative;

import java.util.function.Supplier;

public class AppVersion implements Supplier<String> {
  private static AppVersion sInstance;

  private final String mVersion;

  private AppVersion( String pVersion ) {
    mVersion = pVersion;
  }

  public static AppVersion of( String pVersion ) {
    return sInstance = new AppVersion( pVersion );
  }

  @Override
  public String get() {
    return mVersion;
  }

  public static String value() {
    return (sInstance == null) ? null : sInstance.get();
  }
}
