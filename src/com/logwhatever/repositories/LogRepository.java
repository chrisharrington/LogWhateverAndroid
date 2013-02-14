package com.logwhatever.repositories;

import com.logwhatever.models.Log;
import com.logwhatever.models.Session;
import com.logwhatever.service.IExecutor;
import java.util.List;

public class LogRepository extends BaseRepository<Log> implements ILogRepository {
    public String getCollection() { return "logs"; }
    public Class<Log> getType() { return Log.class; }

    public void name(final String name, Session session, final IExecutor<Log> callback) throws Exception {
	if (name == null || name.equals(""))
	    throw new Exception("The name is missing.");
	
	all(session, new IExecutor<List<Log>>() {
	    public void execute(List<Log> logs) {
		for (Log log : logs) {
		    if (log.Name.equals(name)) {
			callback.execute(log);
			return;
		    }
		}
		
		callback.execute(null);
	    }

	    public void error(Throwable error) {
		callback.error(error);
	    }
	});
    }

    public void create(Log log) throws Exception {
	if (log == null)
	    throw new Exception("The log is missing.");
	
	//HttpRequestor.post(Configuration.getServiceLocation() + "logs", new SimpleEntry("log", log));
    }
}
