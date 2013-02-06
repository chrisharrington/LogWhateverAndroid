package com.logwhatever.repositories;

import com.logwhatever.models.Session;
import com.logwhatever.service.IExecutor;
import java.util.List;

public interface IRepository<TModel> {
    String getCollection();
    Class<TModel> getType();
    void all(Session session, IExecutor<List<TModel>> callback);
}
