package org.litesoft.events.api.v03.model;

import io.swagger.annotations.ApiModel;
import org.litesoft.swagger.SchemaMD;
import org.springframework.validation.annotation.Validated;

/**
 * This form of an Event is for the Update (patching) operation and, as such, all properties are optional, and consist of two groups (\&quot;can&#x27;t be changed\&quot;, and all the others where \&quot;at least one should be changed\&quot;).  The \&quot;can&#x27;t be changed\&quot; properties are - &#x27;id&#x27;, &#x27;updateToken&#x27;, &#x27;user&#x27;; if any of these are changed, the update/patch request will return a 400!
 */
@ApiModel(description = "This form of an Event is for the Update (patching) operation and, as such, all properties are optional, and consist of two groups (\"can't be changed\", and all the others where \"at least one should be changed\").  The \"can't be changed\" properties are - 'id', 'updateToken', 'user'; if any of these are changed, the update/patch request will return a 400!  ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-11T00:44:24.775Z[GMT]")
public class PatchEvent extends AbstractAllEventProperties<PatchEvent> {
    @Override
    protected SchemaMD<PatchEvent> schemaMD() {
        return PatchEventMD.INSTANCE;
    }
}
