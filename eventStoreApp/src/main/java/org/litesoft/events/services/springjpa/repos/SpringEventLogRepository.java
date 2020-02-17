package org.litesoft.events.services.springjpa.repos;

import java.util.List;

import org.litesoft.events.services.persistence.locators.EventLogCodeLocator;
import org.litesoft.events.services.springjpa.persisted.EventLogPOImpl;
import org.litesoft.springjpa.adaptors.SpringRepositoryIdUuid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@SuppressWarnings("unused")
public interface SpringEventLogRepository extends EventLogCodeLocator,
                                                  SpringRepositoryIdUuid<EventLogPOImpl> {

  @Query("FROM EventLogPOImpl t WHERE t.user = :user ORDER BY t.when DESC")
  List<EventLogPOImpl> firstPageByUser( Pageable limit, @Param("user") String user );

  @Query("FROM EventLogPOImpl t WHERE t.user = :user AND t.when < :before  ORDER BY t.when DESC")
  List<EventLogPOImpl> nextPageByUser( Pageable limit, @Param("user") String user, @Param("before") String before );

  @Override
  @Query("FROM EventLogPOImpl t ORDER BY t.unique_obf DESC")
  List<EventLogPOImpl> findFirst( Pageable limit );

  @Override
  @Query("FROM EventLogPOImpl t WHERE t.unique_obf < :before ORDER BY t.unique_obf DESC")
  List<EventLogPOImpl> findNext( Pageable limit, @Param("before") String before );
}
