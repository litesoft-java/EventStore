package org.litesoft.events.api.v02.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * AbstractAllEventProperties
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-06T00:19:01.940Z[GMT]")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractAllEventProperties<T extends AbstractAllEventProperties<T>> extends AbstractCommonEventProperties<T> {
    @JsonProperty("id")
    private String id = null;

    public T id( String id ) {
        this.id = id;
        return us();
    }

    /**
     * Unique - system assigned.
     *
     * @return id
     **/
    @ApiModelProperty(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", value = "Unique - system assigned.")

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }
}
