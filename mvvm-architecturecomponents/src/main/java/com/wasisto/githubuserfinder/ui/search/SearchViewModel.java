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

package com.wasisto.githubuserfinder.ui.search;

import androidx.lifecycle.*;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.domain.UseCase;
import com.wasisto.githubuserfinder.model.SearchUserResult;
import com.wasisto.githubuserfinder.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase;
import com.wasisto.githubuserfinder.domain.SearchUseCase;
import com.wasisto.githubuserfinder.ui.Event;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

import java.util.List;

import static com.wasisto.githubuserfinder.domain.UseCase.Result.Status.ERROR;
import static com.wasisto.githubuserfinder.domain.UseCase.Result.Status.LOADING;
import static com.wasisto.githubuserfinder.domain.UseCase.Result.Status.SUCCESS;

public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";

    private MediatorLiveData<UseCase.Result<SearchUserResult>> searchUserResult = new MediatorLiveData<>();

    private MediatorLiveData<UseCase.Result<List<SearchHistoryItem>>> getHistoryResult = new MediatorLiveData<>();

    private MutableLiveData<String> query = new MutableLiveData<>();

    private LiveData<SearchUserResult> result;

    private LiveData<List<SearchHistoryItem>> history;

    private LiveData<Boolean> shouldShowNoResultsText;

    private LiveData<Boolean> shouldShowResult;

    private MediatorLiveData<Boolean> shouldShowHistory = new MediatorLiveData<>();

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
                getHistoryUseCase.executeAsync(null),
                result -> {
                    if (result.status == ERROR) {
                        loggingHelper.error(TAG, "An error occurred while getting the search user history", result.error);
                    }

                    getHistoryResult.setValue(result);
                }
        );

        result = Transformations.map(
                searchUserResult,
                result -> result.data
        );

        history = Transformations.map(
                getHistoryResult,
                result -> result.data
        );

        shouldShowNoResultsText = Transformations.map(
                searchUserResult,
                result -> {
                    if (result.status == SUCCESS) {
                        return result.data.getTotalCount() == 0;
                    } else {
                        return false;
                    }
                }
        );

        shouldShowResult = Transformations.map(
                searchUserResult,
                result -> result.status == SUCCESS
        );

        shouldShowHistory.setValue(true);

        shouldShowHistory.addSource(
                searchUserResult,
                result -> shouldShowHistory.setValue(false)
        );

        isLoading.addSource(
                searchUserResult,
                result -> isLoading.setValue(result.status == LOADING)
        );

        isLoading.addSource(
                getHistoryResult,
                result -> isLoading.setValue(result.status == LOADING)
        );

        showToastEvent.addSource(
                searchUserResult,
                result -> {
                    if (result.status == ERROR) {
                        showToastEvent.setValue(new Event<>(R.string.an_error_occurred));
                    }
                }
        );

        showToastEvent.addSource(
                getHistoryResult,
                result -> {
                    if (result.status == ERROR) {
                        showToastEvent.setValue(new Event<>(R.string.an_error_occurred));
                    }
                }
        );
    }

    public void onSearch() {
        String q = query.getValue();
        if (q != null) {
            if (query.getValue().isEmpty()) {
                showToastEvent.setValue(new Event<>(R.string.enter_search_query));
            } else {
                searchUserResult.addSource(
                        searchUseCase.executeAsync(q),
                        result -> {
                            if (result.status == ERROR) {
                                loggingHelper.error(TAG, "An error occurred while searching users", result.error);
                            }

                            searchUserResult.setValue(result);
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

    public LiveData<Boolean> getShouldShowNoResultsText() {
        return shouldShowNoResultsText;
    }

    public LiveData<Boolean> getShouldShowResult() {
        return shouldShowResult;
    }

    public LiveData<Boolean> getShouldShowHistory() {
        return shouldShowHistory;
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
