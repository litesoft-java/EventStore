package org.litesoft.events.api.v03.api;

import io.swagger.annotations.ApiParam;
import org.litesoft.events.api.v03.model.PageEvents;
import org.litesoft.events.api.v03.services.V03_EventsStore;
import org.litesoft.restish.support.AbstractRestishController;
import org.litesoft.restish.support.PageLimiter;
import org.litesoft.restish.support.auth.Authorization;
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
@RequestMapping("api/v03")
@SuppressWarnings({"unused", "DefaultAnnotationParam"})
public class V03_EventsLatestApiController extends AbstractRestishController<PageEvents> implements EventsLatestApi {

    private static final Logger log = LoggerFactory.getLogger(V03_EventsLatestApiController.class);

    private static final PageLimiter LIMITER = PageLimiter.withMin(0).withDefault(100).withMax(1000).build();

    private final V03_EventsStore mStore;

    public V03_EventsLatestApiController(Authorization pAuthorization, V03_EventsStore pStore) {
        super(pAuthorization);
        mStore = pStore;
    }

    public ResponseEntity<PageEvents> latestEvents(@ApiParam(value = "email address of the user you want to see Events for.  If not provided, then Events for all 'user's are returned.")
                                                   @Valid @RequestParam(value = "user", required = false)
                                                           String user,
                                                   @Min(0) @Max(1000) @ApiParam(value = "Maximum number of records to return (defaults to 100, the min & max are constraining, not erroring).", allowableValues = "")
                                                   @Valid @RequestParam(value = "limit", required = false)
                                                           Integer limit) {
        return process(() -> PageEvents.from(mStore.latestEvents(authorizePair(), user, LIMITER.normalize(limit))));
    }

}
