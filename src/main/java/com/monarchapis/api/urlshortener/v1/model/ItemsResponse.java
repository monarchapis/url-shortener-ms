package com.monarchapis.api.urlshortener.v1.model;

import java.util.List;

public class ItemsResponse<T> {
	private List<T> items;

	public ItemsResponse(List<T> items) {
		this.items = items;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
}
