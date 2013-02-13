package com.logwhatever.web;

import com.logwhatever.service.IExecutor;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

public interface IHttpRequestor {
    void post(String url, IExecutor<Void> callback, SimpleEntry... parameters);
    <TModel> TModel get(String url, Class<TModel> resultType, SimpleEntry... parameters) throws Exception;
    <TModel> List<TModel> getList(String url, Class<TModel> resultType, SimpleEntry... parameters) throws Exception;
}
