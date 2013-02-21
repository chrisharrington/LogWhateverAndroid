package com.logwhatever.activities;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.inject.Injector;
import com.logwhatever.R;
import com.logwhatever.service.LogWhateverApplication;

public class BaseActivity extends Activity {

    public void hideKeyboard(View view) {
	InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    public void showKeyboard(View view) {
	view.requestFocus();
	getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE | LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    
    public void showError(String error) {
	TextView panel = (TextView) findViewById(R.id.error);
	panel.setText(error);
	panel.setVisibility(View.VISIBLE);
    }
    
    public void hideError() {
	((TextView) findViewById(R.id.error)).setVisibility(View.GONE);
    }
	
    protected void setLoading(boolean isLoading)
    {
	((RelativeLayout) findViewById(R.id.loader)).setBackgroundColor(isLoading ? 0xAAAAAAAA : 0x00AAAAAA);
	((ProgressBar) findViewById(R.id.loader_image)).setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
    
    protected LogWhateverApplication getLogApplication() {
	return (LogWhateverApplication) getApplication();
    }
    
    protected Injector getInjector() {
	return getLogApplication().getInjector();
    }
    
}
