package com.logwhatever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.logwhatever.R;
import com.logwhatever.models.Measurement;
import com.logwhatever.models.Tag;
import java.util.Date;

public class LogFragment extends BaseFragment {

    private View _view;
    private LayoutInflater _inflater;
    private ViewGroup _container;
    private BaseFragment _addMeasurementFragment;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	_inflater = inflater;
	_container = container;
	
	if (_view == null)
	    _view = createView(inflater, container);
	
	hookupEvents((ViewHolder) _view.getTag());
	
	return _view;
    }
    
    private View createView(LayoutInflater inflater, ViewGroup container) {
	View view = inflater.inflate(R.layout.fragment_log, container, false);
	ViewHolder holder = new ViewHolder(view);
	holder.Date.setText(_format.format(new Date()));
	holder.Time.setText("12:00 PM");
	view.setTag(holder);
	return view;
    }
    
    private void hookupEvents(ViewHolder holder) {
	holder.AddMeasurement.setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		showAddMeasurement();
	    }
	});
	
	holder.AddTag.setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		showAddTag();
	    }
	});
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
    
    private class ViewHolder {
	public EditText Name;
	public EditText Date;
	public EditText Time;
	public Button AddMeasurement;
	public Button AddTag;
	
	public ViewHolder(View view) {
	    Name = (EditText) view.findViewById(R.id.log_name);
	    Date = (EditText) view.findViewById(R.id.log_date);
	    Time = (EditText) view.findViewById(R.id.log_time);
	    AddMeasurement = (Button) view.findViewById(R.id.log_add_measurement);
	    AddTag  = (Button) view.findViewById(R.id.log_add_tag);
	}
    }
    
}
