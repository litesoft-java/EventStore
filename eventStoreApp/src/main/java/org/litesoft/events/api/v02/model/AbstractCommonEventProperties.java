package org.litesoft.events.api.v02.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.litesoft.swagger.Schema;

/**
 * AbstractCommonEventProperties
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-06T00:19:01.940Z[GMT]")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractCommonEventProperties<T extends AbstractCommonEventProperties<T>> extends Schema<T> {
    @JsonProperty("user")
    private String user = null;

    @JsonProperty("what")
    private String what = null;

    @JsonProperty("when")
    private String when = null;

    @JsonProperty("where")
    private String where = null;

    @JsonProperty("done")
    private Boolean done = null;

    public T user( String user ) {
        this.user = user;
        return us();
    }

    /**
     * Unique (when combined with 'when').
     *
     * @return user
     **/
    @ApiModelProperty(example = "your.name@your-company.com", value = "Unique (when combined with 'when').")

    public String getUser() {
        return user;
    }

    public void setUser( String user ) {
        this.user = user;
    }

    public T what( String what ) {
        this.what = what;
        return us();
    }

    /**
     * Get what
     *
     * @return what
     **/
    @ApiModelProperty(example = "Lunch", value = "")

    public String getWhat() {
        return what;
    }

    public void setWhat( String what ) {
        this.what = what;
    }

    public T when( String when ) {
        this.when = when;
        return us();
    }

    /**
     * As these are Human events, minute resolution is all that is supported.  Unique (when combined with 'user').
     *
     * @return when
     **/
    @ApiModelProperty(example = "2020-01-15T17:08Z", value = "As these are Human events, minute resolution is all that is supported.  Unique (when combined with 'user').")

    public String getWhen() {
        return when;
    }

    public void setWhen( String when ) {
        this.when = when;
    }

    public T where( String where ) {
        this.where = where;
        return us();
    }

    /**
     * informational
     *
     * @return where
     **/
    @ApiModelProperty(example = "Woodinville", value = "informational")

    public String getWhere() {
        return where;
    }

    public void setWhere( String where ) {
        this.where = where;
    }

    public T done( Boolean done ) {
        this.done = done;
        return us();
    }

    /**
     * TRUE means that this Event is not tracked and hence may NOT have a limited window size (like a 4 hour Lunch).
     *
     * @return done
     **/
    @ApiModelProperty(example = "false", value = "TRUE means that this Event is not tracked and hence may NOT have a limited window size (like a 4 hour Lunch).")

    public Boolean isDone() {
        return done;
    }

    public void setDone( Boolean done ) {
        this.done = done;
    }
}
