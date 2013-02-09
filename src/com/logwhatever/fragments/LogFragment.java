package com.logwhatever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.logwhatever.R;
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
    }
    
    private void showAddMeasurement() {
	loadFloater(_inflater.inflate(R.layout.floater_add_measurement, _container, false));
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
