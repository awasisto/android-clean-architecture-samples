package com.wasisto.githubuserfinder.domain.usecases;

import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource;
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;

public class SearchUsersUseCase {

    private GithubDataSource githubDataSource;

    private SearchHistoryDataSource searchHistoryDataSource;

    private Logger logger;

    public SearchUsersUseCase(GithubDataSource githubDataSource, SearchHistoryDataSource searchHistoryDataSource, Logger logger) {
        this.githubDataSource = githubDataSource;
        this.searchHistoryDataSource = searchHistoryDataSource;
        this.logger = logger;
    }

    public SearchUsersResult execute(String query) throws Exception {
        try {
            searchHistoryDataSource.add(
                    new SearchHistoryItem() {{
                        setQuery(query);
                    }}
            );
        } catch (Exception e) {
            logger.warn("An error occurred while adding a query to the search history", e);
        }
        return githubDataSource.searchUsers(query);
    }
}
