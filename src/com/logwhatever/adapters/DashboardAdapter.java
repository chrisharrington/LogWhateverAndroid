package com.logwhatever.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.logwhatever.R;
import com.logwhatever.models.Dashboard;
import com.logwhatever.models.EventData;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
	View view = convertView;
	if (view == null) {
	    LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    view = vi.inflate(R.layout.listview_dashboard, null);
	}
	
	Dashboard dashboard = _items.get(position);
	if (dashboard == null)
	    return view;

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	((TextView) view.findViewById(R.id.listview_dashboard_title)).setText(dashboard.Name.toUpperCase());
	((TextView) view.findViewById(R.id.listview_dashboard_date)).setText(format.format(dashboard.Date));
	
	ListView measurements = (ListView) view.findViewById(R.id.listview_dashboard_measurements);
	EventData[] measurementData = Arrays.copyOf(dashboard.Measurements.toArray(), dashboard.Measurements.size(), EventData[].class);
	measurements.setAdapter(new EventDataAdapter(_context, R.layout.event_data, measurementData));
	
	ListView tags = (ListView) view.findViewById(R.id.listview_dashboard_tags);
	EventData[] tagData = Arrays.copyOf(dashboard.Tags.toArray(), dashboard.Tags.size(), EventData[].class);
	tags.setAdapter(new EventDataAdapter(_context, R.layout.event_data, tagData));
	
	return view;
    }
}
