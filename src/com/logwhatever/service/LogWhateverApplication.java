package com.logwhatever.service;

import android.app.Application;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.logwhatever.models.Session;
import com.logwhatever.modules.Module;

public class LogWhateverApplication extends Application {
    
    private Injector _injector;
    private Session _session;
    
    public Injector getInjector() { return _injector; }
    public Session getSession() { return _session; }
    public void setSession(Session value) { _session = value; }
    
    @Override
    public void onCreate() {
	_injector = Guice.createInjector(new Module());
    }
    
}
