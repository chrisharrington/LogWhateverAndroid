package com.logwhatever.service;

public interface IExecutor<TParameter> {
    void execute(TParameter parameter);
}
