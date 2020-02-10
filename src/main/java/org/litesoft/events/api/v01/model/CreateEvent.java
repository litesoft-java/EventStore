package org.litesoft.events.api.v01.model;

import io.swagger.annotations.ApiModel;
import org.litesoft.swagger.SchemaMD;
import org.springframework.validation.annotation.Validated;

/**
 * This form of an Event is for the Create operation and, as such, does NOT allow &#x27;id&#x27; to be provided. If &#x27;when&#x27; is not provided, it defaults to &#x27;now&#x27;!
 */
@ApiModel(description = "This form of an Event is for the Create operation and, as such, does NOT allow 'id' to be provided. If 'when' is not provided, it defaults to 'now'! ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-06T00:19:01.940Z[GMT]")
public class CreateEvent extends AbstractCommonEventProperties<CreateEvent> {
    @Override
    protected SchemaMD<CreateEvent> schemaMD() {
        return CreateEventMD.INSTANCE;
    }
}
