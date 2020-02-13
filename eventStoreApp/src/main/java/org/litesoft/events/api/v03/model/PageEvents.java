package org.litesoft.events.api.v03.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.litesoft.restish.support.PageData;
import org.litesoft.swagger.Schema;
import org.litesoft.swagger.SchemaMD;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * An array of Event(s) and a possible next page &#x27;cursor&#x27;
 */
@ApiModel(description = "An array of Event(s) and a possible next page 'cursor'")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-11T00:44:24.775Z[GMT]")
public class PageEvents extends Schema<PageEvents> {
    @JsonProperty("nextToken")
    private String nextToken = null;

    @JsonProperty("data")
    @Valid
    private List<ReturnedEvent> data = null;

    public PageEvents(List<ReturnedEvent> data, String nextToken) {
        this.nextToken = nextToken;
        this.data = data;
    }

    public PageEvents() {
        this(null, null);
    }

    public static PageEvents from(PageData<ReturnedEvent> pageData) {
        return (pageData == null) ? new PageEvents() :
                new PageEvents(pageData.getData(), pageData.getNextToken());
    }

    @Override
    protected SchemaMD<PageEvents> schemaMD() {
        return PageEventsMD.INSTANCE;
    }

    public PageEvents nextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    /**
     * This field is opaque and is used to retrieve the next page of Events (pagenation via 'cursor').
     *
     * @return nextToken
     **/
    @ApiModelProperty(value = "This field is opaque and is used to retrieve the next page of Events (pagenation via 'cursor').")

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public PageEvents data(List<ReturnedEvent> data) {
        this.data = data;
        return this;
    }

    public PageEvents addDataItem(ReturnedEvent dataItem) {
        if (this.data == null) {
            this.data = new ArrayList<ReturnedEvent>();
        }
        this.data.add(dataItem);
        return this;
    }

    /**
     * Array of Events (which may be empty).
     *
     * @return data
     **/
    @ApiModelProperty(value = "Array of Events (which may be empty).")
    @Valid
    public List<ReturnedEvent> getData() {
        return data;
    }

    public void setData(List<ReturnedEvent> data) {
        this.data = data;
    }
}
