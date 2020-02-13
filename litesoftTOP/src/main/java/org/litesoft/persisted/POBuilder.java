package org.litesoft.persisted;

import java.util.Map;

public interface POBuilder<T extends IPersistedObject> {
  T build();

  T buildFrom( Map<String, ?> pFieldMap );
}
