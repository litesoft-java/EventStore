package org.litesoft.springjpa.adaptors;

import org.litesoft.persisted.IPersistedObject;
import org.litesoft.persisted.IPersistedObjectId32;
import org.litesoft.persisted.IPersistedObjectRepositoryId32;
import org.litesoft.persisted.POBuilder;
import org.litesoft.persisted.POMetaData;

@SuppressWarnings("unused")
public abstract class AbstractAdaptorRepositoryId32<T extends IPersistedObjectId32, B extends POBuilder<T>, CT extends T>
        extends AbstractAdaptorRepository<Integer, T, B, CT> implements IPersistedObjectRepositoryId32<T> {
    private final SpringRepositoryId32<CT> mRepository;

    protected AbstractAdaptorRepositoryId32( Class<CT> pClassCT, POMetaData<T, B> pMetaData,
                                             SpringRepositoryId32<CT> pRepository, TransactionalProxy pTransactionalProxy,
                                             IPersistedObject... pAdditionalInstanceTypes ) {
        super( pClassCT, pMetaData, pRepository, pTransactionalProxy, pAdditionalInstanceTypes );
        mRepository = pRepository;
    }

    @Override
    public T findById( Integer pId ) {
        return (pId == null) ? null : disconnect( mRepository.findById( pId ) );
    }
}
