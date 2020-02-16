package org.litesoft.persisted;

public interface IPersistedObjectId<ID> extends IPersistedObject {
    ID getId();
}
