package org.litesoft.events.api.v02.api;

import io.swagger.annotations.*;
import org.litesoft.events.api.v02.model.PageEvents;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-10T18:05:48.665Z[GMT]")
@Api(value = "events-latest", description = "the events-latest API")
public interface EventsLatestApi {

    @ApiOperation(value = "search for latest Events (1st page - cursored)", nickname = "latestEvents", notes = "Retrieve the first page of the latest Events (either for a single 'user' or for all). Is repeatable - assuming no data mutations OR creations. If a 'limit' is not provided, the default is 100! ", response = PageEvents.class, tags = {"developers",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success - next page results", response = PageEvents.class),
            @ApiResponse(code = 400, message = "Error - bad input parameter")})
    @RequestMapping(value = "/events-latest",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<PageEvents> latestEvents(@ApiParam(value = "email address of the user you want to see Events for.  If not provided, then Events for all 'user's are returned.") @Valid @RequestParam(value = "user", required = false) String user, @Min(0) @Max(1000) @ApiParam(value = "Maximum number of records to return.", allowableValues = "") @Valid @RequestParam(value = "limit", required = false) Integer limit);

}
