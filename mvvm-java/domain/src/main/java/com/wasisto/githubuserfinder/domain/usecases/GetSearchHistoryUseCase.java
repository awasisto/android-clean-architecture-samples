package com.wasisto.githubuserfinder.domain.usecases;

import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;

import java.util.List;

public class GetSearchHistoryUseCase {

    private SearchHistoryDataSource searchHistoryDataSource;

    public GetSearchHistoryUseCase(SearchHistoryDataSource searchHistoryDataSource) {
        this.searchHistoryDataSource = searchHistoryDataSource;
    }

    public List<SearchHistoryItem> execute() throws Exception {
        return searchHistoryDataSource.getAll();
    }
}
