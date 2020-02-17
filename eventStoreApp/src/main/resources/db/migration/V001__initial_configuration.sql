/* PostgreSQL */

CREATE TABLE event_log (
  id varchar(36) NOT NULL, /* UUID */
  version Integer,
  unique_obf       varchar(271)  NOT NULL, /* Unique OrderBy Field (OBF) (16+1+254): ts_when - user_email */
  user_email       varchar(254)  NOT NULL,
  what             varchar(64)   NOT NULL,
  ts_when          varchar(17)   NOT NULL, /* ISO8601 DateTime to the Minute */
  lcl_time_offset  varchar(8)    DEFAULT NULL,
  lcl_tz_name      varchar(8)    DEFAULT NULL,
  lcl_where        varchar(32)   DEFAULT NULL,
  billable         BOOLEAN       DEFAULT FALSE,
  client           varchar(64)   DEFAULT NULL,
  done             BOOLEAN       DEFAULT FALSE,
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX event_log_orderby_ndx ON event_log (unique_obf);

CREATE UNIQUE INDEX event_log_user_when_ndx ON event_log (user_email, ts_when);