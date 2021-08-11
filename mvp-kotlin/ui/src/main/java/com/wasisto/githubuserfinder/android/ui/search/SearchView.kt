package com.wasisto.githubuserfinder.android.ui.search

import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult

interface SearchView {

    fun showLoadingIndicator()

    fun hideLoadingIndicator()

    fun setQuery(query: String?)

    fun showSearchResult(searchResult: List<SearchUsersResult.Item>)

    fun showSearchHistory(searchHistory: List<SearchHistoryItem>)

    fun hideSearchHistory()

    fun showNoResultsText()

    fun hideNoResultsText()

    fun openUserDetailsActivity(username: String)

    fun showErrorToast()
}
