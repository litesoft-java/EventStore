package org.litesoft.events.services.persistence.repos;

import org.litesoft.events.services.persistence.locators.EventLogCodeLocator;
import org.litesoft.events.services.persistence.persisted.EventLogPO;
import org.litesoft.persisted.IPersistedObjectRepositoryIdUuid;
import org.litesoft.persisted.NextPageToken;
import org.litesoft.persisted.Page;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface EventLogRepository extends EventLogCodeLocator,
                                            IPersistedObjectRepositoryIdUuid<EventLogPO> {
  @Override
  default Class<EventLogPO> getPoType() {
    return EventLogPO.class;
  }

  /**
   * Delete the PO where the ID & Version match.
   *
   * @param pPO !null
   */
  void delete( EventLogPO pPO );

  /**
   * Load the First Page of Events (for All Users) with a maximum of <code>pLimit</code> results.
   *
   * @param pLimit if <code>pLimit</code> is not between (inclusive) 1 and 10000, it is treated as 1.
   * @return !null (if the contained <code>NextPageToken</code> is null, then there are no more pages).
   */
  Page<EventLogPO> firstPageAllUsers( int pLimit );

  /**
   * Load the First Page of Events (for the passed in User) with a maximum of <code>pLimit</code> results.
   *
   * @param pUser  selector for Events for this passed in User.
   * @param pLimit if <code>pLimit</code> is not between (inclusive) 1 and 10000, it is treated as 1.
   * @return !null (if the contained <code>NextPageToken</code> is null, then there are no more pages).
   */
  Page<EventLogPO> firstPageByUser( String pUser, int pLimit );

  /**
   * Load the Next Page of Events with a maximum of <code>pLimit</code> results.
   *
   * @param pNextPageToken !null (see @throws)
   * @param pLimit         Nullable, but if not null, then if <code>pLimit</code> is not between (inclusive) 1 and 10000, it is treated as 1.
   * @return !null (if the contained <code>NextPageToken</code> is null, then there are no more pages).
   * @throws IllegalArgumentException if the <code>pNextPageToken</code> is null
   */
  Page<EventLogPO> nextPage( NextPageToken pNextPageToken, Integer pLimit )
          throws IllegalArgumentException;
}
