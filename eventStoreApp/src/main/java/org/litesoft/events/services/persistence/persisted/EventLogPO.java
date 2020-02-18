package org.litesoft.events.services.persistence.persisted;

import org.litesoft.events.services.persistence.locators.EventLogCodeLocator;
import org.litesoft.persisted.IPersistedObjectIdUuid;
import org.litesoft.persisted.POMetaData;

@SuppressWarnings({"unused", "WeakerAccess"})
public interface EventLogPO extends EventLogCodeLocator,
                                    IPersistedObjectIdUuid {
  // . . . . . . . . . . . . . . . . . . . . . . . . 1234567890
  int CLASS_ID_ADJUSTER_FOR_FIXED_LENGTH_TO_STRING = 1000000000;
  int LARGEST_CLASS_ID = Integer.MAX_VALUE - CLASS_ID_ADJUSTER_FOR_FIXED_LENGTH_TO_STRING;

  String LOCATION_DEFAULT = "!";
  String STATE_DEFAULT = "Pending";

  POMetaData<EventLogPO, Builder> META_DATA = POMetaData.builder( EventLogPO.class, Builder.class )
          .id( String.class, EventLogPO::getId )
          .version( Integer.class, EventLogPO::getVersion )
          .secureField( "OBF", EventLogPO::getOBF, null ) // Unique OrderBy Field (OBF): When - User
          .field( "User", String.class, EventLogPO::getUser, Builder::withUser ) // Significant
          .field( "What", String.class, EventLogPO::getWhat, Builder::withWhat ) // Significant
          .field( "When", String.class, EventLogPO::getWhen, Builder::withWhen ) // Defaulting : Significant
          .field( "LocalTimeOffset", Integer.class, EventLogPO::getLocalTimeOffset, Builder::withLocalTimeOffset ) // Defaulting : Significant
          .field( "LocalTzName", String.class, EventLogPO::getLocalTzName, Builder::withLocalTzName ) // Defaulting : Significant
          .field( "Where", String.class, EventLogPO::getWhere, Builder::withWhere ) // Defaulting : Significant
          .field( "Billable", Boolean.class, EventLogPO::getBillable, Builder::withBillable )
          .field( "Client", String.class, EventLogPO::getClient, Builder::withClient ) // Defaulting : Significant
          .field( "Done", Boolean.class, EventLogPO::getDone, Builder::withDone )
          .build();

  String getOBF(); // OrderByField

  String getUser();

  String getWhat();

  Integer getLocalTimeOffset();

  String getLocalTzName();

  String getWhen();

  String getWhere();

  Boolean getBillable();

  String getClient();

  Boolean getDone();

  Builder toBuilder();

  default Builder copyInsecureForInsert() {
    return META_DATA.copyInsecureForInsert( this );
  }

  default boolean isEquivalent( EventLogPO them ) {
    return META_DATA.areEquivalent( this, them );
  }

  default boolean areAllFieldsEqual( EventLogPO them ) {
    return META_DATA.areEqual( this, them );
  }

  static Builder builder() {
    return META_DATA.createPOBuilder();
  }

  abstract class Builder
          extends AbstractBuilder<EventLogPO, Builder> {
    private String mUser;
    private String mWhat;
    private String mWhen; // ISO8601 DateTime to the Minute
    private Integer mLocalTimeOffset;
    private String mLocalTzName;
    private String mWhere;
    private Boolean mBillable;
    private String mClient;
    private Boolean mDone;

    protected Builder( EventLogPO them ) {
      super( them );
      if ( them != null ) {
        mUser = them.getUser();
        mWhat = them.getWhat();
        mWhen = them.getWhen();
        mLocalTimeOffset = them.getLocalTimeOffset();
        mLocalTzName = them.getLocalTzName();
        mWhere = them.getWhere();
        mBillable = them.getBillable();
        mClient = them.getClient();
        mDone = them.getDone();
      }
    }

    public String getUser() {
      return mUser;
    }

    public void setUser( String pUser ) {
      mUser = pUser;
    }

    public String getWhat() {
      return mWhat;
    }

    public void setWhat( String pWhat ) {
      mWhat = pWhat;
    }

    public String getWhen() {
      return mWhen;
    }

    public void setWhen( String pWhen ) {
      mWhen = pWhen;
    }

    public Integer getLocalTimeOffset() {
      return mLocalTimeOffset;
    }

    public void setLocalTimeOffset( Integer pLocalTimeOffset ) {
      mLocalTimeOffset = pLocalTimeOffset;
    }

    public String getLocalTzName() {
      return mLocalTzName;
    }

    public void setLocalTzName( String pLocalTzName ) {
      mLocalTzName = pLocalTzName;
    }

    public String getWhere() {
      return mWhere;
    }

    public void setWhere( String pWhere ) {
      mWhere = pWhere;
    }

    public Boolean getBillable() {
      return mBillable;
    }

    public void setBillable( Boolean pBillable ) {
      mBillable = pBillable;
    }

    public String getClient() {
      return mClient;
    }

    public void setClient( String pClient ) {
      mClient = pClient;
    }

    public Boolean getDone() {
      return mDone;
    }

    public void setDone( Boolean pDone ) {
      mDone = pDone;
    }

    public Builder withUser( String pUser ) {
      setUser( pUser );
      return this;
    }

    public Builder withWhat( String pWhat ) {
      setWhat( pWhat );
      return this;
    }

    public Builder withWhen( String pWhen ) {
      setWhen( pWhen );
      return this;
    }

    public Builder withLocalTimeOffset( Integer pLocalTimeOffset ) {
      setLocalTimeOffset( pLocalTimeOffset );
      return this;
    }

    public Builder withLocalTzName( String pLocalTzName ) {
      setLocalTzName( pLocalTzName );
      return this;
    }

    public Builder withWhere( String pWhere ) {
      setWhere( pWhere );
      return this;
    }

    public Builder withBillable( Boolean pBillable ) {
      setBillable( pBillable );
      return this;
    }

    public Builder withClient( String pClient ) {
      setClient( pClient );
      return this;
    }

    public Builder withDone( Boolean pDone ) {
      setDone( pDone );
      return this;
    }

    @Override
    protected POMetaData<EventLogPO, Builder> getFieldMetaData() {
      return META_DATA;
    }
  }
}
