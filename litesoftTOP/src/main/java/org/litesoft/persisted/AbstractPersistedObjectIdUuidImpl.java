package org.litesoft.persisted;

import org.litesoft.bean.DataObjectWithRequiredFields;

import java.util.Objects;

public abstract class AbstractPersistedObjectIdUuidImpl extends DataObjectWithRequiredFields implements IPersistedObjectIdUuid {
  @Override
  public final int hashCode() {
    return Objects.hash( getId() );
  }

  @Override
  public final boolean equals( Object o ) {
    return (this == o) ||
           ((o instanceof IPersistedObjectIdUuid) && equals( (IPersistedObjectIdUuid)o )); // Left to Right
  }

  public final boolean equals( IPersistedObjectIdUuid them ) {
    return (this == them) ||
           ((them != null) && (this.getClass() == them.getClass()) // Left to Right
            && Objects.equals( this.getId(), them.getId() ));
  }
}
