package com.monarchapis.api.urlshortener.v1.model;

import org.joda.time.DateTime;

public class ShortenedUrl {
	private String slug;
	private String longUrl;
	private long visits;
	private String createdBy;
	private DateTime createdDate;

	public String getSlug() {
		return slug;
	}

	public void setSlug(String shortUrl) {
		this.slug = shortUrl;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public long getVisits() {
		return visits;
	}

	public void setVisits(long visits) {
		this.visits = visits;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdTime) {
		this.createdDate = createdTime;
	}
}
