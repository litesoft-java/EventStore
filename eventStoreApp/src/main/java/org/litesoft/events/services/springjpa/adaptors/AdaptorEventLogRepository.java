package org.litesoft.events.services.springjpa.adaptors;

import org.litesoft.alleviative.validation.Significant;
import org.litesoft.events.services.persistence.locators.EventLogCodeLocator;
import org.litesoft.events.services.persistence.persisted.EventLogPO;
import org.litesoft.events.services.persistence.repos.EventLogRepository;
import org.litesoft.events.services.springjpa.persisted.EventLogPOImpl;
import org.litesoft.events.services.springjpa.repos.SpringEventLogRepository;
import org.litesoft.persisted.NextPageToken;
import org.litesoft.persisted.Page;
import org.litesoft.springjpa.adaptors.AbstractAdaptorRepositoryIdUuid;
import org.litesoft.springjpa.adaptors.TransactionalProxy;
import org.springframework.stereotype.Component;

@Component
public class AdaptorEventLogRepository
        extends AbstractAdaptorRepositoryIdUuid<EventLogPO, EventLogPO.Builder, EventLogPOImpl>
        implements EventLogCodeLocator,
                   EventLogRepository {
  private final SpringEventLogRepository mRepository;

  public AdaptorEventLogRepository( SpringEventLogRepository pRepository, TransactionalProxy pTransactionalProxy ) {
    super( EventLogPOImpl.class, EventLogPO.META_DATA, pRepository, pTransactionalProxy );
    mRepository = pRepository;
  }

  @Override
  protected String extractStandardPagedOrderByAttribute( EventLogPOImpl pEntityNonNull ) {
    return pEntityNonNull.getOBF();
  }

  @Override
  public Page<EventLogPO> firstPageAllUsers( int pLimit ) {
    return firstPage( pLimit );
  }

  @Override
  public Page<EventLogPO> firstPageByUser( String pUser, int pLimit ) {
    pUser = Significant.orNull( pUser );
    if ( pUser == null ) {
      return firstPageAllUsers( pLimit );
    }

    pLimit = normalizeLimit( pLimit );
    return disconnect( pLimit,
                       mRepository.firstPageByUser( createPageableForNextPage( pLimit ), pUser ),
                       new ByUserNPTokenEncoder( pUser ) );
  }

  @Override
  public Page<EventLogPO> nextPage( NextPageToken pNextPageToken, Integer pLimit ) {
    InternalNPT zInternalNPT = decodeNextPageToken( pNextPageToken );
    String zUser = zInternalNPT.getOtherField( "User" );
    if ( zUser == null ) {
      return internalNextPage( zInternalNPT, pLimit );
    }

    int zLimit = normalizeLimit( pLimit, zInternalNPT );
    return disconnect( zLimit,
                       mRepository.nextPageByUser( createPageableForNextPage( zLimit ), zUser,
                                                   zInternalNPT.getLastOrderValue() ),
                       new ByUserNPTokenEncoder( zUser ) );
  }

  private static class ByUserNPTokenEncoder implements NextPageTokenEncoder<String, EventLogPOImpl> {
    private final String mUser;

    public ByUserNPTokenEncoder( String pUser ) {
      mUser = pUser;
    }

    @Override
    public NextPageToken encodeNextPageToken( int pLimit, EventLogPOImpl pLastCT ) {
      if ( pLastCT == null ) {
        throw new IllegalStateException( "Can't encode Token, No Last: EventLogPOImpl" );
      }
      String zUnencodedLastValue = pLastCT.getWhen();
      if ( (zUnencodedLastValue == null) || zUnencodedLastValue.isEmpty() ) {
        throw new IllegalStateException( "Can't encode Token, 'EventLogPOImpl' When attribute has no value: " + pLastCT );
      }
      return new InternalNPT( pLimit, zUnencodedLastValue, "User", mUser ).encode();
    }
  }
}
