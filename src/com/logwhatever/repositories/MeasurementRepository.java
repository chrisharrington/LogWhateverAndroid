package com.logwhatever.repositories;

import com.logwhatever.models.Log;
import com.logwhatever.models.Measurement;
import com.logwhatever.models.Session;
import com.logwhatever.service.IExecutor;
import java.util.ArrayList;
import java.util.List;

public class MeasurementRepository extends BaseRepository<Measurement> implements IMeasurementRepository {
    public String getCollection() { return "measurements"; }
    public Class<Measurement> getType() { return Measurement.class; }

    public void log(Session session, final Log log, final IExecutor<List<Measurement>> callback) {
	List<Measurement> result = new ArrayList<Measurement>();
	all(session, new IExecutor<List<Measurement>>() {
	    public void success(List<Measurement> measurements) {
		List<Measurement> result = new ArrayList<Measurement>();
		for (Measurement measurement : measurements)
		    if (measurement.LogId.equals(log.Id))
			result.add(measurement);
		callback.success(result);
	    }

	    public void error(Throwable error) {
		callback.error(error);
	    }
	});
    }
}
