package org.litesoft.events.api.v01.api;

import io.swagger.annotations.ApiParam;
import org.litesoft.events.api.v01.model.CreateEvent;
import org.litesoft.events.api.v01.model.ReturnedEvent;
import org.litesoft.events.api.v01.model.UpdateEvent;
import org.litesoft.events.api.v01.services.V01_EventsStore;
import org.litesoft.restish.support.AbstractRestishController;
import org.litesoft.restish.support.auth.Authorization;
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
@RequestMapping("api/v01")
@SuppressWarnings({"unused", "DefaultAnnotationParam", "MVCPathVariableInspection"})
public class V01_EventApiController extends AbstractRestishController<ReturnedEvent> implements EventApi {

  private static final Logger log = LoggerFactory.getLogger( V01_EventApiController.class );

  private final V01_EventsStore mStore;

  public V01_EventApiController( Authorization pAuthorization, V01_EventsStore pStore ) {
    super( pAuthorization );
    mStore = pStore;
  }

  @Override
  public ResponseEntity<ReturnedEvent> createEvent( @ApiParam(value = "Event to create", required = true)
                                                    @Valid @RequestBody
                                                            CreateEvent body ) {
    return process( HttpStatus.CREATED, () -> mStore.createEvent( authorizePair(), body ) );
  }

  @Override
  public ResponseEntity<ReturnedEvent> deleteEvent( @ApiParam(value = "", required = true)
                                                    @PathVariable("id")
                                                            String id ) {
    return process( () -> mStore.deleteEvent( authorizePair(), id ) );
  }

  @Override
  public ResponseEntity<ReturnedEvent> readEvent( @ApiParam(value = "", required = true)
                                                  @PathVariable("id")
                                                          String id ) {
    return process( () -> mStore.readEvent( authorizePair(), id ) );
  }

  @Override
  public ResponseEntity<ReturnedEvent> updateEvent( @ApiParam(value = "Updated Event", required = true)
                                                    @Valid @RequestBody
                                                            UpdateEvent body ) {
    return process( () -> mStore.updateEvent( authorizePair(), body ) );
  }
}
