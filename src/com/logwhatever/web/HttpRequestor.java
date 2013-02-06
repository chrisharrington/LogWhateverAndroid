package com.logwhatever.web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
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

public class HttpRequestor implements IHttpRequestor {

    private final Gson _gson;
    private final JsonParser _parser;
    
    @Inject
    public HttpRequestor(Gson gson, JsonParser parser) {
	_gson = gson;
	_parser = parser;
    }
    
    public <TModel> TModel get(String url, Class<TModel> resultType, SimpleEntry... parameters) {
	try {
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
	} catch (Exception ex) {
	    return null;
	}
    }
    
    public <TModel> List<TModel> getList(String url, Class<TModel> resultType, SimpleEntry... parameters) {
	try {
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
	} catch (Exception ex) {
	    return null;
	}
    }
    
    public void post(String url, SimpleEntry[] parameters) {
	throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private String createUrlParameters(SimpleEntry[] parameters) {
	String result = "";
	if (parameters.length == 0)
	    return result;
	for (int i = 0; i < parameters.length; i++)
	    result += "&" + parameters[i].getKey() + "=" + URLEncoder.encode(parameters[i].getValue().toString());
	return "?" + result.substring(1);
    }
}
