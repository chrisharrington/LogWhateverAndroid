package com.logwhatever.web;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

public interface IHttpRequestor {
    void post(String url, SimpleEntry[] parameters);
    <TModel> TModel get(String url, Class<TModel> resultType, SimpleEntry... parameters);
    <TModel> List<TModel> getList(String url, Class<TModel> resultType, SimpleEntry... parameters);
}
