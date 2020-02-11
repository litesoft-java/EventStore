package org.litesoft.events.api.v01.api;

import io.swagger.annotations.ApiParam;
import org.litesoft.events.api.v01.model.EventsArray;
import org.litesoft.events.api.v01.services.V01_EventsStore;
import org.litesoft.restish.support.AbstractRestishController;
import org.litesoft.restish.support.PageLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v01")
@SuppressWarnings({"unused", "DefaultAnnotationParam"})
public class EventsApiController extends AbstractRestishController<EventsArray> implements EventsApi {

    private static final Logger log = LoggerFactory.getLogger(EventsApiController.class);

    private static final PageLimiter LIMITER = PageLimiter.withMin(0).withDefault(100).withMax(1000).build();

    private final V01_EventsStore mStore;

    public EventsApiController(V01_EventsStore pStore) {
        mStore = pStore;
    }

    public ResponseEntity<EventsArray> latestEvents(@ApiParam(value = "email address of the user you want to see Events for.  If not provided, then Events for all 'user's are returned.")
                                                    @Valid @RequestParam(value = "user", required = false)
                                                            String user,
                                                    @Min(0) @Max(1000) @ApiParam(value = "Maximum number of records to return.", allowableValues = "")
                                                    @Valid @RequestParam(value = "limit", required = false)
                                                            Integer limit) {
        return process(() -> EventsArray.from(mStore.latestEvents(authorizePair(), user, LIMITER.normalize(limit))));
    }
}
