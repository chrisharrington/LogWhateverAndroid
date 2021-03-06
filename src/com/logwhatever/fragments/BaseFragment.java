package com.logwhatever.fragments;

import android.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.google.inject.Injector;
import com.logwhatever.R;
import com.logwhatever.activities.BaseActivity;
import com.logwhatever.activities.FragmentActivity;
import com.logwhatever.models.Session;
import com.logwhatever.repositories.IEventRepository;
import com.logwhatever.repositories.ILogRepository;
import com.logwhatever.repositories.IMeasurementRepository;
import com.logwhatever.repositories.ITagRepository;
import com.logwhatever.service.LogWhateverApplication;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class BaseFragment extends Fragment {

    protected SimpleDateFormat _format;
    
    protected ILogRepository getLogRepository() { return getInjector().getInstance(ILogRepository.class); }
    protected IEventRepository getEventRepository() { return getInjector().getInstance(IEventRepository.class); }
    protected IMeasurementRepository getMeasurementRepository() { return getInjector().getInstance(IMeasurementRepository.class); }
    protected ITagRepository getTagRepository() { return getInjector().getInstance(ITagRepository.class); }
    
    protected BaseFragment() {
	_format = new SimpleDateFormat("yyyy-MM-dd");
	
	setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	menu.clear();
	inflater.inflate(R.menu.menu, menu);
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
    
    protected void setData(String key, Object value) {
	((FragmentActivity) getActivity()).setData(key, value);
    }
    
    protected Object getData(String key, boolean remove) {
	return ((FragmentActivity) getActivity()).getData(key, remove);
    }
    
    protected View loadFloater(View view) {
	LinearLayout floater = (LinearLayout) getActivity().getWindow().getDecorView().findViewById(R.id.floater);
	floater.setBackgroundColor(0xAAAAAAAA);
	RelativeLayout inner = (RelativeLayout) floater.findViewById(R.id.inner_floater);
	inner.removeAllViews();
	inner.addView(view);
	floater.setVisibility(View.VISIBLE);
	return floater;
    }
    
    protected void hideFloater() {
	getActivity().getWindow().getDecorView().findViewById(R.id.floater).setVisibility(View.GONE);
    }
    
    protected View getFloater() {
	return getActivity().getWindow().getDecorView().findViewById(R.id.floater);
    }
    
    protected void hideKeyboard(View view) {
	((BaseActivity) getActivity()).hideKeyboard(view);
    }
    
    protected void showKeyboard(View view) {
	((BaseActivity) getActivity()).showKeyboard(view);
    }
    
    protected void showError(String error) {
	((BaseActivity) getActivity()).showError(error);
    }
    
    protected void hideError() {
	((BaseActivity) getActivity()).hideError();
    }
    
    protected Session getSession() {
	Session session = new Session();
	session.EmailAddress = "chrisharrington99@gmail.com";
	session.Id = UUID.fromString("1E9F9CBE-F05D-4ABC-8BB6-55FF05186F2F");
	session.Name = "Chris Harrington";
	session.UserId = UUID.fromString("69A8301C-02CB-4E3B-AEC2-18B83C781485");
	return session;
	//return ((LogWhateverApplication) getActivity().getApplication()).getSession();
    }
}
