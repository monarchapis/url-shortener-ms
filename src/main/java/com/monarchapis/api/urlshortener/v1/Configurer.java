package com.monarchapis.api.urlshortener.v1;

import java.io.IOException;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.monarchapis.api.urlshortener.v1.service.UrlShortenerService;
import com.monarchapis.api.urlshortener.v1.service.mongodb.MongoDBUrlShortenerService;
import com.monarchapis.driver.configuration.ConfigurationBundle;
import com.monarchapis.driver.configuration.ResourceConfigurationBundle;
import com.monarchapis.driver.spring.MonarchConfigurer;

@Configuration
@Import(MonarchConfigurer.class)
public class Configurer {
	@Value("${mongodb.host:localhost}")
	private String mongodbHost;

	@Value("${mongodb.port:27017}")
	private int mongodbPort;

	@Value("${mongodb.db:urlShortener}")
	private String mongodbDatabase;

	@Bean
	public ConfigurationBundle configurationBundle() throws IOException {
		return new ResourceConfigurationBundle( //
				"com/monarchapis/driver/Errors", //
				"com/monarchapis/api/urlshortener/Errors");
	}

	@Bean
	public UrlShortenerService urlShortenerService() throws UnknownHostException {
		return new MongoDBUrlShortenerService(mongodbHost, mongodbPort, mongodbDatabase);
	}
}
