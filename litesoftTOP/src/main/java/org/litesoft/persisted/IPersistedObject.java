package org.litesoft.persisted;

public interface IPersistedObject {
  Class getDisplayType();

  void assertCanInsert();

  void assertCanUpdate();
}
