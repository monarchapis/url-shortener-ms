package com.monarchapis.api.env;

import java.io.IOException;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.spring31.properties.EncryptablePropertiesPropertySource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class EncryptedPropertiesPropertySourceLoader implements PropertySourceLoader, PriorityOrdered {
	private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

	public EncryptedPropertiesPropertySourceLoader() {
		encryptor.setPassword(System.getenv("ENC_PWD"));
		encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
	}

	@Override
	public String[] getFileExtensions() {
		return new String[] { "properties", "xml" };
	}

	@Override
	public PropertySource<?> load(final String name, final Resource resource, final String profile) throws IOException {
		if (profile == null) {
			// load the properties
			final Properties props = PropertiesLoaderUtils.loadProperties(resource);

			if (!props.isEmpty()) {
				// create the encryptable properties property source
				return new EncryptablePropertiesPropertySource(name, props, this.encryptor);
			}
		}

		return null;
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}
}