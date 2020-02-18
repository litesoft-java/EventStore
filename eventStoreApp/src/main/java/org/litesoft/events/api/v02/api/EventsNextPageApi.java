package org.litesoft.events.api.v02.api;

import io.swagger.annotations.*;
import org.litesoft.events.api.v02.model.PageEvents;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-10T18:05:48.665Z[GMT]")
@Api(value = "events-nextPage", description = "the events-nextPage API")
public interface EventsNextPageApi {

    @ApiOperation(value = "(search) next page of Events - cursored", nickname = "nextEvents", notes = "Retrieve a subsequent page of Events, based on a previous latest request using a 'nextToken' cursor. Is repeatable - assuming no data mutations OR creations (with past dates). If a 'limit' is not provided, the default is the 'limit' used on the request that generated the 'nextToken'! ", response = PageEvents.class, tags = {"developers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - next page results", response = PageEvents.class),
            @ApiResponse(code = 400, message = "Error - bad input parameter")})
    @RequestMapping(value = "/events-nextPage/{nextToken}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<PageEvents> nextEvents( @ApiParam(value = "a 'cursor' for retreiving the next page of Events.", required = true)
                                           @PathVariable("nextToken")
                                                   String nextToken,
                                           @Min(0) @Max(1000) @ApiParam(value = "Maximum number of records to return.", allowableValues = "")
                                           @Valid @RequestParam(value = "limit", required = false)
                                                   Integer limit );
}
