package org.litesoft.events.api.v02.api;

import io.swagger.annotations.ApiParam;
import org.litesoft.restish.support.AbstractRestishController;
import org.litesoft.events.api.v02.model.CreateEvent;
import org.litesoft.events.api.v02.model.ReturnedEvent;
import org.litesoft.events.api.v02.model.UpdateEvent;
import org.litesoft.events.api.v02.services.V02_EventsStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v02")
@SuppressWarnings({"unused", "DefaultAnnotationParam", "MVCPathVariableInspection"})
public class EventApiController extends AbstractRestishController<ReturnedEvent> implements EventApi {

    private static final Logger log = LoggerFactory.getLogger(EventApiController.class);

    private final V02_EventsStore mStore;

    public EventApiController(V02_EventsStore pStore) {
        mStore = pStore;
    }

    @Override
    public ResponseEntity<ReturnedEvent> createEvent(@ApiParam(value = "Event to create", required = true)
                                                     @Valid @RequestBody
                                                             CreateEvent body) {
        return process(HttpStatus.CREATED, () -> mStore.createEvent(authorizePair(), body));
    }

    @Override
    public ResponseEntity<ReturnedEvent> deleteEvent(@ApiParam(value = "", required = true)
                                                     @PathVariable("id")
                                                             String id) {
        return process(() -> mStore.deleteEvent(authorizePair(), id));
    }

    @Override
    public ResponseEntity<ReturnedEvent> readEvent(@ApiParam(value = "", required = true)
                                                   @PathVariable("id")
                                                           String id) {
        return process(() -> mStore.readEvent(authorizePair(), id));
    }

    @Override
    public ResponseEntity<ReturnedEvent> updateEvent(@ApiParam(value = "Updated Event", required = true)
                                                     @Valid @RequestBody
                                                             UpdateEvent body) {
        return process(() -> mStore.updateEvent(authorizePair(), body));
    }
}
