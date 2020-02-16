package org.litesoft.bean;

@SuppressWarnings("unused")
public interface Id64Accessor extends IdAccessor<Long> {
  Id64Accessor NOOP = deNull( null );

  @Override
  Long getId();

  static Id64Accessor deNull(Id64Accessor it) {
    return (it != null) ? it : () -> null;
  }
}
