package org.litesoft.springjpa.adaptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.hibernate.exception.ConstraintViolationException;
import org.litesoft.alleviative.validation.Significant;
import org.litesoft.codecs.text.HexUTF8v1;
import org.litesoft.codecs.text.StringCodec;
import org.litesoft.persisted.BackdoorIdGenerationAccessor;
import org.litesoft.persisted.IPersistedObject;
import org.litesoft.persisted.IPersistedObjectId;
import org.litesoft.persisted.IPersistedObjectRepository;
import org.litesoft.persisted.NextPageToken;
import org.litesoft.persisted.POBuilder;
import org.litesoft.persisted.POMetaData;
import org.litesoft.persisted.Page;
import org.litesoft.persisted.PersistorConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AbstractAdaptorRepository<ID, T extends IPersistedObjectId<ID>, B extends POBuilder<T>, CT extends T>
        implements IPersistedObjectRepository<ID, T> {

    private static final StringCodec NEXT_PAGE_TOKEN_CODEC = HexUTF8v1.CONSISTENT;

    private final Class<CT> mClassCT;
    private final POMetaData<T, B> mMetaData;
    private final SpringRepository<CT> mRepository;
    private final TransactionalProxy mTransactionalProxy;

    protected AbstractAdaptorRepository( Class<CT> pClassCT, POMetaData<T, B> pMetaData,
                                         SpringRepository<CT> pRepository, TransactionalProxy pTransactionalProxy,
                                         IPersistedObject[] pAdditionalInstanceTypes ) {
        mClassCT = pClassCT;
        mMetaData = pMetaData;
        mRepository = pRepository;
        mTransactionalProxy = pTransactionalProxy;
        System.out.println( "Initialized: " + pMetaData.getTypeSimpleName() );
        for ( IPersistedObject zAdditionalInstanceType : pAdditionalInstanceTypes ) {
            System.out.println( "Initialized: " + zAdditionalInstanceType.getDisplayType().getSimpleName() );
        }
    }

    @Override
    public T insert( T pPO ) {
        return saveProxy( checkInsertAndCopy( pPO ), this::createInsertConstraintViolationMessage );
    }

    protected String createInsertConstraintViolationMessage( T pPO ) {
        return "!Unique on Insert " + getPoType().getSimpleName();
    }

    @Override
    public T update( T pPO ) {
        return saveProxy( checkUpdateAndCopy( pPO ), this::createUpdateConstraintViolationMessage );
    }

    private T saveProxy( CT pPO, Function<CT, String> pConstraintViolationMessageBuilder ) {
        try {
            return mTransactionalProxy.execute( () -> save( pPO ) );
        }
        catch ( ConstraintViolationException | DataIntegrityViolationException e ) {
            throw new PersistorConstraintViolationException( pConstraintViolationMessageBuilder.apply( pPO ) );
        }
    }

    public void delete( T pPO ) {
        deleteProxy( checkDeleteAndCopy( pPO ), this::createUpdateConstraintViolationMessage );
    }

    private void deleteProxy( CT pPO, Function<CT, String> pConstraintViolationMessageBuilder ) {
        try {
            mTransactionalProxy.execute( () -> mRepository.delete( pPO ) );
        }
        catch ( ConstraintViolationException | DataIntegrityViolationException e ) {
            throw new PersistorConstraintViolationException( pConstraintViolationMessageBuilder.apply( pPO ) );
        }
    }

    protected String createUpdateConstraintViolationMessage( T pPO ) {
        return "!Unique on Update" + getPoType().getSimpleName();
    }

    @Override
    public Page<T> firstPage( int pLimit ) {
        pLimit = normalizeLimit( pLimit );
        return disconnect( pLimit,
                           mRepository.findFirst( createPageableForNextPage( pLimit ) ),
                           this::encodeStandardNextPageToken );
    }

    @Override
    public Page<T> nextPage( NextPageToken pNextPageToken, Integer pLimit ) {
        InternalNPT zInternalNPT = decodeNextPageToken( pNextPageToken );
        return internalNextPage( zInternalNPT, pLimit );
    }

    protected Page<T> internalNextPage( InternalNPT pInternalNPT, Integer pLimit ) {
        int zLimit = normalizeLimit( pLimit, pInternalNPT );
        return disconnect( zLimit,
                           mRepository.findNext( createPageableForNextPage( zLimit ),
                                                 pInternalNPT.getLastOrderValue() ),
                           this::encodeStandardNextPageToken );
    }

    protected int normalizeLimit( Integer pLimit, InternalNPT pInternalNPT ) {
        return normalizeLimit( (pLimit != null) ? pLimit : pInternalNPT.getLimit() );
    }

    protected int normalizeLimit( int pLimit ) {
        return ((1 <= pLimit) && (pLimit <= 10000)) ? pLimit : 1;
    }

    protected Pageable createSinglePagePageable( int pLimit ) {
        return PageRequest.of( 0, pLimit );
    }

    protected Pageable createPageableForNextPage( int pLimit ) {
        // Add 1 so that one extra row is fetched, which reduces the odds of a false Next Page indicator!
        return PageRequest.of( 0, pLimit + 1 );
    }

    protected T save( CT pEntity ) {
        return disconnect( mRepository.save( pEntity ) );
    }

    abstract protected String extractStandardPagedOrderByAttribute( CT pEntityNonNull );

    @SuppressWarnings("unchecked")
    protected Iterable<T> cast( Iterable<CT> pIterable ) {
        return (Iterable<T>)pIterable;
    }

    @SuppressWarnings("unchecked")
    protected CT cast( T pT ) {
        return (CT)pT;
    }

    @SuppressWarnings("rawtypes")
    protected CT checkCast( T pT ) {
        if ( pT != null ) {
            Class zClassTI = pT.getClass();
            if ( mClassCT != zClassTI ) {
                if ( !mClassCT.isAssignableFrom( zClassTI ) ) {
                    throw new IllegalStateException( "Not of type '" + mClassCT +
                                                     "', but of type '" + zClassTI +
                                                     "' - Not compatible - value: " + pT );
                }
            }
        }
        return cast( pT );
    }

    protected CT checkInsertAndCopy( T pEntity ) {
        checkActionOnNull( "Insert", pEntity );
        pEntity.assertCanInsert();
        return BackdoorIdGenerationAccessor.generateIdForInsert( cast( mMetaData.copyAll( pEntity ) ) );
    }

    protected CT checkUpdateAndCopy( T pEntity ) {
        checkActionOnNull( "Update", pEntity );
        pEntity.assertCanUpdate();
        return cast( mMetaData.copyAll( pEntity ) );
    }

    protected CT checkDeleteAndCopy( T pEntity ) {
        checkActionOnNull( "Delete", pEntity );
        pEntity.assertCanDelete();
        return cast( mMetaData.copyAll( pEntity ) );
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected T disconnect( Optional<CT> pOptionalEntity ) {
        return mMetaData.copyAll( pOptionalEntity.orElse( null ) );
    }

    protected T disconnect( CT pEntity ) {
        return mMetaData.copyAll( pEntity );
    }

    protected Page<T> disconnect( int pLimit, List<CT> pFound, NextPageTokenEncoder<ID, CT> pNextPageTokenEncoder ) {
        if ( pFound.isEmpty() ) {
            return Page.empty();
        }
        int zReturnCount = Math.min( pLimit, pFound.size() );
        List<T> zPOs = new ArrayList<>( zReturnCount );
        Iterator<CT> zIterator = pFound.iterator();
        CT zLastCT = null;
        for ( int i = 0; i < zReturnCount; i++ ) {
            zLastCT = zIterator.next();
            zPOs.add( disconnect( zLastCT ) );
        }
        return zIterator.hasNext() ?
               new Page<>( zPOs, pNextPageTokenEncoder.encodeNextPageToken( pLimit, zLastCT ) ) :
               new Page<>( zPOs );
    }

    protected List<T> disconnect( List<CT> pFound ) {
        List<T> zPOs = new ArrayList<>( pFound.size() );
        for ( CT zCT : pFound ) {
            zPOs.add( disconnect( zCT ) );
        }
        return zPOs;
    }

    protected void checkActionOnNull( String pAction, T pEntity ) {
        if ( pEntity == null ) {
            throw new NullPointerException( "Can NOT '" + pAction + "' a null " + mMetaData.getTypeSimpleName() );
        }
    }

    protected static class InternalNPT {
        private final String mLastOrderValue;
        private final int mLimit;
        private final Map<String, String> mOtherFields = new HashMap<>();

        public InternalNPT( int pLimit, String pLastOrderValue, String... pOtherKeyValue ) {
            mLimit = pLimit;
            mLastOrderValue = Significant.orNull( pLastOrderValue );
            for ( int i = 0; i < pOtherKeyValue.length; ) {
                String zKey = pOtherKeyValue[i++];
                internalAdd( zKey, pOtherKeyValue[i++] );
            }
            if ( mLastOrderValue == null ) {
                throw new IllegalArgumentException( "LastOrderValue was insignificant" );
            }
        }

        private void internalAdd( String pKey, String pValue ) {
            pKey = Significant.orNull( pKey );
            if ( pKey == null ) {
                throw new IllegalArgumentException( "OtherField Key was null or empty" );
            }
            mOtherFields.put( pKey, Significant.orNull( pValue ) );
        }

        public InternalNPT add( String pKey, String pValue ) {
            internalAdd( pKey, pValue );
            return this;
        }

        public String getLastOrderValue() {
            return mLastOrderValue;
        }

        public int getLimit() {
            return mLimit;
        }

        public Set<String> getOtherFieldKeys() {
            return new HashSet<>( mOtherFields.keySet() );
        }

        public String getOtherField( String pKey ) {
            return mOtherFields.get( pKey );
        }

        public NextPageToken encode() {
            List<String> zFields = new ArrayList<>( 2 + (mOtherFields.size() * 2) );
            zFields.add( mLastOrderValue ); // 0
            zFields.add( Integer.toString( mLimit ) ); // 1
            for ( Map.Entry<String, String> zEntry : mOtherFields.entrySet() ) {
                zFields.add( zEntry.getKey() );
                zFields.add( zEntry.getValue() );
            }
            return new NextPageToken( NEXT_PAGE_TOKEN_CODEC.encodeMultiple( zFields.toArray( new String[0] ) ) );
        }

        public static InternalNPT from( NextPageToken pNextPageToken ) {
            if ( pNextPageToken == null ) {
                throw new IllegalStateException( "NextPageToken was null" );
            }
            String zEncodedToken = Significant.orNull( pNextPageToken.getEncodedToken() );
            if ( zEncodedToken == null ) {
                throw new IllegalStateException( "Encoded Token was null or empty" );
            }
            String[] zFields = NEXT_PAGE_TOKEN_CODEC.decodeMultiple( -2, zEncodedToken );
            String zLastOrderValue = Significant.orNull( zFields[0] );
            String zLimitStr = Significant.orNull( zFields[1] );
            if ( zLastOrderValue == null ) {
                throw new IllegalStateException( "Decoded Token '" + zEncodedToken + "' LastOrderValue was null or empty" );
            }
            if ( zLimitStr == null ) {
                throw new IllegalStateException( "Decoded Token '" + zEncodedToken + "' Limit was null or empty" );
            }
            int zLimit;
            try {
                zLimit = Integer.parseInt( zLimitStr );
            }
            catch ( NumberFormatException e ) {
                throw new IllegalStateException( "Decoded Token '" + zEncodedToken + "' Limit was not numeric" );
            }
            InternalNPT zINPT = new InternalNPT( zLimit, zLastOrderValue );
            for ( int i = 2; i < zFields.length; ) {
                String zKey = Significant.orNull( zFields[i++] );
                String zValue = Significant.orNull( zFields[i++] );
                if ( zKey == null ) {
                    throw new IllegalStateException( "Decoded Token '" + zEncodedToken + "' OtherFields[" + (i - 2) +
                                                     "] Key was null or empty" );
                }
                zINPT.internalAdd( zKey, zValue );
            }
            return zINPT;
        }
    }

    protected interface NextPageTokenEncoder<ID, CT extends IPersistedObjectId<ID>> {
        NextPageToken encodeNextPageToken( int pLimit, CT pLastCT );
    }

    protected InternalNPT decodeNextPageToken( NextPageToken pNextPageToken ) {
        return InternalNPT.from( pNextPageToken );
    }

    protected NextPageToken encodeStandardNextPageToken( int pLimit, CT pLastCT ) {
        if ( pLastCT == null ) {
            throw new IllegalStateException( "Can't encode Token, No Last: " + mClassCT.getSimpleName() );
        }
        String zUnencodedLastValue = extractStandardPagedOrderByAttribute( pLastCT );
        if ( (zUnencodedLastValue == null) || zUnencodedLastValue.isEmpty() ) {
            throw new IllegalStateException( "Can't encode Token, '" + mClassCT.getSimpleName() +
                                             "' OrderBy attribute has no value: " + pLastCT );
        }
        return new InternalNPT( pLimit, zUnencodedLastValue ).encode();
    }
}
