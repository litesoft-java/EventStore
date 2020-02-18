package org.litesoft.persisted;

import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings({"unused", "WeakerAccess", "SameParameterValue", "UnusedReturnValue"})
public class MutationalPersistedObjectUpdater<ID, PO extends IPersistedObjectId<ID>> {
    private final IPersistedObjectRepository<ID, PO> mRepository;

    protected MutationalPersistedObjectUpdater( IPersistedObjectRepository<ID, PO> pRepository ) {
        mRepository = pRepository;
    }

    public MutationalUpdateResult<PO> update( PO pPO, Function<PO, MutationalUpdateResult<PO>> pChanger ) {
        required( "Changer", pChanger );
        if ( pPO == null ) {
            return MutationalUpdateResult.ofCanNotUpdate( "No " + getPoSimpleName() + " provided", null );
        }
        int zTry = 0;
        while ( zTry++ < 5 ) {
            try {
                MutationalUpdateResult<PO> zResult = pChanger.apply( pPO );
                if ( MutationalUpdateResult.isNotSuccess( zResult ) ) {
                    return zResult;
                }
                PO zUpdatedPO = mRepository.update( zResult.getPO() );
                return MutationalUpdateResult.ofSuccess( zUpdatedPO );
            }
            catch ( PersistorConstraintViolationException e ) {
                PO zPO = mRepository.refresh( pPO );
                if ( zPO == null ) {
                    return MutationalUpdateResult.ofCanNotUpdate( "Refresh of " + getPoSimpleName() + " suggests record deleted", pPO );
                }
                pPO = zPO;
            }
            catch ( RuntimeException e ) {
                e.printStackTrace();
                return MutationalUpdateResult.ofErrored( e.getMessage(), pPO );
            }
        }
        return MutationalUpdateResult.ofCanNotUpdate( "Tried " + zTry + " times to update " + getPoSimpleName(), pPO );
    }

    protected <T> T required( String pWhat, T pToTest ) {
        return Objects.requireNonNull( pToTest, getPoSimpleName() + " '" + pWhat +
                                                "' not allowed to be null" );
    }

    protected String getPoSimpleName() {
        return mRepository.getPoType().getSimpleName();
    }
}
