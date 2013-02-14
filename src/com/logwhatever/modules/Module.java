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
import com.logwhatever.service.CachePrimer;
import com.logwhatever.service.Configuration;
import com.logwhatever.service.ICachePrimer;
import com.logwhatever.service.ICollectionCache;
import com.logwhatever.service.IConfiguration;
import com.logwhatever.service.MemoryCollectionCache;
import com.logwhatever.web.HttpRequestor;
import com.logwhatever.web.IHttpRequestor;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
	bind(IHttpRequestor.class).to(HttpRequestor.class);
	bind(IConfiguration.class).to(Configuration.class);
	bind(ICollectionCache.class).toInstance(new MemoryCollectionCache());
	bind(ICachePrimer.class).to(CachePrimer.class);
	
	bindRepositories();

	GsonBuilder builder = new GsonBuilder();
	builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	bind(Gson.class).toInstance(builder.create());
    }
    
    private void bindRepositories() {
	bind(ILogRepository.class).toInstance(new LogRepository());
	bind(IMeasurementRepository.class).toInstance(new MeasurementRepository());
	bind(ITagRepository.class).toInstance(new TagRepository());
	bind(IEventRepository.class).toInstance(new EventRepository());
    }
}
