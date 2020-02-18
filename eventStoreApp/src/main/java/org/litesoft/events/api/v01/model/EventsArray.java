package org.litesoft.events.api.v01.model;

import io.swagger.annotations.ApiModel;
import org.litesoft.restish.support.PageData;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Array of Events (which may be empty).
 */
@ApiModel(description = "Array of Events (which may be empty).")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-06T00:19:01.940Z[GMT]")
public class EventsArray extends ArrayList<ReturnedEvent> {

    public static EventsArray from( PageData<ReturnedEvent> pageData ) {
        EventsArray events = new EventsArray();
        if ( pageData != null ) {
            List<ReturnedEvent> data = pageData.getData();
            if ( data != null ) {
                events.addAll( data );
            }
        }
        return events;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash( super.hashCode() );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "class EventsArray {\n" );
        sb.append( "    " ).append( toIndentedString( super.toString() ) ).append( "\n" );
        sb.append( "}" );
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString( Object o ) {
        if ( o == null ) {
            return "null";
        }
        return o.toString().replace( "\n", "\n    " );
    }
}
