package com.logwhatever.activities;

import android.os.Bundle;
import android.widget.ListView;
import com.logwhatever.R;
import com.logwhatever.adapters.DashboardAdapter;
import com.logwhatever.models.Dashboard;
import com.logwhatever.models.Event;
import com.logwhatever.models.Log;
import com.logwhatever.models.Measurement;
import com.logwhatever.models.Tag;
import com.logwhatever.repositories.IEventRepository;
import com.logwhatever.repositories.ILogRepository;
import com.logwhatever.repositories.IMeasurementRepository;
import com.logwhatever.repositories.ITagRepository;
import com.logwhatever.service.IExecutor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DashboardActivity extends BaseActivity {
    
    private ILogRepository getLogRepository() { return getInjector().getInstance(ILogRepository.class); }
    private IMeasurementRepository getMeasurementRepository() { return getInjector().getInstance(IMeasurementRepository.class); }
    private ITagRepository getTagRepository() { return getInjector().getInstance(ITagRepository.class); }
    private IEventRepository getEventRepository() { return getInjector().getInstance(IEventRepository.class); }
    
    private List<Log> _logs;
    private List<Measurement> _measurements;
    private List<Tag> _tags;
    private List<Event> _events;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);	
        setContentView(R.layout.activity_dashboard);
	setLoading(true);
	
	getLogs();
	getMeasurements();
	getEvents();
	getTags();
    }
    
    private void getLogs() {
	getLogRepository().all(getLogApplication().getSession(), new IExecutor<List<Log>>() {
	    public void execute(List<Log> logs) {
		_logs = logs;
		onDataRetrieved();
	    }
	});
    }
    
    private void getMeasurements() {
	getMeasurementRepository().all(getLogApplication().getSession(), new IExecutor<List<Measurement>>() {
	    public void execute(List<Measurement> measurements) {
		_measurements = measurements;
		onDataRetrieved();
	    }
	});
    }
    
    private void getEvents() {
	getEventRepository().all(getLogApplication().getSession(), new IExecutor<List<Event>>() {
	    public void execute(List<Event> events) {
		_events = events;
		onDataRetrieved();
	    }
	});
    }
    
    private void getTags() {
	getTagRepository().all(getLogApplication().getSession(), new IExecutor<List<Tag>>() {
	    public void execute(List<Tag> tags) {
		_tags = tags;
		onDataRetrieved();
	    }
	});
    }
    
    private void onDataRetrieved() {
	if (_logs == null || _measurements == null || _events == null || _tags == null)
	    return;
	
	ListView list = (ListView) findViewById(R.id.dashboard_listview);
	list.setAdapter(new DashboardAdapter(this, R.layout.listview_dashboard, createDashboardModels()));
	
	setLoading(false);
    }
    
    private List<Dashboard> createDashboardModels() {
	List<Dashboard> dashboards = new ArrayList<Dashboard>();
	
	for (Log log : _logs) {
	    Dashboard dashboard = new Dashboard();
	    dashboard.Name = log.Name;
	    dashboard.Date = getLatestEvent().Date;
	    dashboard.Measurements = getMeasurementsForLog(log);
	    dashboard.Tags = getTagsForLog(log);
	    dashboards.add(dashboard);
	}
	return dashboards;
    }
    
    private Event getLatestEvent() {
	int index = 0;
	Date date = new Date(1, 1, 1);
	for (int i = 0; i < _events.size(); i++) {
	    if (_events.get(i).Date.after(date)) {
		date = _events.get(i).Date;
		index = i;
	    }
	}
	return _events.get(index);
    }
    
    private List<Measurement> getMeasurementsForLog(Log log) {
	List<Measurement> result = new ArrayList<Measurement>();
	for (Measurement measurement : _measurements) {
	    if (measurement.LogId.equals(log.Id))
		result.add(measurement);
	}
	return result;
    }
    
    private List<Tag> getTagsForLog(Log log) {
	if (_tags.isEmpty())
	    return _tags;
	
	Collections.sort(_tags, new TagDateComparator());
	List<Tag> result = new ArrayList<Tag>();
	Date date = _tags.get(0).Date;
	for (Tag tag : _tags)
	    if (tag.LogId.equals(log.Id) && tag.Date.equals(date))
		result.add(tag);
	Collections.sort(result, new TagNameComparator());
	return result;
    }
    
    private class TagDateComparator implements Comparator<Tag> {
	public int compare(Tag first, Tag second) {
	    return second.Date.compareTo(first.Date);
	}
    }
    
    private class TagNameComparator implements Comparator<Tag> {
	public int compare(Tag first, Tag second) {
	    return first.Name.compareTo(second.Name);
	}
    }
}
