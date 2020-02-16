package org.litesoft.events.services.springjpa.adaptors;

import java.util.List;

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
  public static final String ALL_USER_INDICATOR = ":All";

  private final SpringEventLogRepository mRepository;

  public AdaptorEventLogRepository( SpringEventLogRepository pRepository, TransactionalProxy pTransactionalProxy ) {
    super( EventLogPOImpl.class, EventLogPO.META_DATA, pRepository, pTransactionalProxy );
    mRepository = pRepository;
  }

  @Override
  protected String extractPagedOrderByAttribute( EventLogPOImpl pEntityNonNull ) {
    return pEntityNonNull.getOBF();
  }

  @Override
  public Page<EventLogPO> firstPageAllUsers( int pLimit ) {
    pLimit = normalizeLimit( pLimit );
    return customizeNextPageToken( pLimit, ALL_USER_INDICATOR,
                                   mRepository.findFirst( createPageableForNextPage( pLimit ) ) );
  }

  @Override
  public Page<EventLogPO> firstPageByUser( String pUser, int pLimit ) {
    pLimit = normalizeLimit( pLimit );
    return customizeNextPageToken( pLimit, pUser,
                                   mRepository.firstPageByUser( createPageableForNextPage( pLimit ), pUser ) );
  }

  @Override
  public Page<EventLogPO> nextPage( NextPageToken pNextPageToken, Integer pLimit )
          throws IllegalArgumentException {
    if ( pNextPageToken == null ) {
      throw new IllegalArgumentException( "NextPageToken not allowed to be null" );
    }
    String zBefore = decodeNextPageToken( pNextPageToken );
    if ( pLimit == null ) {
      pLimit = zBefore.length(); // TODO: XXX Totally Bugus!  Customized the NextPageToken
    }
    pLimit = normalizeLimit( pLimit );

    List<EventLogPOImpl> zEventLogs;
    if ( zBefore.startsWith( ALL_USER_INDICATOR ) ) { // TODO: XXX Totally Bugus!  Customized the NextPageToken
      zEventLogs = mRepository.findNext( createPageableForNextPage( pLimit ),
                                         zBefore );
    } else {
      zEventLogs = mRepository.nextPageByUser( createPageableForNextPage( pLimit ),
                                               "Some User", // TODO: XXX Totally Bugus!  Customized the NextPageToken
                                               zBefore );
    }
    return disconnect( pLimit, zEventLogs );
  }

  private Page<EventLogPO> customizeNextPageToken( int pLimit, String pUser, List<EventLogPOImpl> pFound ) {
    return disconnect( pLimit, pFound ); // TODO: Customize the NextPageToken
  }
}
