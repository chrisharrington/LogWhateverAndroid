package com.logwhatever.service;

public interface IExecutor<TParameter> {
    void success(TParameter parameter);
    void error(Throwable error);
}
