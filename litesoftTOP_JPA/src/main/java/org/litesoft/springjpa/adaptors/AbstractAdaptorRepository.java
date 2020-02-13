package org.litesoft.springjpa.adaptors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.hibernate.exception.ConstraintViolationException;
import org.litesoft.codecs.text.HexUTF8v1;
import org.litesoft.codecs.text.StringCodec;
import org.litesoft.persisted.IPersistedObject;
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
public abstract class AbstractAdaptorRepository<T extends IPersistedObject, B extends POBuilder<T>, CT extends T> implements IPersistedObjectRepository<T> {
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

  protected String createUpdateConstraintViolationMessage( T pPO ) {
    return "!Unique on Update" + getPoType().getSimpleName();
  }

  @Override
  public Page<T> firstPage( int pLimit ) {
    pLimit = normalizeLimit( pLimit );
    return disconnect( pLimit, mRepository.findFirst( createPageableForNextPage( pLimit ) ) );
  }

  @Override
  public Page<T> nextPage( NextPageToken pNextPageToken, int pLimit ) {
    if ( pNextPageToken == null ) {
      throw new IllegalArgumentException( "NextPageToken not allowed to be null" );
    }
    pLimit = normalizeLimit( pLimit );
    return disconnect( pLimit, mRepository.findNext( createPageableForNextPage( pLimit ),
                                                     decodeNextPageToken( pNextPageToken ) ) );
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

  abstract protected T save( CT pEntity );

  abstract protected String extractPagedOrderByAttribute( CT pEntityNonNull );

  @SuppressWarnings("unchecked")
  protected Iterable<T> cast( Iterable<CT> pIterable ) {
    return (Iterable<T>)pIterable;
  }

  @SuppressWarnings("unchecked")
  protected CT cast( T pT ) {
    return (CT)pT;
  }

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
    return cast( mMetaData.copyAll( pEntity ) );
  }

  protected CT checkUpdateAndCopy( T pEntity ) {
    checkActionOnNull( "Update", pEntity );
    pEntity.assertCanUpdate();
    return cast( mMetaData.copyAll( pEntity ) );
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  protected T disconnect( Optional<CT> pOptionalEntity ) {
    return mMetaData.copyAll( pOptionalEntity.orElse( null ) );
  }

  protected T disconnect( CT pEntity ) {
    return mMetaData.copyAll( pEntity );
  }

  protected Page<T> disconnect( int pLimit, List<CT> pFound ) {
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
           new Page<>( zPOs, encodeNextPageToken( zLastCT ) ) :
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

  protected String decodeNextPageToken( NextPageToken pNextPageToken ) {
    String zEncodedToken = pNextPageToken.getEncodedToken();
    String zAfter = NEXT_PAGE_TOKEN_CODEC.decodeSingle( zEncodedToken );
    if ( (zAfter == null) || zAfter.isEmpty() ) {
      throw new IllegalStateException( "Decoded Token '" + zEncodedToken + "' was null or empty" );
    }
    return zAfter;
  }

  protected NextPageToken encodeNextPageToken( CT pLastCT ) {
    if ( pLastCT == null ) {
      throw new IllegalStateException( "Can't encode Token, No Last: " + mClassCT.getSimpleName() );
    }
    String zUnencodedLastValue = extractPagedOrderByAttribute( pLastCT );
    if ( (zUnencodedLastValue == null) || zUnencodedLastValue.isEmpty() ) {
      throw new IllegalStateException( "Can't encode Token, '" + mClassCT.getSimpleName() +
                                       "' OrderBy attribute has no value: " + pLastCT );
    }
    return new NextPageToken( NEXT_PAGE_TOKEN_CODEC.encodeSingle( zUnencodedLastValue ) );
  }
}
