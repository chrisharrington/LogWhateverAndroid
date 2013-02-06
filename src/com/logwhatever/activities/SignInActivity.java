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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.logwhatever.R;
import com.logwhatever.models.Session;
import com.logwhatever.service.IConfiguration;
import com.logwhatever.web.IHttpRequestor;
import java.util.AbstractMap;

public class SignInActivity extends BaseActivity {

    protected IHttpRequestor getHttpRequestor() { return getInjector().getInstance(IHttpRequestor.class); }
    protected IConfiguration getConfiguration() { return getInjector().getInstance(IConfiguration.class); }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	
        setContentView(R.layout.activity_sign_in);

	this.getActionBar().hide();
	
	EditText email = (EditText) findViewById(R.id.sign_in_email_address);
	email.setText(getOwnerEmailAddress());
	email.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        hookupHandlers();
    }

    protected void onSignInSuccessful(Session session) {
	hideError();
	getLogApplication().setSession(session);
	
	startActivity(new Intent(this, DashboardActivity.class));
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
            String email = ((EditText) findViewById(R.id.sign_in_email_address)).getText().toString();
            String password = ((EditText) findViewById(R.id.sign_in_password)).getText().toString();

            validate(email, password);
	    setLoading(true);
	    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(findViewById(R.id.sign_in_password).getWindowToken(), 0);
	    
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

	@Override
	protected Session doInBackground(String... strings) {
	    AbstractMap.SimpleEntry[] parameters = new AbstractMap.SimpleEntry[2];
	    parameters[0] = new AbstractMap.SimpleEntry("emailAddress", strings[0]);
	    parameters[1] = new AbstractMap.SimpleEntry("password", strings[1]);
	    return getHttpRequestor().get(getConfiguration().getServiceLocation() + "sessions/sign-in", Session.class, parameters);
	}

	@Override
	protected void onPostExecute(Session session) {
	    if (session == null) {
		showError("Invalid credentials.");
		setLoading(false);
	    }
	    else
		onSignInSuccessful(session);
	}
    }
}
