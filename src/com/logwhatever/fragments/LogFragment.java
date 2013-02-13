package com.logwhatever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.logwhatever.R;
import com.logwhatever.models.Log;
import com.logwhatever.models.Measurement;
import com.logwhatever.models.Tag;
import com.logwhatever.repositories.ILogRepository;
import com.logwhatever.service.IExecutor;
import java.util.Date;

public class LogFragment extends BaseFragment {

    private ILogRepository getLogRepository() { return getInjector().getInstance(ILogRepository.class); }   
    
    private View _view;
    private LayoutInflater _inflater;
    private ViewGroup _container;
    private ViewHolder _holder;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	_inflater = inflater;
	_container = container;
	
	if (_view == null)
	    _view = createView(inflater, container);
	
	setHasOptionsMenu(true);
	hookupEvents();
	
	return _view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	menu.clear();
	inflater.inflate(R.menu.menu_log, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	    case R.id.menu_save_log:
		save();
		break;
	    case R.id.menu_log_add_measurement:
		showAddMeasurement();
		break;
	    case R.id.menu_log_add_tag:
		showAddTag();
		break;
	    default:
		return false;
	}
	
	return true;
    }
    
    private void hookupEvents() {
	
    }
    
    private View createView(LayoutInflater inflater, ViewGroup container) {
	View view = inflater.inflate(R.layout.fragment_log, container, false);
	_holder = new ViewHolder(view);
	_holder.Date.setText(_format.format(new Date()));
	_holder.Time.setText("12:00 PM");
	view.setTag(_holder);
	return view;
    }
    
    private void showAddTag() {
	final View view = _inflater.inflate(R.layout.floater_add_tag, _container, false);
	View floater = loadFloater(view);
	
	EditText name = (EditText) floater.findViewById(R.id.add_tag_name);
	name.requestFocus();
	showKeyboard(name);
	
	view.findViewById(R.id.add_tag_cancel).setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		hideFloater();
	    }
	});
	
	view.findViewById(R.id.add_tag_add).setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		Tag tag = readTagFromFloater();
		
		View tagView = _inflater.inflate(R.layout.event_data, _container, false);
		((TextView) tagView.findViewById(R.id.event_data_title)).setText(tag.toString());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.bottomMargin = 10;
		tagView.setLayoutParams(params);
		View container = _view.findViewById(R.id.log_tags);
		((LinearLayout) _view.findViewById(R.id.log_tags)).addView(tagView, 0);
		
		hideFloater();
		hideKeyboard(view);
	    }
	});
    }
    
    private Tag readTagFromFloater() {
	try {
	    Tag tag = new Tag();
	    tag.Name = ((EditText) getFloater().findViewById(R.id.add_tag_name)).getText().toString();
	    
	    if (tag.Name.equals(""))
		throw new Exception("The name is required.");
	    
	    return tag;
	} catch (Exception ex) {
	    TextView error = (TextView) getFloater().findViewById(R.id.add_tag_error);
	    error.setText(ex.getMessage());
	    error.setVisibility(View.VISIBLE);
	    return null;
	}
    }
    
    private void showAddMeasurement() {
	final View view = _inflater.inflate(R.layout.floater_add_measurement, _container, false);
	View floater = loadFloater(view);
	
	EditText name = (EditText) floater.findViewById(R.id.add_measurement_name);
	name.requestFocus();
	showKeyboard(name);
	
	view.findViewById(R.id.add_measurement_cancel).setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		hideFloater();
	    }
	});
	
	view.findViewById(R.id.add_measurement_add).setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		Measurement measurement = readMeasurementFromFloater();
		if (measurement == null)
		    return;
		
		View measurementView = _inflater.inflate(R.layout.event_data, _container, false);
		((TextView) measurementView.findViewById(R.id.event_data_title)).setText(measurement.toString());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.bottomMargin = 10;
		measurementView.setLayoutParams(params);
		View container = _view.findViewById(R.id.log_measurements);
		((LinearLayout) _view.findViewById(R.id.log_measurements)).addView(measurementView, 0);
		
		hideFloater();
		hideKeyboard(view);
	    }
	});
    }
    
    private Measurement readMeasurementFromFloater() {
	try {
	    View floater = getFloater();
	    String quantity = ((EditText) floater.findViewById(R.id.add_measurement_quantity)).getText().toString();
	    if (quantity.equals(""))
		throw new Exception("The quantity is required.");
	    
	    Measurement measurement = new Measurement();
	    measurement.Name = ((EditText) floater.findViewById(R.id.add_measurement_name)).getText().toString();
	    measurement.Quantity = Float.parseFloat(quantity);
	    measurement.Unit = ((EditText) floater.findViewById(R.id.add_measurement_units)).getText().toString();
	    return measurement;
	} catch (Exception ex) {
	    TextView error = (TextView) getFloater().findViewById(R.id.add_measurement_error);
	    error.setText(ex.getMessage());
	    error.setVisibility(View.VISIBLE);
	    return null;
	}
    }
    
    private void save() {
	try {
	    String name = _holder.Name.getText().toString();
	    if (name.equals(""))
		throw new Exception("The name is required.");
	    
	    setLoading(true);
	    
	    getLog(name, new IExecutor<Log>() {
		public void execute(Log log) {
		    if (log != null)
			loadLog(log);
		}

		public void error(Throwable error) {
		    showError("An error has occurred while retrieving your log by name.");
		    setLoading(false);
		}
	    });
	} catch (Exception ex) {
	    showError(ex.getMessage());
	    setLoading(false);
	}
    }
    
    private void getLog(String name, final IExecutor<Log> callback) throws Exception {
	getLogRepository().name(name, getSession(), new IExecutor<Log>() {
	    public void execute(Log log) {
		if (log != null)
		    callback.execute(log);
		//	log = new Log();
		//	log.Id = UUID.randomUUID();
		//	log.Name = name;
		//	log.UserId = getSession().UserId;
		//	getLogRepository().create(log);
		//	return log;
	    }

	    public void error(Throwable error) {
		showError("An error has occurred while retrieving your log by name.");
		setLoading(false);
	    }
	});
    }
    
    private void loadLog(Log log) {
	
    }
    
    private class ViewHolder {
	public EditText Name;
	public EditText Date;
	public EditText Time;
	
	public ViewHolder(View view) {
	    Name = (EditText) view.findViewById(R.id.log_name);
	    Date = (EditText) view.findViewById(R.id.log_date);
	    Time = (EditText) view.findViewById(R.id.log_time);	}
    }
}
