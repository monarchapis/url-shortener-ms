package com.monarchapis.api.urlshortener.v1;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.monarchapis.api.urlshortener.v1.resource.UrlResource;
import com.monarchapis.driver.jaxrs.ConfigConstants;

@Component
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		packages(ConfigConstants.JERSEY_V2_PROVIDER_PACKAGES);
		register(UrlResource.class);
	}
}
