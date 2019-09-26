/*
 * Copyright (c) 2018 Andika Wasisto
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

import com.wasisto.githubuserfinder.R
import com.wasisto.githubuserfinder.model.SearchUserResult
import com.wasisto.githubuserfinder.model.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase
import com.wasisto.githubuserfinder.domain.SearchUseCase
import com.wasisto.githubuserfinder.util.logging.LoggingHelper

class SearchPresenterImpl(
    private val view: SearchView,
    private val searchUseCase: SearchUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val loggingHelper: LoggingHelper
) : SearchPresenter {

    companion object {

        private const val TAG = "SearchPresenterImpl"
    }

    override fun onViewCreated() {
        view.showLoadingIndicator()
        view.hideResultItems()
        view.hideHistory()
        view.hideNoResultsText()

        getHistoryUseCase.execute(
            null,
            onSuccess = { history ->
                view.hideLoadingIndicator()

                val sortedHistory = history.sortedByDescending { it.id }
                view.showHistory(sortedHistory)
            },
            onError = { error ->
                loggingHelper.error(TAG, "An error occurred while getting the search user history", error)

                view.hideLoadingIndicator()

                view.showToast(R.string.an_error_occurred)
            }
        )
    }

    override fun onSearch(query: String) {
        search(query)
    }

    override fun onResultItemClick(resultItem: SearchUserResult.Item) {
        view.openUserDetailsActivity(resultItem.login)
    }

    override fun onHistoryItemClick(historyItem: SearchHistoryItem) {
        view.setQuery(historyItem.query)

        search(historyItem.query)
    }

    private fun search(query: String) {
        if (query.isEmpty()) {
            view.showToast(R.string.enter_search_query)
        } else {
            view.showLoadingIndicator()
            view.hideResultItems()
            view.hideHistory()
            view.hideNoResultsText()

            searchUseCase.execute(
                query,
                onSuccess = { result ->
                    view.hideLoadingIndicator()

                    if (result.items.isNotEmpty()) {
                        view.showResultItems(result.items)
                    } else {
                        view.showNoResultsText()
                    }
                },
                onError = { error ->
                    loggingHelper.error(TAG, "An error occurred while searching users", error)

                    view.hideLoadingIndicator()

                    view.showToast(R.string.an_error_occurred)
                }
            )
        }
    }
}
