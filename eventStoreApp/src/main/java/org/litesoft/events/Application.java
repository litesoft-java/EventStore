package org.litesoft.events;

import org.litesoft.events.services.persistence.persisted.EventLogPO;
import org.litesoft.events.services.persistence.repos.EventLogRepository;
import org.litesoft.persisted.Page;
import org.litesoft.restish.support.auth.Authorization;
import org.litesoft.restish.support.auth.ThreadLocalAuthorization;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
  private static final String VERSION = "2020-02-16";

  public static void main( String[] args ) {
    SpringApplication.run( Application.class, args );
  }

  @Bean
  public Authorization createAuthorization() {
    return new ThreadLocalAuthorization();
  }

  @Bean
  public VersionSupplier createVersionSupplier( EventLogRepository pRepository ) {
    initializeEventLogs( pRepository );

    return () -> VERSION;
  }

  private EventLogPO po( String pWhen, String pWhat ) {
    return EventLogPO.builder()
            .withUser( "george@the.com" ).withWhen( pWhen ).withWhat( pWhat )
            .build();
  }

  private void initializeEventLogs( EventLogRepository pRepository ) {
    Page<EventLogPO> zPage = pRepository.firstPageAllUsers( 1 );
    if ( !zPage.isEmpty() ) {
      return;
    }
    pRepository.insert( po( "2020-02-07T17:30Z", "TvlHome" ) );
    pRepository.insert( po( "2020-02-07T15:59Z", "Code" ) );
    pRepository.insert( po( "2020-02-07T15:46Z", "Break" ) );
    pRepository.insert( po( "2020-02-07T12:45Z", "Code" ) );
    pRepository.insert( po( "2020-02-07T11:15Z", "Lunch" ) );
    pRepository.insert( po( "2020-02-07T08:30Z", "Meeting" ) );
    pRepository.insert( po( "2020-02-07T07:00Z", "TvlWork" ) );
  }
}