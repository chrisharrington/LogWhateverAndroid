package com.logwhatever.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.logwhatever.R;
import com.logwhatever.models.EventData;
import com.logwhatever.models.Measurement;
import java.util.List;

public class EventDataAdapter extends ArrayAdapter<EventData> {
    
    private EventData[] _items;
    private Context _context;
    
    public EventDataAdapter(Context context, int textViewResourceId, EventData... items) {
	super(context, textViewResourceId, items);
	_items = items;
	_context = context;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View view = convertView;
	if (view == null) {
	    LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    view = vi.inflate(R.layout.event_data, null);
	}
	
	Object data  = _items[position];
	if (data == null)
	    return view;

	TextView title = (TextView) view.findViewById(R.id.event_data_title);
	title.setText(data.toString());
	
	return view;
    }
}
