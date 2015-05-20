package com.monarchapis.api.urlshortener.v1.service.mongodb;

import static com.mongodb.client.model.Filters.*;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.Document;
import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.monarchapis.api.urlshortener.v1.model.ShortenedUrl;
import com.monarchapis.api.urlshortener.v1.service.UrlShortenerService;
import com.monarchapis.driver.exception.ConflictException;
import com.monarchapis.driver.model.Claims;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

public class MongoDBUrlShortenerService implements UrlShortenerService {
	private MongoClient mongo;
	private MongoDatabase db;
	private MongoCollection<Document> collection;

	public MongoDBUrlShortenerService(String host, int port, String database) throws UnknownHostException {
		mongo = new MongoClient(host, port);
		mongo.setWriteConcern(WriteConcern.JOURNALED);
		db = mongo.getDatabase(database);
		collection = db.getCollection("urls");

		collection.createIndex( //
				new Document("longUrl", 1), //
				new IndexOptions().name("by-longUrl")); //

		collection.createIndex( //
				new Document("userId", 1), //
				new IndexOptions().name("by-userId"));
	}

	@PreDestroy
	public void close() {
		mongo.close();
	}

	@Override
	public ShortenedUrl shorten(String longUrl) {
		ShortenedUrl url = null;
		Document o = collection.find( //
				eq("longUrl", longUrl)) //
				.first();

		if (o != null) {
			url = convert(o);
		} else {
			String slug = RandomStringUtils.randomAlphanumeric(6);
			url = create(longUrl, slug);
		}

		return url;
	}

	@Override
	public ShortenedUrl shorten(String longUrl, String slug) {
		Document o = collection.find( //
				eq("_id", slug)) //
				.first();

		if (o != null) {
			throw new ConflictException("shortenedUrls", slug);
		}

		return create(longUrl, slug);
	}

	private ShortenedUrl create(String longUrl, String slug) {
		String userId = Claims.current().getSubject().or("unknown");

		Document doc = new Document("_id", slug) //
				.append("longUrl", longUrl) //
				.append("visits", 0L) //
				.append("createdBy", userId) //
				.append("createdDate", new Date());

		collection.insertOne(doc);

		return convert(doc);
	}

	public Optional<ShortenedUrl> expand(String slug) {
		Document o = collection.find(new Document("_id", slug)).first();

		return Optional.fromNullable(o != null ? convert(o) : null);
	}

	public void signalVisit(String slug) {
		collection.updateOne( //
				eq("_id", slug), //
				new Document("$inc", new Document("visits", 1)));
	}

	public long getVisits(String slug) {
		Document o = collection.find( //
				eq("_id", slug)) //
				.projection(new Document("visits", 1)) //
				.first();

		return (o != null) ? ((Number) o.get("visits")).longValue() : 0;
	}

	private ShortenedUrl convert(Document o) {
		ShortenedUrl url = new ShortenedUrl();

		url.setSlug(o.get("_id").toString());
		url.setLongUrl((String) o.get("longUrl"));
		url.setVisits(((Number) o.get("visits")).longValue());
		url.setCreatedBy((String) o.get("createdBy"));
		url.setCreatedDate(new DateTime((Date) o.get("createdDate")));

		return url;
	}

	@Override
	public List<ShortenedUrl> urlsByUserId(String userId) {
		List<ShortenedUrl> urls = new LinkedList<ShortenedUrl>();
		FindIterable<Document> cursor = collection.find(new Document("createdBy", userId));

		for (Document o : cursor) {
			urls.add(convert(o));
		}

		return urls;
	}
}
