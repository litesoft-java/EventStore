package org.litesoft.events.api.v03.api;

import io.swagger.annotations.*;
import org.litesoft.events.api.v03.model.CreateEvent;
import org.litesoft.events.api.v03.model.PatchEvent;
import org.litesoft.events.api.v03.model.ReturnedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-11T00:44:24.775Z[GMT]")
@Api(value = "event", description = "the event API")
public interface EventApi {

    @ApiOperation(value = "create an Event", nickname = "createEvent", notes = "Create an Event", response = ReturnedEvent.class, tags = {"developers",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success", response = ReturnedEvent.class),
            @ApiResponse(code = 400, message = "Error - invalid input, object invalid"),
            @ApiResponse(code = 403, message = "Error - Forbidden to change another user's Events"),
            @ApiResponse(code = 409, message = "Error - existing Event with the same User & When")})
    @RequestMapping(value = "/event",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<ReturnedEvent> createEvent( @ApiParam(value = "Event to create", required = true)
                                               @Valid @RequestBody
                                                       CreateEvent body );

    @ApiOperation(value = "delete an Event", nickname = "deleteEvent", notes = "Delete an Event by 'updateToken'", response = ReturnedEvent.class, tags = {"developers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ReturnedEvent.class),
            @ApiResponse(code = 403, message = "Error - Forbidden to change another user's Events"),
            @ApiResponse(code = 404, message = "Error - Event from 'updateToken' not found"),
            @ApiResponse(code = 428, message = "Error - 'updateToken' not current")})
    @RequestMapping(value = "/event/{updateToken}",
            produces = {"application/json"},
            method = RequestMethod.DELETE)
    ResponseEntity<ReturnedEvent> deleteEvent( @ApiParam(value = "", required = true)
                                               @PathVariable("updateToken")
                                                       String updateToken );

    @ApiOperation(value = "read an Event", nickname = "readEvent", notes = "Reads an Event by 'id'", response = ReturnedEvent.class, tags = {"developers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ReturnedEvent.class),
            @ApiResponse(code = 404, message = "Error - Event with 'id' not found")})
    @RequestMapping(value = "/event/{id}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<ReturnedEvent> readEvent( @ApiParam(value = "", required = true)
                                             @PathVariable("id")
                                                     String id );

    @ApiOperation(value = "update an Event", nickname = "updateEvent", notes = "Update an Event (patches)", response = ReturnedEvent.class, tags = {"developers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ReturnedEvent.class),
            @ApiResponse(code = 204, message = "Success - Current Event properties already match requested changes (No Content)"),
            @ApiResponse(code = 400, message = "Error - invalid input, object invalid"),
            @ApiResponse(code = 403, message = "Error - Forbidden to change another user's Events"),
            @ApiResponse(code = 404, message = "Error - Event from 'updateToken' not found"),
            @ApiResponse(code = 409, message = "Error - existing Event with the same User & When"),
            @ApiResponse(code = 428, message = "Error - 'updateToken' not current")})
    @RequestMapping(value = "/event/{updateToken}",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PATCH)
    ResponseEntity<ReturnedEvent> updateEvent( @ApiParam(value = "Event changes to patch", required = true)
                                               @Valid @RequestBody
                                                       PatchEvent body,
                                               @ApiParam(value = "", required = true)
                                               @PathVariable("updateToken")
                                                       String updateToken );
}
