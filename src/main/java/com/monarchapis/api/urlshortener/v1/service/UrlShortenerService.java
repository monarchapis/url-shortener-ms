package com.monarchapis.api.urlshortener.v1.service;

import java.util.List;

import com.google.common.base.Optional;
import com.monarchapis.api.urlshortener.v1.model.ShortenedUrl;

public interface UrlShortenerService {
	ShortenedUrl shorten(String longUrl);

	ShortenedUrl shorten(String longUrl, String slug);

	Optional<ShortenedUrl> expand(String slug);

	void signalVisit(String slug);

	long getVisits(String slug);

	List<ShortenedUrl> urlsByUserId(String userId);
}
