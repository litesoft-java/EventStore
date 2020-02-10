package org.litesoft.events.api.v02.model;

import io.swagger.annotations.ApiModel;
import org.litesoft.swagger.SchemaMD;
import org.springframework.validation.annotation.Validated;

/**
 * This form of an Event is for the Update operation. If the &#x27;id&#x27; does not exist a 404 will be returned.
 */
@ApiModel(description = "This form of an Event is for the Update operation. If the 'id' does not exist a 404 will be returned.  ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-10T18:05:48.665Z[GMT]")
public class UpdateEvent extends AbstractAllEventProperties<UpdateEvent> {
    @Override
    protected SchemaMD<UpdateEvent> schemaMD() {
        return UpdateEventMD.INSTANCE;
    }
}
