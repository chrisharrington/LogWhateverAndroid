package com.logwhatever.service;

import com.logwhatever.models.Session;

public interface ICachePrimer {
    void prime(Session session, IExecutor<Void> callback);
}
