package com.wasisto.githubuserfinder.domain.usecases

import com.wasisto.githubuserfinder.common.Logger
import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult

class SearchUsersUseCase(
    private val githubDataSource: GithubDataSource,
    private val searchHistoryDataSource: SearchHistoryDataSource,
    private val logger: Logger
) {
    operator fun invoke(query: String): SearchUsersResult {
        try {
            searchHistoryDataSource.add(SearchHistoryItem(query = query))
            logger.info("Query added to the search history")
        } catch (e: Exception) {
            logger.warn("An error occurred while adding a query to the search history", e)
        }
        return githubDataSource.searchUsers(query)
    }
}