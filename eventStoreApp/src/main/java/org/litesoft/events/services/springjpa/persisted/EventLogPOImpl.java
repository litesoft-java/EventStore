package org.litesoft.events.services.springjpa.persisted;

import java.time.Instant;

import org.litesoft.events.services.persistence.locators.EventLogCodeLocator;
import org.litesoft.events.services.persistence.persisted.EventLogPO;
import org.litesoft.springjpa.persisted.PersistedObjectIdUuidImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings({"unused", "WeakerAccess", "rawtypes"})
@Entity
@Table(name = "event_log")
public class EventLogPOImpl extends PersistedObjectIdUuidImpl implements EventLogCodeLocator,
                                                                         EventLogPO {
  static {
    META_DATA.builderFactory( BuilderImpl::new );
  }

  private String unique_obf; // OrderBy Field (OBF) Unique: state - date - course - location - encodedPassword
  private String user;
  private String what;
  private String when; // ISO8601 DateTime to the Minute
  @Column(name = "lcl_time_offset")
  private String localTimeOffset;
  @Column(name = "lcl_tz_name")
  private String localTzName;
  private String where;
  private Boolean billable;
  private String client;
  private Boolean done;

  @Override
  public Class getDisplayType() {
    return EventLogPO.class;
  }

  @Override
  public String getOBF() {
    return unique_obf;
  }

  @Override
  public String getUser() {
    return user;
  }

  @Override
  public String getWhat() {
    return what;
  }

  @Override
  public String getWhen() {
    return when;
  }

  @Override
  public String getLocalTimeOffset() {
    return localTimeOffset;
  }

  @Override
  public String getLocalTzName() {
    return localTzName;
  }

  @Override
  public String getWhere() {
    return where;
  }

  @Override
  public Boolean getBillable() {
    return billable;
  }

  @Override
  public String getClient() {
    return client;
  }

  @Override
  public Boolean getDone() {
    return done;
  }

  @Override
  public Builder toBuilder() {
    return new BuilderImpl( this );
  }

  private EventLogPO populate( BuilderImpl them ) {
    populateAbstract( them );
    user = requiredEmail( "User", them.getUser() );
    what = requiredSignificant( "What", them.getWhat() );
    when = requiredISO8601Min( "When", them.getWhen(), Instant::now );
    localTimeOffset = significantOr( them.getLocalTimeOffset(), null );
    localTzName = significantOr( them.getLocalTzName(), null );
    where = significantOr( them.getWhere(), null );
    billable = them.getBillable();
    client = significantOr( them.getClient(), null );
    done = them.getDone();

    unique_obf = // Generated
            when + "|" +
            user;
    return this;
  }

  private static class BuilderImpl extends Builder {
    public BuilderImpl( EventLogPO them ) {
      super( them );
    }

    public EventLogPO build() {
      return new EventLogPOImpl().populate( this );
    }
  }

  @Override
  public String toString() {
    return META_DATA.toString( this );
  }
}
