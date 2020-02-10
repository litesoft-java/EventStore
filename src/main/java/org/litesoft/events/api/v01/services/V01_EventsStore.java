package org.litesoft.events.api.v01.services;

import org.litesoft.events.api.v01.model.CreateEvent;
import org.litesoft.events.api.v01.model.ReturnedEvent;
import org.litesoft.events.api.v01.model.UpdateEvent;
import org.litesoft.restish.support.PageData;

public interface V01_EventsStore {
    PageData<ReturnedEvent> latestEvents(String pUser, int pLimit);

    ReturnedEvent createEvent(CreateEvent pEvent);

    ReturnedEvent deleteEvent(String pID);

    ReturnedEvent readEvent(String pID);

    ReturnedEvent updateEvent(UpdateEvent pEvent);
}
