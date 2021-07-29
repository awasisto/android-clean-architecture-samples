package com.wasisto.githubuserfinder.android.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasisto.githubuserfinder.android.ui.common.Event
import com.wasisto.githubuserfinder.common.Logger
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    val query = MutableLiveData<String>()

    val searchResultItems = MutableLiveData<List<SearchUsersResult.Item>>()

    val searchHistoryItems = MutableLiveData<List<SearchHistoryItem>>()

    val shouldShowNoResultsText = MutableLiveData<Boolean>()

    val shouldShowHistory = MutableLiveData<Boolean>()

    val shouldShowLoadingIndicator = MutableLiveData<Boolean>()

    val showErrorToastEvent = MutableLiveData<Event<Unit>>()

    val openUserDetailsActivityEvent = MutableLiveData<Event<String>>()

    fun load() = viewModelScope.launch(mainDispatcher) {
        shouldShowLoadingIndicator.value = true

        viewModelScope.launch(ioDispatcher) {
            try {
                val history = getSearchHistoryUseCase()

                logger.debug("history: $history")

                val descSortedHistory = history.sortedByDescending { it.id }

                launch(mainDispatcher) {
                    searchHistoryItems.value = descSortedHistory
                    shouldShowHistory.value = true
                }
            } catch (e: Exception) {
                logger.warn("An error occurred while getting the search user history", e)

                launch(mainDispatcher) {
                    showErrorToastEvent.value = Event(Unit)
                }
            }

            launch(mainDispatcher) {
                shouldShowLoadingIndicator.value = false
            }
        }
    }

    fun onSearchButtonClick() = viewModelScope.launch(mainDispatcher) {
        doSearch()
    }

    fun onResultItemClick(resultItem: SearchUsersResult.Item) = viewModelScope.launch(mainDispatcher) {
        openUserDetailsActivityEvent.value = Event(resultItem.login)
    }

    fun onSearchHistoryItemClick(historyItem: SearchHistoryItem) = viewModelScope.launch(mainDispatcher) {
        query.value = historyItem.query
        doSearch()
    }

    private fun doSearch() {
        shouldShowHistory.value = false
        shouldShowLoadingIndicator.value = true

        viewModelScope.launch(ioDispatcher) {
            try {
                val result = searchUsersUseCase(query.value!!)

                logger.debug("result: $result")

                launch(mainDispatcher) {
                    searchResultItems.value = result.items
                    shouldShowNoResultsText.value = (result.totalCount == 0L)
                }
            } catch (e: Exception) {
                logger.warn( "An error occurred while searching users", e)

                launch(mainDispatcher) {
                    shouldShowHistory.value = true
                    showErrorToastEvent.value = Event(Unit)
                }
            }

            launch(mainDispatcher) {
                shouldShowLoadingIndicator.value = false
            }
        }
    }
}