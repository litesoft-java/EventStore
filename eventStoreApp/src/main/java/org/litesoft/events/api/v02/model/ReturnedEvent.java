package org.litesoft.events.api.v02.model;

import io.swagger.annotations.ApiModel;
import org.litesoft.swagger.SchemaMD;
import org.springframework.validation.annotation.Validated;

/**
 * This form of an Event is for Returning an existing Event and, as such, it is &#x27;Complete&#x27; - all properties and all rules.
 */
@ApiModel(description = "This form of an Event is for Returning an existing Event and, as such, it is 'Complete' - all properties and all rules.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-10T18:05:48.665Z[GMT]")
public class ReturnedEvent extends AbstractAllEventProperties<ReturnedEvent> {
    @Override
    protected SchemaMD<ReturnedEvent> schemaMD() {
        return ReturnedEventMD.INSTANCE;
    }
}
