package com.logwhatever.service;

import java.util.List;

public interface ICollectionCache {
    <TModel> void store(List list, Class<TModel> type);
    <TModel> List<TModel> retrieve(Class<TModel> type);
    <TModel> boolean containsKey(Class<TModel> type);
}
