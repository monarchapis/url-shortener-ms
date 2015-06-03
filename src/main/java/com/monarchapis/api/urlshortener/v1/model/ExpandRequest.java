package com.monarchapis.api.urlshortener.v1.model;

import org.hibernate.validator.constraints.NotBlank;

public class ExpandRequest {
	@NotBlank
	private String slug;

	private boolean visit;

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public boolean isVisit() {
		return visit;
	}

	public void setVisit(boolean visit) {
		this.visit = visit;
	}
}
