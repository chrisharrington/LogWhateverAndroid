package com.logwhatever.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.logwhatever.R;
import com.logwhatever.models.Dashboard;
import com.logwhatever.models.Measurement;
import com.logwhatever.models.Tag;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardAdapter extends ArrayAdapter<Dashboard> {
    
    private List<Dashboard> _items;
    private Context _context;
    
    public DashboardAdapter(Context context, int textViewResourceId, List<Dashboard> items) {
	super(context, textViewResourceId, items);
	_items = items;
	_context = context;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View view = convertView;
	if (view == null) {
	    view = inflater.inflate(R.layout.listview_dashboard, null);
	}
	
	Dashboard dashboard = _items.get(position);
	if (dashboard == null)
	    return view;

	if (dashboard.Name.toLowerCase() == "physio")
	    dashboard.Name = "physio";
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	((TextView) view.findViewById(R.id.listview_dashboard_title)).setText(dashboard.Name.toUpperCase());
	((TextView) view.findViewById(R.id.listview_dashboard_date)).setText(format.format(dashboard.Date));
	
	setMeasurements(view, dashboard, inflater);
	setTags(view, dashboard, inflater);
	
	return view;
    }

    private void setMeasurements(View view, Dashboard dashboard, LayoutInflater inflater) {
	LinearLayout measurements = (LinearLayout) view.findViewById(R.id.linear_dashboard_measurements);;
	measurements.removeAllViews();
	
	for (Measurement measurement : dashboard.Measurements) {
	    View measurementView = inflater.inflate(R.layout.event_data, null);
	    ((TextView) measurementView.findViewById(R.id.event_data_title)).setText(measurement.toString());
	    LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    params.setMargins(0, 8, 0, 0);
	    measurementView.setLayoutParams(params);
	    measurements.addView(measurementView);
	}
    }
    
    private void setTags(View view, Dashboard dashboard, LayoutInflater inflater) {
	LinearLayout tags = (LinearLayout) view.findViewById(R.id.linear_dashboard_tags);;
	tags.removeAllViews();
	
	for (Tag tag : dashboard.Tags) {
	    LinearLayout tagView = (LinearLayout) inflater.inflate(R.layout.event_data, null);
	    ((TextView) tagView.findViewById(R.id.event_data_title)).setText(tag.toString());
	    LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	    params.setMargins(0, 8, 0, 0);
	    tagView.setLayoutParams(params);
	    tags.addView(tagView);
	}
    }
}
