package com.monarchapis.api.urlshortener.v1.model;

import org.hibernate.validator.constraints.NotBlank;

public class ShortenRequest {
	@NotBlank
	private String longUrl;

	private String slug;

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
}
