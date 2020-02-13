package org.litesoft.alleviative;

import java.util.function.Supplier;

public class LowerCasingStringSupplierProxy implements Supplier<String> {
  private final Supplier<String> mProxied;

  private LowerCasingStringSupplierProxy( Supplier<String> pToProxy ) {
    mProxied = pToProxy;
  }

  @Override
  public String get() {
    String zValue = mProxied.get();
    return (zValue == null) ? null : zValue.toLowerCase();
  }

  public static Supplier<String> of( Supplier<String> pToProxy ) {
    return (pToProxy == null) ? null : new LowerCasingStringSupplierProxy( pToProxy );
  }
}
