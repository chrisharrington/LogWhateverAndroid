package com.logwhatever.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.logwhatever.repositories.EventRepository;
import com.logwhatever.repositories.IEventRepository;
import com.logwhatever.repositories.ILogRepository;
import com.logwhatever.repositories.IMeasurementRepository;
import com.logwhatever.repositories.ITagRepository;
import com.logwhatever.repositories.LogRepository;
import com.logwhatever.repositories.MeasurementRepository;
import com.logwhatever.repositories.TagRepository;
import com.logwhatever.service.Configuration;
import com.logwhatever.service.IConfiguration;
import com.logwhatever.web.HttpRequestor;
import com.logwhatever.web.IHttpRequestor;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
	bind(IHttpRequestor.class).to(HttpRequestor.class);
	bind(IConfiguration.class).to(Configuration.class);
	bind(ILogRepository.class).to(LogRepository.class);
	bind(IMeasurementRepository.class).to(MeasurementRepository.class);
	bind(ITagRepository.class).to(TagRepository.class);
	bind(IEventRepository.class).to(EventRepository.class);

	GsonBuilder builder = new GsonBuilder();
	builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	bind(Gson.class).toInstance(builder.create());
    }
    
}
