package com.logwhatever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.logwhatever.R;
import com.logwhatever.models.Measurement;

public class AddMeasurementFragment extends BaseFragment {

    private View _view;
    private ViewHolder _holder;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	if (_view == null)
	    _view = createView(inflater, container);
	
	_holder = (ViewHolder) _view.getTag();
	hookupEvents(_holder);
	_holder.Name.requestFocus();
	showKeyboard();
	
	return _view;
    }
    
    private void hookupEvents(ViewHolder holder) {
	holder.Cancel.setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		returnToPreviousFragment();
	    }
	});
	
	holder.Add.setOnClickListener(new OnClickListener() {
	    public void onClick(View arg0) {
		Measurement measurement = new Measurement();
		measurement.Name = _holder.Name.getText().toString();
		measurement.Quantity = Float.parseFloat(_holder.Quantity.getText().toString());
		measurement.Unit = _holder.Units.getText().toString();
		setData("added-measurement", measurement);
		returnToPreviousFragment();
	    }
	});
    }
    
    private View createView(LayoutInflater inflater, ViewGroup container) {
//	View view = inflater.inflate(R.layout.fragment_add_measurement, container, false);
//	view.setTag(new ViewHolder(view));
//	return view;
	return null;
    }
    
    private class ViewHolder {
	public EditText Name;
	public EditText Quantity;
	public EditText Units;
	public Button Add;
	public Button Cancel;
	
	public ViewHolder(View view) {
	    Name = (EditText) view.findViewById(R.id.add_measurement_name);
	    Quantity = (EditText) view.findViewById(R.id.add_measurement_quantity);
	    Units  = (EditText) view.findViewById(R.id.add_measurement_units);
	    Add = (Button) view.findViewById(R.id.add_measurement_add);
	    Cancel = (Button) view.findViewById(R.id.add_measurement_cancel);
	}
    }
}
