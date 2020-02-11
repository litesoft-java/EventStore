package org.litesoft.events.api.v03.api;

import io.swagger.annotations.ApiParam;
import org.litesoft.events.api.v03.model.PageEvents;
import org.litesoft.events.api.v03.services.V03_EventsStore;
import org.litesoft.restish.support.AbstractRestishController;
import org.litesoft.restish.support.PageLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v03")
@SuppressWarnings({"unused", "DefaultAnnotationParam", "MVCPathVariableInspection"})
public class V03_EventsNextPageApiController extends AbstractRestishController<PageEvents> implements EventsNextPageApi {

    private static final Logger log = LoggerFactory.getLogger(V03_EventsNextPageApiController.class);

    private static final PageLimiter LIMITER = PageLimiter.withMin(0).withDefault(100).withMax(1000).build();

    private final V03_EventsStore mStore;

    public V03_EventsNextPageApiController(V03_EventsStore pStore) {
        mStore = pStore;
    }

    public ResponseEntity<PageEvents> nextEvents(@ApiParam(value = "a 'cursor' for retrieving the next page of Events.", required = true)
                                                 @PathVariable("nextToken")
                                                         String nextToken,
                                                 @Min(0) @Max(1000) @ApiParam(value = "Maximum number of records to return (defaults to 100, the min & max are constraining, not erroring).", allowableValues = "")
                                                 @Valid @RequestParam(value = "limit", required = false)
                                                         Integer limit) {
        return process(() -> PageEvents.from(mStore.nextEvents(authorizePair(), nextToken, LIMITER.optionallyNormalize(limit))));
    }

}
