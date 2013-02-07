package com.logwhatever.activities;
import android.app.Activity;
import android.opengl.Visibility;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.inject.Injector;
import com.logwhatever.R;
import com.logwhatever.service.LogWhateverApplication;

public class BaseActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu, menu);
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	    case R.id.menu_log:
		return true;
	    case R.id.menu_search:
		return true;
	    default:
		return super.onOptionsItemSelected(item);
	}
    }
    
    protected void showError(String error) {
	TextView panel = (TextView) findViewById(R.id.error);
	panel.setText(error);
	panel.setVisibility(View.VISIBLE);
    }
    
    protected void hideError() {
	((TextView) findViewById(R.id.error)).setVisibility(View.GONE);
    }
	
    protected void setLoading(boolean isLoading)
    {
	Object blah = findViewById(R.id.loader);
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
