package com.wasisto.githubuserfinder.domain.models;

import com.google.common.base.MoreObjects;

public class SearchHistoryItem {

    private Integer id;

    private String query;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("query", query)
                .toString();
    }
}
