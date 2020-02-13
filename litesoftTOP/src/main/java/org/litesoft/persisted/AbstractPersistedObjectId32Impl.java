package org.litesoft.persisted;

import java.util.Objects;

import org.litesoft.bean.DataObjectWithRequiredFields;

public abstract class AbstractPersistedObjectId32Impl extends DataObjectWithRequiredFields implements IPersistedObjectId32 {
  @Override
  public final int hashCode() {
    return Objects.hash( getId() );
  }

  @Override
  public final boolean equals( Object o ) {
    return (this == o) ||
           ((o instanceof IPersistedObjectId32) && equals( (IPersistedObjectId32)o )); // Left to Right
  }

  public final boolean equals( IPersistedObjectId32 them ) {
    return (this == them) ||
           ((them != null) && (this.getClass() == them.getClass()) // Left to Right
            && Objects.equals( this.getId(), them.getId() ));
  }
}
