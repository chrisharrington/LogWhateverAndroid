package com.logwhatever.fragments;

import android.app.Fragment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.google.inject.Injector;
import com.logwhatever.R;
import com.logwhatever.activities.FragmentActivity;
import com.logwhatever.service.LogWhateverApplication;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class BaseFragment extends Fragment {

    protected SimpleDateFormat _format;
    
    protected BaseFragment() {
	_format = new SimpleDateFormat("yyyy-MM-dd");
    }
    
    protected LogWhateverApplication getLogApplication() {
	return (LogWhateverApplication) getActivity().getApplication();
    }
    
    protected Injector getInjector() {
	return getLogApplication().getInjector();
    }
    
    protected void setLoading(boolean isLoading)
    {
	((RelativeLayout) getActivity().findViewById(R.id.loader)).setBackgroundColor(isLoading ? 0xAAAAAAAA : 0x00AAAAAA);
	((ProgressBar) getActivity().findViewById(R.id.loader_image)).setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
    
    protected void switchToFragment(BaseFragment fragment, boolean addToBackStack) {
	((FragmentActivity) getActivity()).switchToFragment(fragment, addToBackStack);
    }
    
    protected void returnToPreviousFragment() {
	((FragmentActivity) getActivity()).returnToPreviousFragment();
    }
    
    protected void showKeyboard() {
	getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    
    protected void setData(String key, Object value) {
	((FragmentActivity) getActivity()).setData(key, value);
    }
    
    protected Object getData(String key, boolean remove) {
	return ((FragmentActivity) getActivity()).getData(key, remove);
    }
    
    protected void loadFloater(View view) {
	LinearLayout floater = (LinearLayout) getActivity().getWindow().getDecorView().findViewById(R.id.floater);
	floater.setBackgroundColor(0xAAAAAAAA);
	RelativeLayout inner = (RelativeLayout) floater.findViewById(R.id.inner_floater);
	inner.removeAllViews();
	inner.addView(view);
	floater.setVisibility(View.VISIBLE);
    }
}
