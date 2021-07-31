package com.wasisto.githubuserfinder.android.ui.search;

import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;

import java.util.List;

public interface SearchView {

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void setQuery(String query);

    void showSearchResult(List<SearchUsersResult.Item> searchResult);

    void showSearchHistory(List<SearchHistoryItem> searchHistory);

    void hideSearchHistory();

    void showNoResultsText();

    void hideNoResultsText();

    void openUserDetailsActivity(String username);

    void showErrorToast();
}
