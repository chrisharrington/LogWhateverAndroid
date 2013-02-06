package com.logwhatever.repositories;

import com.logwhatever.models.Log;

public class LogRepository extends BaseRepository<Log> implements ILogRepository {
    public String getCollection() { return "logs"; }
    public Class<Log> getType() { return Log.class; }
}
