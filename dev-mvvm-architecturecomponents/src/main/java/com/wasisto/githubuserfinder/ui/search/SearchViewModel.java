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

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase;
import com.wasisto.githubuserfinder.domain.SearchUseCase;
import com.wasisto.githubuserfinder.ui.Event;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

import java.util.Collections;
import java.util.List;

import static com.wasisto.githubuserfinder.data.Resource.Status.ERROR;
import static com.wasisto.githubuserfinder.data.Resource.Status.LOADING;
import static com.wasisto.githubuserfinder.data.Resource.Status.SUCCESS;

public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private MutableLiveData<String> query = new MutableLiveData<>();

    private MutableLiveData<SearchUserResult> result = new MutableLiveData<>();

    private MutableLiveData<List<SearchHistoryItem>> history = new MutableLiveData<>();

    private MutableLiveData<Boolean> isResultEmpty = new MutableLiveData<>();

    private MutableLiveData<Boolean> isResultShouldBeShown = new MutableLiveData<>();

    private MutableLiveData<Boolean> isHistoryShouldBeShown = new MutableLiveData<>();

    private MutableLiveData<Event<String>> openUserDetailsActivityEvent = new MutableLiveData<>();

    private MutableLiveData<Event<Integer>> showToastEvent = new MutableLiveData<>();

    private SearchUseCase searchUseCase;

    private LoggingHelper loggingHelper;

    public SearchViewModel(SearchUseCase searchUseCase, GetHistoryUseCase getHistoryUseCase,
                           LoggingHelper loggingHelper) {
        this.searchUseCase = searchUseCase;
        this.loggingHelper = loggingHelper;

        getHistoryUseCase.execute(null).observeForever(resource -> {
            if (resource != null) {
                isResultShouldBeShown.setValue(false);

                if (resource.status == LOADING) {
                    isLoading.setValue(true);
                    isHistoryShouldBeShown.setValue(false);
                } else if (resource.status == SUCCESS) {
                    isLoading.setValue(false);
                    Collections.sort(resource.data, (item1, item2) -> Integer.compare(item2.getId(), item1.getId()));
                    history.setValue(resource.data);
                    isHistoryShouldBeShown.setValue(true);
                } else if (resource.status == ERROR) {
                    loggingHelper.error(TAG, "An error occurred while getting the search user history",
                            resource.error);

                    isLoading.setValue(false);
                    showToastEvent.setValue(new Event<>(R.string.an_error_occurred));
                }
            }
        });
    }

    public void onSearch() {
        if (query.getValue() == null || query.getValue().isEmpty()) {
            showToastEvent.setValue(new Event<>(R.string.enter_search_query));
        } else {
            searchUseCase.execute(query.getValue()).observeForever(resource -> {
                if (resource != null) {
                    isHistoryShouldBeShown.setValue(false);

                    if (resource.status == LOADING) {
                        isLoading.setValue(true);
                        isResultShouldBeShown.setValue(false);
                    } else if (resource.status == SUCCESS) {
                        isLoading.setValue(false);
                        result.setValue(resource.data);
                        isResultShouldBeShown.setValue(true);
                    } else if (resource.status == ERROR) {
                        loggingHelper.error(TAG, "An error occurred while searching users", resource.error);

                        isLoading.setValue(false);
                        showToastEvent.setValue(new Event<>(R.string.an_error_occurred));
                    }
                }
            });
        }
    }

    public void onResultItemClick(SearchUserResult.Item resultItem) {
        openUserDetailsActivityEvent.setValue(new Event<>(resultItem.getLogin()));
    }

    public void onHistoryItemClick(SearchHistoryItem historyItem) {
        query.setValue(historyItem.getQuery());
        onSearch();
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getQuery() {
        return query;
    }

    public MutableLiveData<SearchUserResult> getResult() {
        return result;
    }

    public MutableLiveData<List<SearchHistoryItem>> getHistory() {
        return history;
    }

    public MutableLiveData<Boolean> getIsResultEmpty() {
        return isResultEmpty;
    }

    public MutableLiveData<Boolean> getIsResultShouldBeShown() {
        return isResultShouldBeShown;
    }

    public MutableLiveData<Boolean> getIsHistoryShouldBeShown() {
        return isHistoryShouldBeShown;
    }

    public MutableLiveData<Event<String>> getOpenUserDetailsActivityEvent() {
        return openUserDetailsActivityEvent;
    }

    public MutableLiveData<Event<Integer>> getShowToastEvent() {
        return showToastEvent;
    }
}
