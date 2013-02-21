package com.logwhatever.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.logwhatever.service.IExecutor;
import java.util.Date;
import java.util.List;

public class LogFragment extends BaseFragment {
    
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
	showKeyboard(_holder.Name);
	
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
	_holder.Name.addTextChangedListener(new TextWatcher() {
	    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { getPotentialLog(); }
	    public void afterTextChanged(Editable arg0) {}
	});
    }
    
    private void getPotentialLog() {
	try {
	    String name = _holder.Name.getText().toString();
	    if (name.equals(""))
		return;
	    
	    getLog(name, new IExecutor<Log>() {
		public void success(Log log) {
		    if (log == null)
			clearLog();
		    else
			loadLog(log);
		}

		public void error(Throwable error) {
		    showError("An error has occurred while retrieving your log by name.");
		}
	    });
	} catch (Exception ex) {
	    showError(ex.getMessage());
	}
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
		
		LinearLayout tagContainer = (LinearLayout) _view.findViewById(R.id.log_tags);
		
		View tagView = _inflater.inflate(R.layout.event_data, _container, false);
		((TextView) tagView.findViewById(R.id.event_data_title)).setText(tag.toString());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (tagContainer.getChildCount() > 0)
		    params.bottomMargin = 10;
		tagView.setLayoutParams(params);
		
		tagContainer.addView(tagView, 0);
		tagContainer.setVisibility(View.VISIBLE);
		
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
		
		LinearLayout measurementContainer = (LinearLayout) _view.findViewById(R.id.log_measurements);
		
		View measurementView = _inflater.inflate(R.layout.log_add_measurement, _container, false);
		((TextView) measurementView.findViewById(R.id.log_add_measurement_units)).setText(measurement.Unit);
		TextView quantity = (TextView) measurementView.findViewById(R.id.log_add_measurement_quantity);
		quantity.setText(Float.toString(measurement.Quantity));
		if (!measurement.Name.equals(""))
		    quantity.setHint(measurement.Name);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if (measurementContainer.getChildCount() > 0)
		    params.bottomMargin = 10;
		measurementView.setLayoutParams(params);
		
		
		measurementContainer.addView(measurementView, 0);
		measurementContainer.setVisibility(View.VISIBLE);
		
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
	
    }
    
    private void getLog(String name, final IExecutor<Log> callback) throws Exception {
	getLogRepository().name(name, getSession(), new IExecutor<Log>() {
	    public void success(Log log) {
		callback.success(log);
	    }

	    public void error(Throwable error) {
		showError("An error has occurred while retrieving your log by name.");
	    }
	});
    }
    
    private void loadLog(Log log) {
	loadMeasurements(log);
    }
    
    private void clearLog() {
	_holder.Measurements.setVisibility(View.GONE);
	_holder.Tags.setVisibility(View.GONE);
    }
    
    private void loadMeasurements(Log log) {
	getMeasurementRepository().uniqueForLog(getSession(), log, new IExecutor<List<Measurement>>() {
	    public void success(List<Measurement> measurements) {
		LinearLayout container = (LinearLayout) _container.findViewById(R.id.log_measurements);
		for (Measurement measurement : measurements) {
		    LinearLayout measurementView = (LinearLayout) _inflater.inflate(R.layout.log_add_measurement, _container, false);
		    if (measurement.Unit != null && !measurement.Unit.equals("")) {
			TextView unit = (TextView) measurementView.findViewById(R.id.log_add_measurement_units);
			unit.setText(measurement.Unit);
			unit.setVisibility(View.VISIBLE);
		    }
		    if (measurement.Name != null && !measurement.Name.equals(""))
			((EditText) measurementView.findViewById(R.id.log_add_measurement_quantity)).setHint(measurement.Name);
		    container.addView(measurementView);
		}

		if (!measurements.isEmpty())
		    _holder.Measurements.setVisibility(View.VISIBLE);
 	    }

	    public void error(Throwable error) {
		showError("An error has occurred while retrieving measurements for the selected log.");
	    }
	});
    }
    
    private class ViewHolder {
	public EditText Name;
	public EditText Date;
	public EditText Time;
	public LinearLayout Measurements;
	public LinearLayout Tags;
	
	public ViewHolder(View view) {
	    Name = (EditText) view.findViewById(R.id.log_name);
	    Date = (EditText) view.findViewById(R.id.log_date);
	    Time = (EditText) view.findViewById(R.id.log_time);
	    Measurements = (LinearLayout) view.findViewById(R.id.log_measurements);
	    Tags  = (LinearLayout) view.findViewById(R.id.log_tags);
	}
    }
}
