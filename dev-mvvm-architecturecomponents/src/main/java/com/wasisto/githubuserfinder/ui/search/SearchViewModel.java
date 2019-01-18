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

package com.wasisto.githubuserfinder.ui.search;

import android.arch.lifecycle.*;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.Resource;
import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase;
import com.wasisto.githubuserfinder.domain.SearchUseCase;
import com.wasisto.githubuserfinder.ui.Event;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

import java.util.List;

import static com.wasisto.githubuserfinder.data.Resource.Status.ERROR;
import static com.wasisto.githubuserfinder.data.Resource.Status.LOADING;
import static com.wasisto.githubuserfinder.data.Resource.Status.SUCCESS;

public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";

    private MediatorLiveData<Resource<SearchUserResult>> searchUserResult = new MediatorLiveData<>();

    private MediatorLiveData<Resource<List<SearchHistoryItem>>> getHistoryResult = new MediatorLiveData<>();

    private MutableLiveData<String> query = new MutableLiveData<>();

    private LiveData<SearchUserResult> result;

    private LiveData<List<SearchHistoryItem>> history;

    private LiveData<Boolean> isShouldShowNoResultsText;

    private LiveData<Boolean> isShouldShowResult;

    private MediatorLiveData<Boolean> isShouldShowHistory = new MediatorLiveData<>();

    private MediatorLiveData<Boolean> isLoading = new MediatorLiveData<>();

    private MediatorLiveData<Event<Integer>> showToastEvent = new MediatorLiveData<>();

    private MutableLiveData<Event<String>> openUserDetailsActivityEvent = new MutableLiveData<>();

    private SearchUseCase searchUseCase;

    private LoggingHelper loggingHelper;

    public SearchViewModel(SearchUseCase searchUseCase, GetHistoryUseCase getHistoryUseCase,
                           LoggingHelper loggingHelper) {
        this.searchUseCase = searchUseCase;
        this.loggingHelper = loggingHelper;

        getHistoryResult.addSource(
                getHistoryUseCase.execute(null),
                resource -> {
                    if (resource != null && resource.status == ERROR) {
                        loggingHelper.error(TAG, "An error occurred while getting the search user history",
                                resource.error);
                    }

                    getHistoryResult.setValue(resource);
                }
        );

        result = Transformations.map(
                searchUserResult,
                resource -> resource.data
        );

        history = Transformations.map(
                getHistoryResult,
                resource -> resource.data
        );

        isShouldShowNoResultsText = Transformations.map(
                searchUserResult,
                resource -> {
                    if (resource.status == SUCCESS) {
                        return resource.data.getTotalCount() == 0;
                    } else {
                        return false;
                    }
                }
        );

        isShouldShowResult = Transformations.map(
                searchUserResult,
                resource -> resource.status == SUCCESS
        );

        isShouldShowHistory.setValue(true);

        isShouldShowHistory.addSource(
                searchUserResult,
                resource -> isShouldShowHistory.setValue(false)
        );

        isLoading.addSource(
                searchUserResult,
                resource -> {
                    if (resource != null) {
                        isLoading.setValue(resource.status == LOADING);
                    }
                }
        );

        isLoading.addSource(
                getHistoryResult,
                resource -> {
                    if (resource != null) {
                        isLoading.setValue(resource.status == LOADING);
                    }
                }
        );

        showToastEvent.addSource(
                searchUserResult,
                resource -> {
                    if (resource != null && resource.status == ERROR) {
                        showToastEvent.setValue(new Event<>(R.string.an_error_occurred));
                    }
                }
        );

        showToastEvent.addSource(
                getHistoryResult,
                resource -> {
                    if (resource != null && resource.status == ERROR) {
                        showToastEvent.setValue(new Event<>(R.string.an_error_occurred));
                    }
                }
        );
    }

    public void onSearch() {
        String queryValue = query.getValue();
        if (queryValue != null) {
            if (query.getValue().isEmpty()) {
                showToastEvent.setValue(new Event<>(R.string.enter_search_query));
            } else {
                searchUserResult.addSource(
                        searchUseCase.execute(queryValue),
                        resource -> {
                            if (resource != null && resource.status == ERROR) {
                                loggingHelper.error(TAG, "An error occurred while searching users",
                                        resource.error);
                            }

                            searchUserResult.setValue(resource);
                        }
                );
            }
        }
    }

    public void onResultItemClick(SearchUserResult.Item resultItem) {
        openUserDetailsActivityEvent.setValue(new Event<>(resultItem.getLogin()));
    }

    public void onHistoryItemClick(SearchHistoryItem historyItem) {
        query.setValue(historyItem.getQuery());
        onSearch();
    }

    public MutableLiveData<String> getQuery() {
        return query;
    }

    public LiveData<SearchUserResult> getResult() {
        return result;
    }

    public LiveData<List<SearchHistoryItem>> getHistory() {
        return history;
    }

    public LiveData<Boolean> getIsShouldShowNoResultsText() {
        return isShouldShowNoResultsText;
    }

    public LiveData<Boolean> getIsShouldShowResult() {
        return isShouldShowResult;
    }

    public LiveData<Boolean> getIsShouldShowHistory() {
        return isShouldShowHistory;
    }

    public MediatorLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MediatorLiveData<Event<Integer>> getShowToastEvent() {
        return showToastEvent;
    }

    public MutableLiveData<Event<String>> getOpenUserDetailsActivityEvent() {
        return openUserDetailsActivityEvent;
    }
}
