package com.logwhatever.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.logwhatever.R;
import com.logwhatever.models.Log;
import com.logwhatever.models.Session;
import com.logwhatever.repositories.ILogRepository;
import com.logwhatever.service.ICachePrimer;
import com.logwhatever.service.IConfiguration;
import com.logwhatever.service.IExecutor;
import com.logwhatever.web.IHttpRequestor;
import java.util.AbstractMap;
import java.util.List;

public class SignInActivity extends BaseActivity {

    protected IHttpRequestor getHttpRequestor() { return getInjector().getInstance(IHttpRequestor.class); }
    protected IConfiguration getConfiguration() { return getInjector().getInstance(IConfiguration.class); }
    protected ILogRepository getLogRepository() { return getInjector().getInstance(ILogRepository.class); }
    protected ICachePrimer getCachePrimer() { return getInjector().getInstance(ICachePrimer.class); }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.activity_sign_in);

	this.getActionBar().hide();
	
	EditText email = (EditText) findViewById(R.id.sign_in_email_address);
	email.setText(getOwnerEmailAddress());
	email.requestFocus();
	showKeyboard(email);

        hookupHandlers();
    }

    private void onSignInSuccessful(Session session) {
	final Context context = this;
	hideError();
	getLogApplication().setSession(session);
	getCachePrimer().prime(session, new IExecutor<Void>() {
	    public void execute(Void parameter) { startActivity(new Intent(context, FragmentActivity.class)); }
	    public void error(Throwable error) { showError("An error has occurred while priming the cache."); }
	});
    }
    
    private String getOwnerEmailAddress() {
	AccountManager accountManager = AccountManager.get(this); 
	Account[] accounts = accountManager.getAccountsByType("com.google");
	return accounts.length > 0 ? accounts[0].name : "";
    }
    
    private void hookupHandlers() {
        Button signIn = (Button) findViewById(R.id.sign_in_submit);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        EditText password = (EditText) findViewById(R.id.sign_in_password);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	    @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    signIn();
                    return true;
                }

                return false;
            }
        });
    }

    private void signIn() {
	try {
	    EditText emailView = (EditText) findViewById(R.id.sign_in_email_address);
            String email = emailView.getText().toString();
            String password = ((EditText) findViewById(R.id.sign_in_password)).getText().toString();

            validate(email, password);
	    setLoading(true);
	    hideKeyboard(emailView);
	    
	    new SignInTask().execute(email, password);
        } catch (Exception ex) {
            showError(ex.getMessage());
	    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void validate(String email, String password) throws IllegalArgumentException {
        if (email.equals(""))
            throw new IllegalArgumentException("The email address is required.");
        if (password.equals(""))
            throw new IllegalArgumentException("The password is required.");
    }
    
    public class SignInTask extends AsyncTask<String, Void, Session> {

	private Throwable _error;
	
	@Override
	protected Session doInBackground(String... strings) {
	    try {
		AbstractMap.SimpleEntry[] parameters = new AbstractMap.SimpleEntry[2];
		parameters[0] = new AbstractMap.SimpleEntry("emailAddress", strings[0]);
		parameters[1] = new AbstractMap.SimpleEntry("password", strings[1]);
		return getHttpRequestor().get(getConfiguration().getServiceLocation() + "sessions/sign-in", Session.class, parameters);
	    } catch (Exception ex) {
		_error = ex;
		return null;
	    }
	}

	@Override
	protected void onPostExecute(Session session) {
	    if (_error != null) {
		showError("An error has occurred while signing you in.");
		setLoading(false);
	    }
	    if (session == null) {
		showError("Invalid credentials.");
		setLoading(false);
	    }
	    else
		onSignInSuccessful(session);
	}
    }
}
