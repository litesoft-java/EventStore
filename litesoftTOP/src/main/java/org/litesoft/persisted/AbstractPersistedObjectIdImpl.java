package org.litesoft.persisted;

import java.util.Objects;

import org.litesoft.bean.DataObjectWithRequiredFields;

public abstract class AbstractPersistedObjectIdImpl<ID> extends DataObjectWithRequiredFields implements IPersistedObjectIdCommon<ID> {
  @Override
  public final int hashCode() {
    return Objects.hash( getId() );
  }

  @Override
  public final boolean equals( Object o ) {
    return (this == o) ||
           ((o instanceof AbstractPersistedObjectIdImpl) && equals( (AbstractPersistedObjectIdImpl)o )); // Left to Right
  }

  public final boolean equals( AbstractPersistedObjectIdImpl them ) {
    return (this == them) ||
           ((them != null) && (this.getClass() == them.getClass()) // Left to Right
            && Objects.equals( this.getId(), them.getId() ));
  }

  protected void generateIdStrategyForInsert() {
    throw new IllegalStateException( "No override of:  generateIdStrategyForInsert" );
  }
}
