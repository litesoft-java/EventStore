package org.litesoft.events.api.v03.model;

import io.swagger.annotations.ApiModel;
import org.springframework.validation.annotation.Validated;

/**
 * This form of an Event is for the Create operation and, as such, does NOT allow &#x27;id&#x27; or &#x27;updateToken&#x27; to be provided. If &#x27;when&#x27; is not provided, it defaults to &#x27;now&#x27;!
 */
@ApiModel(description = "This form of an Event is for the Create operation and, as such, does NOT allow 'id' or 'updateToken' to be provided. If 'when' is not provided, it defaults to 'now'! ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-11T00:44:24.775Z[GMT]")
public class CreateEvent extends AbstractCommonEventProperties<CreateEvent> {
    @Override
    protected AbstractCommonEventPropertiesMD<CreateEvent> abstractCommonEventPropertiesMD() {
        return CreateEventMD.INSTANCE;
    }
}
