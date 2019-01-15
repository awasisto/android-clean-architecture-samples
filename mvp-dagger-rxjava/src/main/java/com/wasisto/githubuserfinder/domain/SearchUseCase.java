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

package com.wasisto.githubuserfinder.domain;

import com.wasisto.githubuserfinder.data.github.GithubDataSource;
import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;
import io.reactivex.Observable;

import javax.inject.Inject;

public class SearchUseCase implements UseCase<String, SearchUserResult> {

    private static final String TAG = "SearchUseCase";

    private GithubDataSource githubDataSource;

    private SearchHistoryDataSource searchHistoryDataSource;

    private LoggingHelper loggingHelper;

    @Inject
    public SearchUseCase(GithubDataSource githubDataSource, SearchHistoryDataSource searchHistoryDataSource,
                         LoggingHelper loggingHelper) {
        this.githubDataSource = githubDataSource;
        this.searchHistoryDataSource = searchHistoryDataSource;
        this.loggingHelper = loggingHelper;
    }

    @Override
    public Observable<SearchUserResult> execute(String query) {
        SearchHistoryItem searchHistoryItem = new SearchHistoryItem();
        searchHistoryItem.setQuery(query);

        return searchHistoryDataSource.add(searchHistoryItem)
                .doOnComplete(
                        () -> loggingHelper.info(TAG, "Query added to the search history")
                )
                .doOnError(
                        error -> loggingHelper.warn(TAG, "An error occurred while adding a query to the " +
                                "search history", error)
                )
                .andThen(
                        githubDataSource.searchUser(query)
                );
    }
}
