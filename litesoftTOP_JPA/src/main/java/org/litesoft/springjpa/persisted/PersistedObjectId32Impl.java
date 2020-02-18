package org.litesoft.springjpa.persisted;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.litesoft.persisted.AbstractPersistedObjectId32Impl;
import org.litesoft.persisted.IPersistedObjectId32;

@SuppressWarnings("unused")
@MappedSuperclass
public abstract class PersistedObjectId32Impl extends AbstractPersistedObjectId32Impl implements IPersistedObjectId32 {

    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @SuppressWarnings("rawtypes")
    protected void populateAbstract( IPersistedObjectId32.AbstractBuilder them ) {
        id = them.getId();
        version = them.getVersion();
    }
}
