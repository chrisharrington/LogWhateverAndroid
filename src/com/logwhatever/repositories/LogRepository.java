package com.logwhatever.repositories;

import com.logwhatever.models.Log;
import com.logwhatever.models.Session;
import com.logwhatever.service.IExecutor;
import java.util.List;

public class LogRepository extends BaseRepository<Log> implements ILogRepository {
    public String getCollection() { return "logs"; }
    public Class<Log> getType() { return Log.class; }

    public void name(final String name, Session session, final IExecutor<Log> callback) {
	if (name == null || name.equals("")) {
	    callback.error(new Exception("The name is missing."));
	    return;
	}
	
	all(session, new IExecutor<List<Log>>() {
	    public void success(List<Log> logs) {
		for (Log log : logs) {
		    if (log.Name.equals(name)) {
			callback.success(log);
			return;
		    }
		}
		
		callback.success(null);
	    }

	    public void error(Throwable error) {
		callback.error(error);
	    }
	});
    }

    public void create(Session session, Log log) {
	//HttpRequestor.post(Configuration.getServiceLocation() + "logs", new SimpleEntry("log", log));
    }
}
