package org.litesoft.events.api.v03.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.litesoft.swagger.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * AbstractCommonEventProperties
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-11T00:44:24.775Z[GMT]")
public abstract class AbstractCommonEventProperties<T extends AbstractCommonEventProperties<T>> extends Schema<T> {
    @JsonProperty("user")
    private String user = null;

    @JsonProperty("what")
    private String what = null;

    @JsonProperty("when")
    private String when = null;

    @JsonProperty("localTimeOffset")
    private Integer localTimeOffset = null;

    @JsonProperty("localTzName")
    private String localTzName = null;

    @JsonProperty("where")
    private String where = null;

    @JsonProperty("billable")
    private Boolean billable = null;

    @JsonProperty("client")
    private String client = null;

    @JsonProperty("done")
    private Boolean done = null;

    public T user(String user) {
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

    public void setUser(String user) {
        this.user = user;
    }

    public T what(String what) {
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

    public void setWhat(String what) {
        this.what = what;
    }

    public T when(String when) {
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

    public void setWhen(String when) {
        this.when = when;
    }

    public T localTimeOffset(Integer localTimeOffset) {
        this.localTimeOffset = localTimeOffset;
        return us();
    }

    /**
     * informational
     *
     * @return localTimeOffset
     **/
    @ApiModelProperty(example = "-480", value = "informational")

    public Integer getLocalTimeOffset() {
        return localTimeOffset;
    }

    public void setLocalTimeOffset(Integer localTimeOffset) {
        this.localTimeOffset = localTimeOffset;
    }

    public T localTzName(String localTzName) {
        this.localTzName = localTzName;
        return us();
    }

    /**
     * informational
     *
     * @return localTzName
     **/
    @ApiModelProperty(example = "PT", value = "informational")

    public String getLocalTzName() {
        return localTzName;
    }

    public void setLocalTzName(String localTzName) {
        this.localTzName = localTzName;
    }

    public T where(String where) {
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

    public void setWhere(String where) {
        this.where = where;
    }

    public T billable(Boolean billable) {
        this.billable = billable;
        return us();
    }

    /**
     * if TRUE then 'client' is required
     *
     * @return billable
     **/
    @ApiModelProperty(example = "true", value = "if TRUE then 'client' is required")

    public Boolean isBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public T client(String client) {
        this.client = client;
        return us();
    }

    /**
     * Used for possible Billing
     *
     * @return client
     **/
    @ApiModelProperty(example = "Fred's Market", value = "Used for possible Billing")

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public T done(Boolean done) {
        this.done = done;
        return us();
    }

    /**
     * if TRUE, then Billable can't be TRUE. TRUE means that this Event is not tracked and hence may NOT have a limited window size (like a 4 hour Lunch).
     *
     * @return done
     **/
    @ApiModelProperty(example = "false", value = "if TRUE, then Billable can't be TRUE. TRUE means that this Event is not tracked and hence may NOT have a limited window size (like a 4 hour Lunch).")

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
