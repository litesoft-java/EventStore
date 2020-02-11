package org.litesoft.events.api.v01.services;

import org.litesoft.events.api.v01.model.CreateEvent;
import org.litesoft.events.api.v01.model.ReturnedEvent;
import org.litesoft.events.api.v01.model.UpdateEvent;
import org.litesoft.restish.support.AuthorizePair;
import org.litesoft.restish.support.PageData;
import org.springframework.stereotype.Service;

@Service
public class V01_EventsStoreImpl implements V01_EventsStore {

    private static final ReturnedEvent E07 = new ReturnedEvent().what("TvlHome")
            /* . . . . . . . . . . . . . . . . . . */.when("2020-02-07T17:30")
            .user("george@the.com").id("d290f1ee-6c54-4b01-90e6-d701748f0857");
    private static final ReturnedEvent E06 = new ReturnedEvent().what("Code")
            /* . . . . . . . . . . . . . . . . . . */.when("2020-02-07T15:59")
            .user("george@the.com").id("d290f1ee-6c54-4b01-90e6-d701748f0856");
    private static final ReturnedEvent E05 = new ReturnedEvent().what("Break")
            /* . . . . . . . . . . . . . . . . . . */.when("2020-02-07T15:46")
            .user("george@the.com").id("d290f1ee-6c54-4b01-90e6-d701748f0855");
    private static final ReturnedEvent E04 = new ReturnedEvent().what("Code")
            /* . . . . . . . . . . . . . . . . . . */.when("2020-02-07T12:45")
            .user("george@the.com").id("d290f1ee-6c54-4b01-90e6-d701748f0854");
    private static final ReturnedEvent E03 = new ReturnedEvent().what("Lunch")
            /* . . . . . . . . . . . . . . . . . . */.when("2020-02-07T11:15")
            .user("george@the.com").id("d290f1ee-6c54-4b01-90e6-d701748f0853");
    private static final ReturnedEvent E02 = new ReturnedEvent().what("Meeting")
            /* . . . . . . . . . . . . . . . . . . */.when("2020-02-07T08:30")
            .user("george@the.com").id("d290f1ee-6c54-4b01-90e6-d701748f0852");
    private static final ReturnedEvent E01 = new ReturnedEvent().what("TvlWork")
            /* . . . . . . . . . . . . . . . . . . */.when("2020-02-07T07:00")
            .user("george@the.com").id("d290f1ee-6c54-4b01-90e6-d701748f0851");

    @Override
    public PageData<ReturnedEvent> latestEvents(AuthorizePair pAuthorizePair, String pUser, int pLimit) {
        return new PageData<>(E07, E06, E05, E04, E03, E02, E01);
    }

    @Override
    public ReturnedEvent createEvent(AuthorizePair pAuthorizePair, CreateEvent pEvent) {
        return null;
    }

    @Override
    public ReturnedEvent deleteEvent(AuthorizePair pAuthorizePair, String pID) {
        return null;
    }

    @Override
    public ReturnedEvent readEvent(AuthorizePair pAuthorizePair, String pID) {
        return null;
    }

    @Override
    public ReturnedEvent updateEvent(AuthorizePair pAuthorizePair, UpdateEvent pEvent) {
        return null;
    }
}
