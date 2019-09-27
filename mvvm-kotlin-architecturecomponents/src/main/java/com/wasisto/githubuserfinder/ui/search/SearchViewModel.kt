/*
 * Copyright (c) 2019 Andika Wasisto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wasisto.githubuserfinder.ui.search

import androidx.lifecycle.*
import com.wasisto.githubuserfinder.R
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase
import com.wasisto.githubuserfinder.domain.SearchUseCase
import com.wasisto.githubuserfinder.domain.UseCase
import com.wasisto.githubuserfinder.model.SearchHistoryItem
import com.wasisto.githubuserfinder.ui.Event

import com.wasisto.githubuserfinder.model.SearchUserResult
import com.wasisto.githubuserfinder.util.logging.LoggingHelper

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    getHistoryUseCase: GetHistoryUseCase,
    private val loggingHelper: LoggingHelper
) : ViewModel() {

    companion object {
        private const val TAG = "SearchViewModel"
    }

    private val searchUserResult = MediatorLiveData<UseCase.Result<SearchUserResult>>()

    private val getHistoryResult = MediatorLiveData<UseCase.Result<List<SearchHistoryItem>>>()

    val query = MutableLiveData<String>()

    val result: LiveData<SearchUserResult>

    val history: LiveData<List<SearchHistoryItem>>

    val shouldShowNoResultsText: LiveData<Boolean>

    val shouldShowResult: LiveData<Boolean>

    val shouldShowHistory = MediatorLiveData<Boolean>()

    val isLoading = MediatorLiveData<Boolean>()

    val showToastEvent = MediatorLiveData<Event<Int>>()

    val openUserDetailsActivityEvent = MutableLiveData<Event<String>>()

    init {
        getHistoryResult.addSource(getHistoryUseCase.executeAsync(Unit)) { result ->
            if (result is UseCase.Result.Error) {
                loggingHelper.warn(TAG, "An error occurred while getting the search user history", result.error)
            }

            getHistoryResult.value = result
        }

        result = Transformations.map(searchUserResult) { result ->
            (result as? UseCase.Result.Success)?.data
        }

        history = Transformations.map(getHistoryResult) { result ->
            (result as? UseCase.Result.Success)?.data?.sortedByDescending { it.id }
        }

        shouldShowNoResultsText = Transformations.map(searchUserResult) { result ->
            if (result is UseCase.Result.Success) {
                return@map result.data.totalCount == 0L
            } else {
                return@map false
            }
        }

        shouldShowResult = Transformations.map(searchUserResult) { result ->
            result is UseCase.Result.Success
        }

        shouldShowHistory.value = true

        shouldShowHistory.addSource(searchUserResult) {
            shouldShowHistory.value = false
        }

        isLoading.addSource(searchUserResult) { result ->
            if (result != null) {
                isLoading.value = result is UseCase.Result.Loading
            }
        }

        isLoading.addSource(getHistoryResult) { result ->
            if (result != null) {
                isLoading.value = result is UseCase.Result.Loading
            }
        }

        showToastEvent.addSource(searchUserResult) { result ->
            if (result is UseCase.Result.Error) {
                showToastEvent.value = Event(R.string.an_error_occurred)
            }
        }
    }

    fun onSearch() {
        val q = query.value

        if (q != null) {
            if (q.isEmpty()) {
                showToastEvent.value = Event(R.string.enter_search_query)
            } else {
                searchUserResult.addSource(searchUseCase.executeAsync(q)) { result ->
                    searchUserResult.value = result
                }
            }
        }
    }

    fun onResultItemClick(username: String) {
        openUserDetailsActivityEvent.value = Event(username)
    }

    fun onHistoryItemClick(q: String) {
        query.value = q
        onSearch()
    }
}
