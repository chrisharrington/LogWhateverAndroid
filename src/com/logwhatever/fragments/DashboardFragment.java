package com.logwhatever.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.logwhatever.R;
import com.logwhatever.models.Event;
import com.logwhatever.models.Log;
import com.logwhatever.models.Measurement;
import com.logwhatever.models.Tag;
import com.logwhatever.repositories.IEventRepository;
import com.logwhatever.repositories.ILogRepository;
import com.logwhatever.repositories.IMeasurementRepository;
import com.logwhatever.repositories.ITagRepository;
import com.logwhatever.service.IExecutor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends BaseFragment {

    private ILogRepository getLogRepository() { return getInjector().getInstance(ILogRepository.class); }
    private IMeasurementRepository getMeasurementRepository() { return getInjector().getInstance(IMeasurementRepository.class); }
    private ITagRepository getTagRepository() { return getInjector().getInstance(ITagRepository.class); }
    private IEventRepository getEventRepository() { return getInjector().getInstance(IEventRepository.class); }
    
    private View _view;
    private List<Log> _logs;
    private List<Measurement> _measurements;
    private List<Tag> _tags;
    private List<Event> _events;
    private SimpleDateFormat _format;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	setLoading(true);
	
	_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        _view = inflater.inflate(R.layout.fragment_dashboard, container, false);
	
	getLogs();
	getMeasurements();
	getEvents();
	getTags();
	
	return _view;
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
	
	LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	LinearLayout layout = (LinearLayout) _view.findViewById(R.id.dashboard_container);
	createDashboardModels(layout, inflater);
	
	setLoading(false);
    }
    
    private void createDashboardModels(LinearLayout layout, LayoutInflater inflater) {
	for (int i = 0; i < _logs.size(); i++) {
	    Log log = _logs.get(i);
	    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.dashboard_item, null);
	    ((TextView) view.findViewById(R.id.listview_dashboard_title)).setText(log.Name);
	    ((TextView) view.findViewById(R.id.listview_dashboard_date)).setText(_format.format(getLatestEvent().Date));
	    createMeasurements(view, getMeasurementsForLog(log), inflater);
	    createTags(view, getTagsForLog(log), inflater);
	    if (i > 0) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 20, 0, 0);
		view.setLayoutParams(params);
	    }
	    layout.addView(view);
	}
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
	Date date = new Date(1, 1, 1);
	for (Tag tag : _tags)
	    if (tag.LogId.equals(log.Id) && date.before(tag.Date))
		date = tag.Date;
	
	for (Tag tag : _tags)
	    if (tag.LogId.equals(log.Id) && tag.Date.equals(date))
		result.add(tag);
	Collections.sort(result, new TagNameComparator());
	return result;
    }
    
    private void createMeasurements(View view, List<Measurement> measurements, LayoutInflater inflater) {
	LinearLayout measurementsLayout = (LinearLayout) view.findViewById(R.id.linear_dashboard_measurements);
	measurementsLayout.removeAllViews();
	
	for (Measurement measurement : measurements) {
	    LinearLayout measurementView = (LinearLayout) inflater.inflate(R.layout.event_data, null);
	    ((TextView) measurementView.findViewById(R.id.event_data_title)).setText(measurement.toString());
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    params.setMargins(0, 8, 0, 0);
	    measurementView.setLayoutParams(params);
	    measurementsLayout.addView(measurementView);
	}
    }
    
    private void createTags(View view, List<Tag> tags, LayoutInflater inflater) {
	LinearLayout tagsLayout = (LinearLayout) view.findViewById(R.id.linear_dashboard_tags);
	tagsLayout.removeAllViews();
	
	for (Tag tag : tags) {
	    LinearLayout tagView = (LinearLayout) inflater.inflate(R.layout.event_data, null);
	    ((TextView) tagView.findViewById(R.id.event_data_title)).setText(tag.toString());
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    params.setMargins(0, 8, 0, 0);
	    tagView.setLayoutParams(params);;
	    tagsLayout.addView(tagView);
	}
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
