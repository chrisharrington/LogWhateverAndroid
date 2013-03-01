package com.logwhatever.repositories;

import com.logwhatever.models.Log;
import com.logwhatever.models.Session;
import com.logwhatever.service.IExecutor;

public interface ILogRepository extends IRepository<Log> {
    void name(String name, Session session, IExecutor<Log> callback);
    void create(Session session, Log log);
}