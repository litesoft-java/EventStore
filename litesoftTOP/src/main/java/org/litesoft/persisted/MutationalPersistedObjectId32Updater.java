package org.litesoft.persisted;

import java.util.function.Function;

@SuppressWarnings({"unused", "SameParameterValue", "UnusedReturnValue"})
public class MutationalPersistedObjectId32Updater<PO extends IPersistedObjectId32> extends MutationalPersistedObjectUpdater<Integer, PO> {
  private final IPersistedObjectRepositoryId32<PO> mRepository;

  public MutationalPersistedObjectId32Updater( IPersistedObjectRepositoryId32<PO> pRepository ) {
    super( pRepository );
    mRepository = pRepository;
  }

  public MutationalUpdateResult<PO> update( Integer pPO_Id, Function<PO, MutationalUpdateResult<PO>> pChanger ) {
    PO zPO = mRepository.findById( pPO_Id );
    if ( zPO == null ) {
      return MutationalUpdateResult.ofCanNotUpdate( "No " + getPoSimpleName() + " found with ID: " + pPO_Id, null );
    }
    return update( zPO, pChanger );
  }
}
