package com.logwhatever.repositories;

import android.os.AsyncTask;
import com.google.inject.Inject;
import com.logwhatever.models.Session;
import com.logwhatever.service.IConfiguration;
import com.logwhatever.service.IExecutor;
import com.logwhatever.web.IHttpRequestor;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<TModel> implements IRepository<TModel> {
    @Inject IHttpRequestor HttpRequestor;
    @Inject IConfiguration Configuration;
    
    private GetAll _getAll;
    
    public abstract String getCollection();
    public abstract Class<TModel> getType();
    
    public synchronized void all(Session session, IExecutor<List<TModel>> callback) {
	if (_getAll == null)
	    _getAll = new GetAll(getCollection(), getType());
	
	if (_getAll.isCompleted() && _getAll.getError() == null)
	    callback.execute(_getAll.getResults());
	else if (_getAll.isCompleted() && _getAll.getError() != null)
	    callback.error(_getAll.getError());
	else
	    _getAll.registerCallback(callback);
	
	if (!_getAll.hasBegunExecuting())
	    _getAll.execute(session, callback);
    }
    
    private class GetAll extends AsyncTask<Object, Void, List<TModel>> {

	private String _collection;
	private Class<TModel> _type;
	private Throwable _exception;
	private List<TModel> _results;
	private boolean _isCompleted;
	private boolean _hasBegunExecuting;
	private List<IExecutor<List<TModel>>> _callbacks;
	
	public boolean isCompleted() { return _isCompleted; }
	public boolean hasBegunExecuting() { return _hasBegunExecuting; }
	public Throwable getError() { return _exception; }
	
	public List<TModel> getResults() { return _results; }
	public void registerCallback(IExecutor<List<TModel>> callback) { _callbacks.add(callback); }
	
	public GetAll(String collection, Class<TModel> type) {
	    _collection = collection;
	    _type = type;
	    _callbacks = new ArrayList<IExecutor<List<TModel>>>();
	}
	
	@Override
	protected List<TModel> doInBackground(Object... parameters) {
	    _hasBegunExecuting = true;
	    try {
		registerCallback((IExecutor<List<TModel>>) parameters[1]);
		
		Session session = (Session) parameters[0];
		return HttpRequestor.getList(Configuration.getServiceLocation() + _collection, _type, new SimpleEntry("auth", session.Id), new SimpleEntry("userId", session.UserId));
	    } catch (Exception ex) {
		_exception = ex;
		return null;
	    }
	}
	
	@Override
	protected void onPostExecute(List<TModel> results) {
	    _isCompleted = true;
	    _results = results;
	    for (IExecutor<List<TModel>> callback : _callbacks) {
		if (_exception == null)
		    callback.execute(results);
		else
		    callback.error(_exception);
	    }
	}
    }
}
