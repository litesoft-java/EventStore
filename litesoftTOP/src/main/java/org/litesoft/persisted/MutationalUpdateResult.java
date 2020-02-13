package org.litesoft.persisted;

@SuppressWarnings({"unused", "WeakerAccess"})
public class MutationalUpdateResult<PO extends IPersistedObject> {

  public enum Status {
    NoChangeNeeded,
    CanNotUpdate,
    Errored,
    Success
  }

  private final Status mStatus;
  private final String mMessageForStatus;
  private final PO mPO;

  private MutationalUpdateResult( Status pStatus, String pMessageForStatus, PO pPO ) {
    mStatus = pStatus;
    mMessageForStatus = pMessageForStatus;
    mPO = pPO;
  }

  public Status getStatus() {
    return mStatus;
  }

  public String getMessageForStatus() {
    return mMessageForStatus;
  }

  public PO getPO() {
    return mPO;
  }

  public static <PO extends IPersistedObject> MutationalUpdateResult<PO> ofNoChangeNeeded( PO pPO ) {
    return ofNoChangeNeeded( null, pPO );
  }

  public static <PO extends IPersistedObject> MutationalUpdateResult<PO> ofNoChangeNeeded( String pMessageForStatus, PO pPO ) {
    return new MutationalUpdateResult<>( Status.NoChangeNeeded, pMessageForStatus, pPO );
  }

  public static <PO extends IPersistedObject> MutationalUpdateResult<PO> ofCanNotUpdate( String pMessageForStatus, PO pPO ) {
    return new MutationalUpdateResult<>( Status.CanNotUpdate, pMessageForStatus, pPO );
  }

  public static <PO extends IPersistedObject> MutationalUpdateResult<PO> ofErrored( String pMessageForStatus, PO pPO ) {
    return new MutationalUpdateResult<>( Status.Errored, pMessageForStatus, pPO );
  }

  public static <PO extends IPersistedObject> MutationalUpdateResult<PO> ofSuccess( PO pPO ) {
    return new MutationalUpdateResult<>( Status.Success, null, pPO );
  }

  public static <PO extends IPersistedObject> boolean isNotSuccess( MutationalUpdateResult<PO> pResult ) {
    return (pResult == null) || (Status.Success != pResult.getStatus());
  }
}
