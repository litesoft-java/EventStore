package org.litesoft.springjpa.persisted;

import org.litesoft.persisted.AbstractPersistedObjectId64Impl;
import org.litesoft.persisted.IPersistedObjectId64;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@SuppressWarnings("unused")
@MappedSuperclass
public abstract class PersistedObjectId64Impl extends AbstractPersistedObjectId64Impl implements IPersistedObjectId64 {

  @Id
  @GeneratedValue
  private Long id;

  @Version
  private Integer version;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public Integer getVersion() {
    return version;
  }

  @SuppressWarnings("rawtypes")
  protected void populateAbstract(AbstractBuilder them ) {
    id = them.getId();
    version = them.getVersion();
  }
}
