package org.litesoft.springjpa.persisted;

import java.util.UUID;

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

  protected void populateIdForInsert() {
    id = UUID.randomUUID().toString(); // Not Optimal, as real UUIDs rely on system/host data!
  }
}
