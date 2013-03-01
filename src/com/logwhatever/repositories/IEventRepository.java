package com.logwhatever.repositories;

import com.logwhatever.models.Event;
import com.logwhatever.models.Session;
import com.logwhatever.service.IExecutor;

public interface IEventRepository extends IRepository<Event> {
    void create(Session session, Event event, IExecutor<Void> callback);
}
