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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import com.wasisto.githubuserfinder.data.Resource;
import com.wasisto.githubuserfinder.data.github.GithubDataSource;
import com.wasisto.githubuserfinder.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

import static com.wasisto.githubuserfinder.data.Resource.Status.ERROR;
import static com.wasisto.githubuserfinder.data.Resource.Status.SUCCESS;

public class SearchUseCase implements UseCase<String, Resource<SearchUserResult>> {

    private static final String TAG = "SearchUseCase";

    private GithubDataSource githubDataSource;

    private SearchHistoryDataSource searchHistoryDataSource;

    private LoggingHelper loggingHelper;

    public SearchUseCase(GithubDataSource githubDataSource, SearchHistoryDataSource searchHistoryDataSource,
                         LoggingHelper loggingHelper) {
        this.githubDataSource = githubDataSource;
        this.searchHistoryDataSource = searchHistoryDataSource;
        this.loggingHelper = loggingHelper;
    }

    @Override
    public LiveData<Resource<SearchUserResult>> execute(String query) {
        MediatorLiveData liveData = new MediatorLiveData<>();

        SearchHistoryItem searchHistoryItem = new SearchHistoryItem();
        searchHistoryItem.setQuery(query);

        liveData.addSource(
                searchHistoryDataSource.add(searchHistoryItem),
                object -> {
                    if (object != null) {
                        Resource resource = (Resource) object;
                        if (resource.status == SUCCESS) {
                            loggingHelper.info(TAG, "Query added to the search history");
                        } else if (resource.status == ERROR) {
                            loggingHelper.warn(TAG, "An error occurred while adding a query to the " +
                                            "search history", resource.error);
                        }
                    }
                }
        );

        liveData.addSource(
                githubDataSource.searchUser(query),
                liveData::setValue
        );

        return liveData;
    }
}
