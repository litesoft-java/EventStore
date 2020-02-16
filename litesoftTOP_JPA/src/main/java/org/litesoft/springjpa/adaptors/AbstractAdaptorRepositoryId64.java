package org.litesoft.springjpa.adaptors;

import org.litesoft.persisted.*;

@SuppressWarnings("unused")
public abstract class AbstractAdaptorRepositoryId64<T extends IPersistedObjectId64, B extends POBuilder<T>, CT extends T>
        extends AbstractAdaptorRepository<Long, T, B, CT> implements IPersistedObjectRepositoryId64<T> {
  private final SpringRepositoryId64<CT> mRepository;

  protected AbstractAdaptorRepositoryId64(Class<CT> pClassCT, POMetaData<T, B> pMetaData,
                                          SpringRepositoryId64<CT> pRepository, TransactionalProxy pTransactionalProxy,
                                          IPersistedObject... pAdditionalInstanceTypes ) {
    super( pClassCT, pMetaData, pRepository, pTransactionalProxy, pAdditionalInstanceTypes );
    mRepository = pRepository;
  }

  @Override
  public T findById( Long pId ) {
    return pId == null ? null : disconnect( mRepository.findById( pId ) );
  }

  @Override
  protected T save( CT pEntity ) {
    return disconnect( mRepository.save( pEntity ) );
  }
}
