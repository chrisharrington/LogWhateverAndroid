package com.logwhatever.repositories;

import android.os.AsyncTask;
import com.google.inject.Inject;
import com.logwhatever.models.Session;
import com.logwhatever.service.IConfiguration;
import com.logwhatever.service.IExecutor;
import com.logwhatever.web.IHttpRequestor;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.UUID;

public abstract class BaseRepository<TModel> implements IRepository<TModel> {
    @Inject IHttpRequestor HttpRequestor;
    @Inject IConfiguration Configuration;
    
    public abstract String getCollection();
    public abstract Class<TModel> getType();
    
    public void all(Session session, IExecutor<List<TModel>> callback) {
	new GetAll(getCollection(), getType(), session, callback).execute();
    }
    
    private class GetAll extends AsyncTask<Void, Void, List<TModel>> {

	private String _collection;
	private Class<TModel> _type;
	private Session _session;
	private IExecutor<List<TModel>> _callback;
	private Throwable _exception;
	
	public GetAll(String collection, Class<TModel> type, Session session, IExecutor<List<TModel>> callback) {
	    _collection = collection;
	    _type = type;
	    _session = session;
	    _callback = callback;
	}
	
	@Override
	protected List<TModel> doInBackground(Void... parameters) {
	    try {
		return HttpRequestor.getList(Configuration.getServiceLocation() + _collection, _type, new SimpleEntry("auth", _session.Id), new SimpleEntry("userId", _session.UserId));
	    } catch (Exception ex) {
		_exception = ex;
		return null;
	    }
	}
	
	@Override
	protected void onPostExecute(List<TModel> results) {
	    if (_exception == null)
		_callback.execute(results);
	    else
		_callback.error(_exception);
	}
    }
}
