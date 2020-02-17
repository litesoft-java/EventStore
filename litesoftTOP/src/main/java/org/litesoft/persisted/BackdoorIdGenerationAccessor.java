package org.litesoft.persisted;

public class BackdoorIdGenerationAccessor {
  public static <PO> PO generateIdForInsert( PO pPO ) {
    if ( pPO instanceof AbstractPersistedObjectIdImpl ) {
      ((AbstractPersistedObjectIdImpl<?>)pPO).generateIdStrategyForInsert();
    }
    return pPO;
  }
}
