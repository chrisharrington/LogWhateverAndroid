package com.logwhatever.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryCollectionCache implements ICollectionCache {

    private final Map<Class<?>, List> _dictionary;
    
    public MemoryCollectionCache() {
	_dictionary = new HashMap<Class<?>, List>();
    }
    
    public <TModel> List<TModel> Retrieve(Class<TModel> type) {
	return _dictionary.get(type);
    }

    public <TModel> void store(List list, Class<TModel> type) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public <TModel> List<TModel> retrieve(Class<TModel> type) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    public <TModel> boolean containsKey(Class<TModel> type) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

}
