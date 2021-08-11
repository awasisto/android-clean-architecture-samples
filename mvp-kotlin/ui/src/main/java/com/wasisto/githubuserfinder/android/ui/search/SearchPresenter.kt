package com.wasisto.githubuserfinder.android.ui.search

import com.wasisto.githubuserfinder.common.Logger
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SearchPresenter(
    private val view: SearchView,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {

    private lateinit var descSortedHistory: List<SearchHistoryItem>

    fun load() = coroutineScope.launch(mainDispatcher) {
        view.showLoadingIndicator()

        launch(ioDispatcher) {
            try {
                val history: List<SearchHistoryItem> = getSearchHistoryUseCase()

                logger.debug("history: $history")

                descSortedHistory = history.sortedByDescending { it.id }

                launch(mainDispatcher) {
                    view.showSearchHistory(descSortedHistory)
                }
            } catch (e: Exception) {
                logger.warn("An error occurred while getting the search user history", e)

                launch(mainDispatcher) {
                    view.showErrorToast()
                }
            }

            launch(mainDispatcher) {
                view.hideLoadingIndicator()
            }
        }
    }

    fun search(username: String) = coroutineScope.launch(mainDispatcher) {
        view.hideSearchHistory()
        view.showLoadingIndicator()

        launch(ioDispatcher) {
            try {
                val result: SearchUsersResult = searchUsersUseCase(username)

                logger.debug("result: $result")

                launch(mainDispatcher) {
                    if (result.totalCount == 0L) {
                        view.showNoResultsText()
                    } else {
                        view.hideNoResultsText()
                        view.showSearchResult(result.items)
                    }
                }
            } catch (e: Exception) {
                logger.warn("An error occurred while searching users", e)

                launch(mainDispatcher) {
                    view.showSearchHistory(descSortedHistory)
                    view.showErrorToast()
                }
            }

            launch(mainDispatcher) {
                view.hideLoadingIndicator()
            }
        }
    }

    fun onResultItemClick(resultItem: SearchUsersResult.Item) = coroutineScope.launch(mainDispatcher) {
        view.openUserDetailsActivity(resultItem.login)
    }

    fun onSearchHistoryItemClick(historyItem: SearchHistoryItem) = coroutineScope.launch(mainDispatcher) {
        val query = historyItem.query
        view.setQuery(query)
        search(query)
    }
}
