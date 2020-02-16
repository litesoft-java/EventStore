package org.litesoft.persisted;

import org.litesoft.alleviative.functionalinterfaces.FluentSetter;
import org.litesoft.bean.DataObjectWithRequiredFields;
import org.litesoft.bean.IdAccessor;
import org.litesoft.bean.VersionAccessor;

import java.util.List;
import java.util.Map;

public interface IPersistedObjectIdCommon<ID> extends IPersistedObjectId<ID>, VersionAccessor {

  @Override
  default void assertCanInsert() {
    if ( getId() != null ) {
      throw new IllegalArgumentException( "Can NOT 'insert' a '" + getDisplayType().getSimpleName() +
                                          "' with an Id: " + this );
    }
    if ( getVersion() != null ) {
      throw new IllegalArgumentException( "Can NOT 'insert' a '" + getDisplayType().getSimpleName() +
                                          "' with a Version: " + this );
    }
  }

  @Override
  default void assertCanUpdate() {
    if ( getId() == null ) {
      throw new IllegalArgumentException( "Can NOT 'update' a '" + getDisplayType().getSimpleName() +
                                          "' without an Id: " + this );
    }
    if ( getVersion() == null ) {
      throw new IllegalArgumentException( "Can NOT 'update' a '" + getDisplayType().getSimpleName() +
                                          "' without a Version: " + this );
    }
  }

  abstract class AbstractIdAndVersionBuilder<ID, T extends IPersistedObjectIdCommon<ID>, B extends POBuilder<T>>
          extends DataObjectWithRequiredFields implements POBuilder<T>,
                                                          IdAccessor<ID>, VersionAccessor {
    private ID id;
    private Integer version;

    protected AbstractIdAndVersionBuilder( IPersistedObjectIdCommon<ID> them ) {
      if ( them != null ) {
        id = them.getId();
        version = them.getVersion();
      }
    }

    @Override
    public ID getId() {
      return id;
    }

    @Override
    public Integer getVersion() {
      return version;
    }

    @Override
    public T buildFrom( Map<String, ?> pFieldMap ) {
      B zB = castB();
      POMetaData<T, B> zMetaData = getFieldMetaData();
      id = getField( pFieldMap, zMetaData.getIdField() );
      version = getInteger( pFieldMap, zMetaData.getVersionField() );
      List<POField<T, B, ?>> zOtherFields = zMetaData.getOtherFields();
      for ( POField<T, B, ?> zField : zOtherFields ) {
        FluentSetter<B, ?> zSetter = zField.getSetter();
        if ( !zField.isSecure() && (zSetter != null) ) {
          zSetter.apply( zB, getField( pFieldMap, zField ) );
        }
      }
      return build();
    }

    abstract protected POMetaData<T, B> getFieldMetaData();

    private Integer getInteger( Map<String, ?> pFieldMap, POField<T, B, ?> pField ) {
      return getField( pFieldMap, pField );
    }

    private <U> U getField( Map<String, ?> pFieldMap, POField<T, B, ?> pField ) {
      String zName = pField.getName();
      Object zValue = pFieldMap.get( lCase1stChar( zName ) );
      if ( zValue != null ) {
        Class<?> zActualType = zValue.getClass();
        Class<?> zExpectedType = pField.getType();
        if ( !zExpectedType.isAssignableFrom( zActualType ) ) {
          throw new IllegalStateException( "Unable to assign field '" + zName +
                                           "' to type '" + zExpectedType.getSimpleName() +
                                           "' a type of '" + zActualType.getSimpleName() + "': " + zValue );
        }
      }
      return cast( zValue );
    }

    private B castB() {
      return cast( this );
    }

    @SuppressWarnings("unchecked")
    private static <U> U cast( Object pValue ) {
      return (U)pValue;
    }

    private static String lCase1stChar( String zString ) {
      if ( (zString != null) && !zString.isEmpty() ) {
        StringBuilder sb = new StringBuilder( zString );
        sb.setCharAt( 0, Character.toLowerCase( sb.charAt( 0 ) ) );
        zString = sb.toString();
      }
      return zString;
    }
  }
}
