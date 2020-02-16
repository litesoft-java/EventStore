package org.litesoft.persisted;

@SuppressWarnings("rawtypes")
public interface IPersistedObject {
  Class getDisplayType();

  void assertCanInsert();

  void assertCanUpdate();
}
