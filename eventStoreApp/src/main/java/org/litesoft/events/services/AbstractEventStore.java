package org.litesoft.events.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.litesoft.alleviative.Cast;
import org.litesoft.alleviative.beans.SetValue;
import org.litesoft.alleviative.validation.Significant;
import org.litesoft.codecs.UnacceptableEncodingException;
import org.litesoft.codecs.text.HexUTF8v1;
import org.litesoft.events.exceptions.RestishDuplicateWhenUserException;
import org.litesoft.events.exceptions.RestishEventNoChangeException;
import org.litesoft.events.services.persistence.persisted.EventLogPO;
import org.litesoft.events.services.persistence.repos.EventLogRepository;
import org.litesoft.persisted.NextPageToken;
import org.litesoft.persisted.POField;
import org.litesoft.persisted.POMetaData;
import org.litesoft.persisted.Page;
import org.litesoft.persisted.PersistorConstraintViolationException;
import org.litesoft.restish.support.auth.AuthorizePair;
import org.litesoft.restish.support.exceptions.RestishBadParamException;
import org.litesoft.restish.support.exceptions.RestishException;
import org.litesoft.restish.support.exceptions.RestishInvalidObjectException;
import org.litesoft.restish.support.exceptions.RestishUpdateIdNotFoundException;
import org.litesoft.restish.support.exceptions.RestishUpdateTokenNotCurrentException;

@SuppressWarnings({"UnnecessaryLocalVariable", "unused"})
public abstract class AbstractEventStore {

    private final Map<String, POField<EventLogPO, EventLogPO.Builder, ?>> mApiSupportedAccessors = new HashMap<>();
    private final Map<String, Function<EventLogPO, ?>> mApiUnsupportedAccessors = new HashMap<>();
    protected final String mApiVersion;
    protected final EventLogRepository mRepository;

    protected AbstractEventStore( String pApiVersion, EventLogRepository pRepository,
                                  POMetaData<EventLogPO, EventLogPO.Builder> pMetaData,
                                  String[] pApiSupportedFields, String[] pApiUnsupportedFields ) {
        mApiVersion = pApiVersion;
        mRepository = pRepository;

        Set<String> zApiSupportedFields = new HashSet<>( Arrays.asList( pApiSupportedFields ) );
        Set<String> zApiUnsupportedFields = new HashSet<>( Arrays.asList( pApiUnsupportedFields ) );
        pMetaData.getOtherFields().forEach( pField -> {
            String zName = pField.getName();
            if ( zApiSupportedFields.contains( zName ) ) {
                mApiSupportedAccessors.put( zName, pField );
            } else if ( zApiUnsupportedFields.contains( zName ) ) {
                mApiUnsupportedAccessors.put( zName, pField.getGetter() );
            }
        } );
    }

    protected Page<EventLogPO> firstPage( AuthorizePair pAuthorizePair, String pUser, int pLimit ) {
        pUser = Significant.orNull( pUser );

        Page<EventLogPO> zPage = (pUser == null) ?
                                 mRepository.firstPageAllUsers( pLimit ) :
                                 mRepository.firstPageByUser( pUser, pLimit );
        return zPage;
    }

    protected Page<EventLogPO> nextPage( AuthorizePair pAuthorizePair, String pNextToken, Integer pLimit ) {
        Page<EventLogPO> zPage = mRepository.nextPage( new NextPageToken( pNextToken ), pLimit );
        return zPage;
    }

    protected String requiredSignificantField( String pWhat, String pValue ) {
        pValue = Significant.orNull( pValue );
        if ( pValue != null ) {
            return pValue;
        }
        throw new RestishInvalidObjectException( "required field '" + pWhat + "' missing or not significant" );
    }

    protected <T> T requiredNotNullField( String pWhat, T pValue ) {
        if ( pValue != null ) {
            return pValue;
        }
        throw new RestishInvalidObjectException( "required field '" + pWhat + "' missing or null" );
    }

    protected void verifySameOnUpdate( String pExpected, String pWhat, SetValue<String> pSetValue ) {
        if ( pSetValue != null ) {
            String zValue = checkSignificantRequiredFieldChange( pWhat, pSetValue ).get();
            if ( !pExpected.equals( zValue ) ) {
                throw new RestishInvalidObjectException( "the '" + pWhat + "' field can NOT be changed! ('" + pExpected +
                                                         "' was != new '" + zValue + "')" );
            }
        }
    }

    protected SetValue<String> checkSignificantRequiredFieldChange( String pWhat, SetValue<String> pSetValue ) {
        if ( pSetValue == null ) {
            return null;
        }
        String zValue = pSetValue.get();
        if ( zValue == null ) {
            throw new RestishInvalidObjectException( "required field '" + pWhat + "' was null" );
        }
        return check( pWhat, pSetValue, zValue, "updated required" );
    }

    protected SetValue<String> checkSignificantFieldChange( String pWhat, SetValue<String> pSetValue ) {
        String zValue = (pSetValue == null) ? null : pSetValue.get();
        if ( zValue == null ) {
            return pSetValue;
        }
        return check( pWhat, pSetValue, zValue, "updated" );
    }

    private SetValue<String> check( String pWhat, SetValue<String> pSetValue, String pValue, String pErrorPrefix ) {
        String zTrimmed = Significant.orNull( pValue );
        if ( pValue.equals( zTrimmed ) ) {
            return pSetValue;
        }
        throw new RestishInvalidObjectException( pErrorPrefix + " field '" + pWhat + "' was either empty or had leading or trailing spaces" );
    }

    private class ChangeCollector {
        private final Map<String, Object> mChanges = new HashMap<>();

        public void checkAdd( EventLogPO pPO, String pName, Object pValue ) {
            if ( !Objects.equals( pValue, mApiSupportedAccessors.get( pName ).getGetter().apply( pPO ) ) ) { // Change!
                mChanges.put( pName, pValue );
            }
        }

        public EventLogPO.Builder complete( EventLogPO pPO ) {
            if ( mChanges.isEmpty() ) {
                throw new RestishEventNoChangeException();
            }
            for ( Map.Entry<String, Function<EventLogPO, ?>> zEntry : mApiUnsupportedAccessors.entrySet() ) {
                Object zValue = zEntry.getValue().apply( pPO );
                if ( zValue != null ) {
                    throw new RestishException( 451,
                                                "Api version '" + mApiVersion + "' does not support field '" +
                                                zEntry.getKey() + "'!  Event probably managed by a subsequent API version." );
                }
            }
            EventLogPO.Builder zBuilder = pPO.toBuilder();
            for ( Map.Entry<String, Object> zEntry : mChanges.entrySet() ) {
                String zName = zEntry.getKey();
                Object zValue = zEntry.getValue();
                POField<EventLogPO, EventLogPO.Builder, ?> zField = mApiSupportedAccessors.get( zName );
                if ( zValue != null ) {
                    if ( zValue.getClass() != zField.getType() ) {
                        throw new RestishInvalidObjectException( "Field '" + zName +
                                                                 "' expected type '" + zField.getClass().getSimpleName() +
                                                                 "', but got: " + zValue.getClass().getSimpleName() );
                    }
                }
                zField.getSetter().apply( zBuilder, Cast.it( zValue ) );
            }

            return zBuilder;
        }
    }

    protected EventLogPO.Builder checkUpdateApiBasedPatches( EventLogPO zPO, Object... pNameSetValuePairs ) {
        ChangeCollector zChanges = new ChangeCollector();
        for ( int i = 0; i < pNameSetValuePairs.length; ) {
            String zName = pNameSetValuePairs[i++].toString();
            SetValue<?> zSetValue = Cast.it( pNameSetValuePairs[i++] );
            if ( zSetValue != null ) {
                Object zValue = zSetValue.get();
                zChanges.checkAdd( zPO, zName, zValue );
            }
        }
        EventLogPO.Builder zBuilder = zChanges.complete( zPO );
        return zBuilder;
    }

    protected EventLogPO.Builder checkUpdateApiBasedChanges( String pId, Object... pNameValuePairs ) {
        pId = Significant.orNull( pId );
        if ( pId == null ) {
            throw new RestishUpdateIdNotFoundException( "was Null for update" );
        }
        EventLogPO zPO = mRepository.findById( pId );

        if ( zPO == null ) {
            throw new RestishUpdateIdNotFoundException();
        }
        ChangeCollector zChanges = new ChangeCollector();
        for ( int i = 0; i < pNameValuePairs.length; ) {
            String zName = pNameValuePairs[i++].toString();
            Object zValue = pNameValuePairs[i++];
            zChanges.checkAdd( zPO, zName, zValue );
        }
        EventLogPO.Builder zBuilder = zChanges.complete( zPO );
        return zBuilder;
    }

    protected EventLogPO update( EventLogPO.Builder pBuilder ) {
        EventLogPO zUpdated;
        EventLogPO zPO = pBuilder.build();
        try {
            zUpdated = mRepository.update( zPO );
        }
        catch ( PersistorConstraintViolationException e ) {
            throw new RestishDuplicateWhenUserException( " " + e.getMessage() );
        }
        return zUpdated;
    }

    protected EventLogPO getForChangeByUpdateToken( String pUpdateToken ) {
        UpdateTokenValues zTokenValues = decodeUpdateToken( pUpdateToken );
        EventLogPO zPO = mRepository.findById( Significant.orNull( zTokenValues.getId() ) );
        if ( (zPO != null) && !zPO.getVersion().equals( zTokenValues.getVersion() ) ) { // Left to Right!
            throw new RestishUpdateTokenNotCurrentException();
        }
        return zPO;
    }

    protected static class UpdateTokenValues {
        private final String mId;
        private final int mVersion;

        public UpdateTokenValues( String pId, int pVersion ) {
            mId = pId;
            mVersion = pVersion;
        }

        public String getId() {
            return mId;
        }

        public int getVersion() {
            return mVersion;
        }
    }

    protected String encodeUpdateToken( EventLogPO pPO ) {
        String zId = Significant.orNull( pPO.getId() );
        Integer zVersion = pPO.getVersion();
        if ( (zId == null) || (zVersion == null) ) {
            throw new IllegalStateException( "EventLog UpdateToken attempted on non-persisted entity" );
        }
        return HexUTF8v1.INSTANCE.encodeMultiple( zId, // 0
                                                  Integer.toString( zVersion ) ); // 1
    }

    protected UpdateTokenValues decodeUpdateToken( String pUpdateToken ) {
        try {
            String[] zValues = HexUTF8v1.INSTANCE.decodeMultiple( 2, pUpdateToken );
            String zId = zValues[0];
            int zVersion = Integer.parseInt( zValues[1] );
            return new UpdateTokenValues( zId, zVersion );
        }
        catch ( UnacceptableEncodingException | NumberFormatException e ) {
            throw new RestishBadParamException( " - Update Token: " + e.getMessage() );
        }
    }
}
