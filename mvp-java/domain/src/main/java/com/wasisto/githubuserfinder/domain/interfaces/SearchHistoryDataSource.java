package com.wasisto.githubuserfinder.domain.interfaces;

import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;

import java.util.List;

public interface SearchHistoryDataSource {

    List<SearchHistoryItem> getAll() throws Exception;

    void add(SearchHistoryItem searchHistoryItem) throws Exception;
}
