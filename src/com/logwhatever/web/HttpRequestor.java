package com.logwhatever.web;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.logwhatever.service.IExecutor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpRequestor implements IHttpRequestor {

    private final Gson _gson;
    private final JsonParser _parser;
    
    @Inject
    public HttpRequestor(Gson gson, JsonParser parser) {
	_gson = gson;
	_parser = parser;
    }
    
    public <TModel> TModel get(String url, Class<TModel> resultType, SimpleEntry... parameters) throws Exception {
	URL location  = new URL(url + createUrlParameters(parameters));
	URLConnection connection = location.openConnection();

	String line;
	StringBuilder builder = new StringBuilder();
	InputStream stream = connection.getInputStream();
	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	while((line = reader.readLine()) != null) {
	    builder.append(line);
	}

	return _gson.fromJson(builder.toString(), resultType);
    }
    
    public <TModel> List<TModel> getList(String url, Class<TModel> resultType, SimpleEntry... parameters) throws Exception {
	URL location  = new URL(url + createUrlParameters(parameters));
	URLConnection connection = location.openConnection();

	String line;
	StringBuilder builder = new StringBuilder();
	InputStream stream = connection.getInputStream();
	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	while((line = reader.readLine()) != null) {
	    builder.append(line);
	}

	JsonElement elements = new JsonParser().parse(builder.toString());
	JsonArray array = elements.getAsJsonArray();
	Iterator iterator = (Iterator) array.iterator();
	List<TModel> list = new ArrayList<TModel>();

	while(iterator.hasNext()){
	    JsonElement element = (JsonElement) iterator.next();
	    list.add(_gson.fromJson(element, resultType));
	}

	return list;
    }
    
    public void post(String url, IExecutor<Void> callback, SimpleEntry... parameters) throws Exception {
	new Post(url, callback).execute(parameters);
    }
    
    private String createUrlParameters(SimpleEntry[] parameters) {
	String result = "";
	if (parameters.length == 0)
	    return result;
	for (int i = 0; i < parameters.length; i++)
	    result += "&" + parameters[i].getKey() + "=" + URLEncoder.encode(parameters[i].getValue().toString());
	return "?" + result.substring(1);
    }
    
    private class Post extends AsyncTask<SimpleEntry, Void, Integer> {

	private String _url;
	private IExecutor<Void> _callback;
	
	public Post(String url, IExecutor<Void> callback) {
	    _url = url;
	    _callback = callback;
	}
	
	@Override
	protected Integer doInBackground(SimpleEntry... parameters) {
	    try {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(_url);

		List<NameValuePair> args = new ArrayList<NameValuePair>();
		for (SimpleEntry parameter : parameters)
		    args.add(new BasicNameValuePair(parameter.getKey().toString(), parameter.getValue().toString()));
		post.setEntity(new UrlEncodedFormEntity(args));

		return client.execute(post).getStatusLine().getStatusCode();
	    } catch (Exception ex) {
		return 0;
	    }
	}
	
	@Override
	protected void onPostExecute(Integer code) {
	    if (code == 200)
		_callback.success(null);
	    else
		_callback.error(null);
	}
	
    }
}
