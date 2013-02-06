package com.logwhatever.repositories;

import com.logwhatever.models.Event;

public class EventRepository extends BaseRepository<Event> implements IEventRepository {
    public String getCollection() { return "events"; }
    public Class<Event> getType() { return Event.class; }
}
