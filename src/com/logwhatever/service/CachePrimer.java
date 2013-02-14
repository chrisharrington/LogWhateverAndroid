package com.logwhatever.service;

import com.google.inject.Inject;
import com.logwhatever.models.Event;
import com.logwhatever.models.Log;
import com.logwhatever.models.Measurement;
import com.logwhatever.models.Session;
import com.logwhatever.models.Tag;
import com.logwhatever.repositories.IEventRepository;
import com.logwhatever.repositories.ILogRepository;
import com.logwhatever.repositories.IMeasurementRepository;
import com.logwhatever.repositories.ITagRepository;
import java.util.List;

public class CachePrimer implements ICachePrimer {
    
    @Inject ILogRepository LogRepository;
    @Inject IEventRepository EventRepository;
    @Inject IMeasurementRepository MeasurementRepository;
    @Inject ITagRepository TagRepository;

    private boolean _logsComplete;
    private boolean _eventsComplete;
    private boolean _measurementsComplete;
    private boolean _tagsComplete;
    private IExecutor<Void> _callback;
    
    public void prime(Session session, IExecutor<Void> callback) {
	_callback = callback;
	
	primeLogs(session);
    }

    private void primeLogs(Session session) {
	LogRepository.all(session, new IExecutor<List<Log>>() {
	    public void execute(List<Log> parameter) { _logsComplete = true; }
	    public void error(Throwable error) { _logsComplete = true; }
	});
    }
    
    private void primeEvents(Session session) {
	EventRepository.all(session, new IExecutor<List<Event>>() {
	    public void execute(List<Event> parameter) { _eventsComplete = true; }
	    public void error(Throwable error) { _eventsComplete = true; }
	});
    }
    
    private void primeMeasurements(Session session) {
	MeasurementRepository.all(session, new IExecutor<List<Measurement>>() {
	    public void execute(List<Measurement> parameter) { _measurementsComplete = true; }
	    public void error(Throwable error) { _measurementsComplete = true; }
	});
    }
    
    private void primeTags(Session session) {
	TagRepository.all(session, new IExecutor<List<Tag>>() {
	    public void execute(List<Tag> parameter) { _tagsComplete = true; }
	    public void error(Throwable error) { _tagsComplete = true; }
	});
    }
 
    private void onPrimeComplete() {
	if (!_logsComplete || !_eventsComplete || !_measurementsComplete || !_tagsComplete)
	    return;
	
	_callback.execute(null);
    }
}
