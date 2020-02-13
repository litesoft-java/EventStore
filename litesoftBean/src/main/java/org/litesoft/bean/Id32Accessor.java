package org.litesoft.bean;

@SuppressWarnings("unused")
public interface Id32Accessor extends Accessor {
  Id32Accessor NOOP = deNull( null );

  Integer getId();

  static Id32Accessor deNull( Id32Accessor it ) {
    return (it != null) ? it : () -> null;
  }
}
