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

import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.GetSearchHistoryUseCase;
import com.wasisto.githubuserfinder.domain.SearchUseCase;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.Collections;

public class SearchPresenterImpl implements SearchPresenter {

    private static final String TAG = "SearchPresenterImpl";

    private SearchView view;

    private SearchUseCase searchUseCase;

    private GetSearchHistoryUseCase getSearchHistoryUseCase;

    private LoggingHelper loggingHelper;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public SearchPresenterImpl(SearchView view, SearchUseCase searchUseCase,
                               GetSearchHistoryUseCase getSearchHistoryUseCase, LoggingHelper loggingHelper) {
        this.view = view;
        this.searchUseCase = searchUseCase;
        this.getSearchHistoryUseCase = getSearchHistoryUseCase;
        this.loggingHelper = loggingHelper;
    }

    @Override
    public void onViewCreated() {
        view.showLoadingIndicator();
        view.hideResultItems();
        view.hideHistory();
        view.hideNoResultsText();

        compositeDisposable.add(
            getSearchHistoryUseCase.execute(null)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            history -> {
                                view.hideLoadingIndicator();

                                Collections.sort(history, (item1, item2) -> Integer.compare(item2.getId(),
                                        item1.getId()));
                                view.showHistory(history);
                            },
                            error -> {
                                loggingHelper.error(TAG, "An error occurred while getting the search " +
                                        "user history", error);

                                view.hideLoadingIndicator();

                                view.showToast(R.string.an_error_occurred);
                            }
                    )
        );
    }

    @Override
    public void onSearch(String query) {
        search(query);
    }

    @Override
    public void onResultItemClick(SearchUserResult.Item resultItem) {
        view.openUserDetailsActivity(resultItem.getLogin());
    }

    @Override
    public void onHistoryItemClick(SearchHistoryItem historyItem) {
        view.setQuery(historyItem.getQuery());

        search(historyItem.getQuery());
    }
    
    private void search(String query) {
        if (query.isEmpty()) {
            view.showToast(R.string.enter_search_query);
        } else {
            view.showLoadingIndicator();
            view.hideResultItems();
            view.hideHistory();
            view.hideNoResultsText();

            compositeDisposable.add(
                    searchUseCase.execute(query)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    result -> {
                                        view.hideLoadingIndicator();

                                        if (!result.getItems().isEmpty()) {
                                            view.showResultItems(result.getItems());
                                        } else {
                                            view.showNoResultsText();
                                        }
                                    },
                                    error -> {
                                        loggingHelper.error(TAG, "An error occurred while searching users",
                                                error);

                                        view.hideLoadingIndicator();

                                        view.showToast(R.string.an_error_occurred);
                                    }
                            )
            );
        }
    }

    @Override
    public void onViewDestroyed() {
        compositeDisposable.dispose();
    }
}
