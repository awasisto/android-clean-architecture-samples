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
import com.wasisto.githubuserfinder.Callback;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase;
import com.wasisto.githubuserfinder.domain.SearchUseCase;
import com.wasisto.githubuserfinder.ui.Event;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";

    private MutableLiveData<SearchUserResult> result = new MutableLiveData<>();

    private MutableLiveData<List<SearchHistoryItem>> history = new MutableLiveData<>();

    private MutableLiveData<Event<Integer>> errorMessageEvent = new MutableLiveData<>();

    private SearchUseCase searchUseCase;

    private GetHistoryUseCase getHistoryUseCase;

    private LoggingHelper loggingHelper;

    public SearchViewModel(SearchUseCase searchUseCase, GetHistoryUseCase getHistoryUseCase,
                           LoggingHelper loggingHelper) {
        this.searchUseCase = searchUseCase;
        this.getHistoryUseCase = getHistoryUseCase;
        this.loggingHelper = loggingHelper;

        this.getHistoryUseCase.execute(null, new Callback<List<SearchHistoryItem>>() {
            @Override
            public void onSuccess(List<SearchHistoryItem> history) {
                SearchViewModel.this.history.setValue(history);
            }

            @Override
            public void onError(Throwable error) {
                loggingHelper.error(TAG, "An error occurred while getting the search user history", error);
            }
        });
    }

    public void onSearch(String query) {
        searchUseCase.execute(query, new Callback<SearchUserResult>() {
            @Override
            public void onSuccess(SearchUserResult result) {
                SearchViewModel.this.result.setValue(result);
            }

            @Override
            public void onError(Throwable error) {
                loggingHelper.error(TAG, "An error occurred while searching users", error);

                Event<Integer> errorEvent = new Event<>();
                errorEvent.setData(R.string.an_error_occurred);

                SearchViewModel.this.errorMessageEvent.setValue(errorEvent);
            }
        });
    }

    public MutableLiveData<SearchUserResult> getResult() {
        return result;
    }

    public MutableLiveData<List<SearchHistoryItem>> getHistory() {
        return history;
    }

    public MutableLiveData<Event<Integer>> getErrorMessageEvent() {
        return errorMessageEvent;
    }
}
