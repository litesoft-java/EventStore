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
               ((o instanceof AbstractPersistedObjectIdImpl) && equals( (AbstractPersistedObjectIdImpl<?>)o )); // Left to Right
    }

    public final boolean equals( AbstractPersistedObjectIdImpl<?> them ) {
        return (this == them) ||
               ((them != null) && (this.getClass() == them.getClass()) // Left to Right
                && Objects.equals( this.getId(), them.getId() )); // The use of Class forces the child objects to be the same type!
    }

    protected void generateIdStrategyForInsert() {
        throw new IllegalStateException( "No override of:  generateIdStrategyForInsert" );
    }

    @Override
    public void assertCanInsert() {
        if ( getId() != null ) {
            throw canNot( "insert", "with", "Id" );
        }
        if ( getVersion() != null ) {
            throw canNot( "insert", "with", "Version" );
        }
    }

    @Override
    public void assertCanUpdate() {
        assertPersisted( "update" );
    }

    @Override
    public void assertCanDelete() {
        assertPersisted( "delete" );
    }

    private void assertPersisted( String pAction ) {
        if ( getId() == null ) {
            throw canNot( pAction, "without", "Id" );
        }
        if ( getVersion() == null ) {
            throw canNot( pAction, "without", "Version" );
        }
    }

    private IllegalArgumentException canNot( String pAction, String pWithOrWithout, String pWhat ) {
        return new IllegalArgumentException( "Can NOT '" + pAction + "' a '" + getDisplayType().getSimpleName() +
                                             "' " + pWithOrWithout + " a " + pWhat + ": " + this );
    }
}
