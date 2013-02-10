package com.logwhatever.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import com.logwhatever.R;
import com.logwhatever.fragments.BaseFragment;
import com.logwhatever.fragments.LogFragment;
import java.util.HashMap;
import java.util.Map;

public class FragmentActivity extends BaseActivity {

    private FragmentManager _fragmentManager;
    private Map<String, Object> _data;
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	FragmentTransaction transaction = _fragmentManager.beginTransaction();
	switch (item.getItemId()) {
	    case R.id.menu_log:
		transaction.add(R.id.fragment_container, new LogFragment());
		break;
	    case R.id.menu_search:
	    default:
		return super.onOptionsItemSelected(item);
	}
	
	transaction.commit();
	return true;
    }
    
    public void switchToFragment(BaseFragment fragment, boolean addToBackStack) {
	FragmentTransaction transaction = _fragmentManager.beginTransaction();
	if (addToBackStack)
	    transaction.addToBackStack(null);
	transaction.replace(R.id.fragment_container, fragment);
	transaction.commit();
    }
    
    public void returnToPreviousFragment() {
	getFragmentManager().popBackStackImmediate();
    }
    
    public void setData(String key, Object value) {
	if (_data == null)
	    _data = new HashMap<String, Object>();
	_data.put(key, value);
    }
    
    public Object getData(String key, boolean remove) {
	if (_data == null || !_data.containsKey(key))
	    return null;
	
	Object data = _data.get(key);
	if (remove)
	    _data.remove(key);
	return data;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.activity);
	
	_fragmentManager = getFragmentManager();
	FragmentTransaction transaction = _fragmentManager.beginTransaction();
	//transaction.add(R.id.fragment_container, new DashboardFragment());
	transaction.add(R.id.fragment_container, new LogFragment());
	transaction.commit();
    }
    
}
