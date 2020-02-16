package org.litesoft.springjpa.persisted;

import org.litesoft.persisted.AbstractPersistedObjectIdUuidImpl;
import org.litesoft.persisted.IPersistedObjectIdUuid;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@SuppressWarnings("unused")
@MappedSuperclass
public abstract class PersistedObjectIdUuidImpl extends AbstractPersistedObjectIdUuidImpl implements IPersistedObjectIdUuid {

  @Id
  @GeneratedValue(generator="UUID")
  private String id;

  @Version
  private Integer version;

  @Override
  public String getId() {
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
