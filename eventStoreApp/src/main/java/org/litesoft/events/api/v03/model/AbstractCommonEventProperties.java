package org.litesoft.events.api.v03.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.litesoft.alleviative.beans.SetValue;
import org.litesoft.swagger.MappedSchema;
import org.litesoft.swagger.SchemaMD;
import org.springframework.validation.annotation.Validated;

/**
 * AbstractCommonEventProperties
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-11T00:44:24.775Z[GMT]")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractCommonEventProperties<T extends AbstractCommonEventProperties<T>> extends MappedSchema<T> {

    protected abstract AbstractCommonEventPropertiesMD<T> abstractCommonEventPropertiesMD();

    @Override
    protected final SchemaMD<T> schemaMD() {
        return abstractCommonEventPropertiesMD();
    }

    /**
     * Unique (when combined with 'when').
     *
     * @return user
     **/
    @ApiModelProperty(example = "your.name@your-company.com", value = "Unique (when combined with 'when').")

    public String getUser() {
        return fieldGet( abstractCommonEventPropertiesMD().nfUser() );
    }

    public void setUser( String pUser ) {
        fieldSet( abstractCommonEventPropertiesMD().nfUser(), pUser );
    }

    public SetValue<String> onUser() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfUser() );
    }

    public T withUser( String pUser ) {
        setUser( pUser );
        return us();
    }

    /**
     * Get what
     *
     * @return what
     **/
    @ApiModelProperty(example = "Lunch", value = "")

    public String getWhat() {
        return fieldGet( abstractCommonEventPropertiesMD().nfWhat() );
    }

    public void setWhat( String pWhat ) {
        fieldSet( abstractCommonEventPropertiesMD().nfWhat(), pWhat );
    }

    public SetValue<String> onWhat() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfWhat() );
    }

    public T withWhat( String pWhat ) {
        setWhat( pWhat );
        return us();
    }

    /**
     * As these are Human events, minute resolution is all that is supported.  Unique (when combined with 'user').
     *
     * @return when
     **/
    @ApiModelProperty(example = "2020-01-15T17:08Z", value = "As these are Human events, minute resolution is all that is supported.  Unique (when combined with 'user').")

    public String getWhen() {
        return fieldGet( abstractCommonEventPropertiesMD().nfWhen() );
    }

    public void setWhen( String pWhen ) {
        fieldSet( abstractCommonEventPropertiesMD().nfWhen(), pWhen );
    }

    public SetValue<String> onWhen() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfWhen() );
    }

    public T withWhen( String pWhen ) {
        setWhen( pWhen );
        return us();
    }

    /**
     * informational
     *
     * @return localTimeOffset
     **/
    @ApiModelProperty(example = "-480", value = "informational")

    public Integer getLocalTimeOffset() {
        return fieldGet( abstractCommonEventPropertiesMD().nfLocalTimeOffset() );
    }

    public void setLocalTimeOffset( Integer pLocalTimeOffset ) {
        fieldSet( abstractCommonEventPropertiesMD().nfLocalTimeOffset(), pLocalTimeOffset );
    }

    public SetValue<Integer> onLocalTimeOffset() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfLocalTimeOffset() );
    }

    public T withLocalTimeOffset( Integer pLocalTimeOffset ) {
        setLocalTimeOffset( pLocalTimeOffset );
        return us();
    }

    /**
     * informational
     *
     * @return localTzName
     **/
    @ApiModelProperty(example = "PT", value = "informational")

    public String getLocalTzName() {
        return fieldGet( abstractCommonEventPropertiesMD().nfLocalTzName() );
    }

    public void setLocalTzName( String pLocalTzName ) {
        fieldSet( abstractCommonEventPropertiesMD().nfLocalTzName(), pLocalTzName );
    }

    public SetValue<String> onLocalTzName() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfLocalTzName() );
    }

    public T withLocalTzName( String pLocalTzName ) {
        setLocalTzName( pLocalTzName );
        return us();
    }

    /**
     * informational
     *
     * @return where
     **/
    @ApiModelProperty(example = "Woodinville", value = "informational")

    public String getWhere() {
        return fieldGet( abstractCommonEventPropertiesMD().nfWhere() );
    }

    public void setWhere( String pWhere ) {
        fieldSet( abstractCommonEventPropertiesMD().nfWhere(), pWhere );
    }

    public SetValue<String> onWhere() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfWhere() );
    }

    public T withWhere( String pWhere ) {
        setWhere( pWhere );
        return us();
    }

    /**
     * if TRUE then 'client' is required
     *
     * @return billable
     **/
    @ApiModelProperty(example = "true", value = "if TRUE then 'client' is required")

    public Boolean getBillable() {
        return fieldGet( abstractCommonEventPropertiesMD().nfBillable() );
    }

    public void setBillable( Boolean pBillable ) {
        fieldSet( abstractCommonEventPropertiesMD().nfBillable(), pBillable );
    }

    public SetValue<Boolean> onBillable() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfBillable() );
    }

    public T withBillable( Boolean pBillable ) {
        setBillable( pBillable );
        return us();
    }

    /**
     * Used for possible Billing
     *
     * @return client
     **/
    @ApiModelProperty(example = "Fred's Market", value = "Used for possible Billing")

    public String getClient() {
        return fieldGet( abstractCommonEventPropertiesMD().nfClient() );
    }

    public void setClient( String pClient ) {
        fieldSet( abstractCommonEventPropertiesMD().nfClient(), pClient );
    }

    public SetValue<String> onClient() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfClient() );
    }

    public T withClient( String pClient ) {
        setClient( pClient );
        return us();
    }

    /**
     * if TRUE, then Billable can't be TRUE. TRUE means that this Event is not tracked and hence may NOT have a limited window size (like a 4 hour Lunch).
     *
     * @return done
     **/
    @ApiModelProperty(example = "false", value = "if TRUE, then Billable can't be TRUE. TRUE means that this Event is not tracked and hence may NOT have a limited window size (like a 4 hour Lunch).")

    public Boolean getDone() {
        return fieldGet( abstractCommonEventPropertiesMD().nfDone() );
    }

    public void setDone( Boolean pDone ) {
        fieldSet( abstractCommonEventPropertiesMD().nfDone(), pDone );
    }

    public SetValue<Boolean> onDone() {
        return fieldGetSetValue( abstractCommonEventPropertiesMD().nfDone() );
    }

    public T withDone( Boolean pDone ) {
        setDone( pDone );
        return us();
    }
}
