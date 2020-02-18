package org.litesoft.persisted;

public abstract class AbstractPersistedObjectIdUuidImpl extends AbstractPersistedObjectIdImpl<String> implements IPersistedObjectIdUuid {
    @Override
    protected void generateIdStrategyForInsert() {
        populateIdForInsert();
    }

    abstract protected void populateIdForInsert();
}
