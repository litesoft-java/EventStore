package org.litesoft.persisted;

public abstract class NeutralPersistedObjectId32Impl extends AbstractPersistedObjectId32Impl implements IPersistedObjectId32 {
  private Integer id;

  private Integer version;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public Integer getVersion() {
    return version;
  }

  protected void populateAbstract( IPersistedObjectId32.AbstractBuilder them ) {
    id = them.getId();
    version = them.getVersion();
  }
}
