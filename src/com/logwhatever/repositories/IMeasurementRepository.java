package com.logwhatever.repositories;

import com.logwhatever.models.Log;
import com.logwhatever.models.Measurement;
import com.logwhatever.models.Session;
import com.logwhatever.service.IExecutor;
import java.util.List;

public interface IMeasurementRepository extends IRepository<Measurement> {
    void log(Session session, Log log, IExecutor<List<Measurement>> callback);
    void uniqueForLog(Session session, Log log, IExecutor<List<Measurement>> callback);
    void create(Session session, Measurement measurement);
}
