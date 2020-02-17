package org.litesoft.springjpa.adaptors;

import org.litesoft.persisted.IPersistedObject;
import org.litesoft.persisted.IPersistedObjectIdUuid;
import org.litesoft.persisted.IPersistedObjectRepositoryIdUuid;
import org.litesoft.persisted.POBuilder;
import org.litesoft.persisted.POMetaData;

@SuppressWarnings("unused")
public abstract class AbstractAdaptorRepositoryIdUuid<T extends IPersistedObjectIdUuid, B extends POBuilder<T>, CT extends T>
        extends AbstractAdaptorRepository<String, T, B, CT> implements IPersistedObjectRepositoryIdUuid<T> {
  private final SpringRepositoryIdUuid<CT> mRepository;

  protected AbstractAdaptorRepositoryIdUuid( Class<CT> pClassCT, POMetaData<T, B> pMetaData,
                                             SpringRepositoryIdUuid<CT> pRepository, TransactionalProxy pTransactionalProxy,
                                             IPersistedObject... pAdditionalInstanceTypes ) {
    super( pClassCT, pMetaData, pRepository, pTransactionalProxy, pAdditionalInstanceTypes );
    mRepository = pRepository;
  }

  @Override
  public T findById( String pId ) {
    return (pId == null) ? null : disconnect( mRepository.findById( pId ) );
  }
}
