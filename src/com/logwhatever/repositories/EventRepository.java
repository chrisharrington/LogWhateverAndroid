package com.logwhatever.repositories;

import com.logwhatever.models.Event;
import com.logwhatever.models.Session;
import com.logwhatever.service.IExecutor;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

public class EventRepository extends BaseRepository<Event> implements IEventRepository {
    public String getCollection() { return "events"; }
    public Class<Event> getType() { return Event.class; }

    public void create(Session session, Event event, IExecutor<Void> callback) {
	try {
	    HttpRequestor.post(Configuration.getServiceLocation() + "events", callback, createParametersFromEvent(event));
	    if (callback != null)
		callback.success(null);
	} catch (Exception ex) {
	    if (callback != null)
		callback.error(ex);
	}
    }
    
    private SimpleEntry[] createParametersFromEvent(Event event) {
	List<SimpleEntry> parameters = new ArrayList<SimpleEntry>();
	parameters.add(new SimpleEntry("Id", event.Id));
	parameters.add(new SimpleEntry("UserId", event.UserId));
	parameters.add(new SimpleEntry("LogId", event.LogId));
	parameters.add(new SimpleEntry("LogName", event.LogName));
	parameters.add(new SimpleEntry("Date", event.Date));
	return parameters.toArray(new SimpleEntry[parameters.size()]);
    }
}
