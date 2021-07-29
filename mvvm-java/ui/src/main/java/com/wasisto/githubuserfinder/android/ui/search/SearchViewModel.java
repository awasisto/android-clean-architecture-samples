package com.wasisto.githubuserfinder.android.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasisto.githubuserfinder.android.ui.common.Event;
import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase;
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SearchViewModel extends ViewModel {

    private GetSearchHistoryUseCase getSearchHistoryUseCase;

    private SearchUsersUseCase searchUsersUseCase;

    private Logger logger;

    private ExecutorService ioExecutorService;

    private ExecutorService mainExecutorService;

    private MutableLiveData<String> query = new MutableLiveData<>();

    private MutableLiveData<List<SearchUsersResult.Item>> searchResultItems = new MutableLiveData<>();

    private MutableLiveData<List<SearchHistoryItem>> searchHistoryItems = new MutableLiveData<>();

    private MutableLiveData<Boolean> shouldShowNoResultsText = new MutableLiveData<>();

    private MutableLiveData<Boolean> shouldShowHistory = new MutableLiveData<>();

    private MutableLiveData<Boolean> shouldShowLoadingIndicator = new MutableLiveData<>();

    private MutableLiveData<Event<Void>> showErrorToastEvent = new MutableLiveData<>();

    private MutableLiveData<Event<String>> openUserDetailsActivityEvent = new MutableLiveData<>();

    public SearchViewModel(SearchUsersUseCase searchUsersUseCase, GetSearchHistoryUseCase getSearchHistoryUseCase, Logger logger,
                           ExecutorService ioExecutorService, ExecutorService mainExecutorService) {
        this.searchUsersUseCase = searchUsersUseCase;
        this.getSearchHistoryUseCase = getSearchHistoryUseCase;
        this.logger = logger;
        this.ioExecutorService = ioExecutorService;
        this.mainExecutorService = mainExecutorService;
    }

    public void load() {
        mainExecutorService.execute(() -> {
            shouldShowLoadingIndicator.setValue(true);

            ioExecutorService.execute(() -> {
                try {
                    List<SearchHistoryItem> history = getSearchHistoryUseCase.execute();

                    logger.debug(String.format("history: %s", history));

                    List<SearchHistoryItem> descSortedHistory = new ArrayList<>(history);
                    Collections.sort(descSortedHistory, (item1, item2) -> Integer.compare(item2.getId(), item1.getId()));

                    mainExecutorService.execute(() -> {
                        searchHistoryItems.setValue(descSortedHistory);
                        shouldShowHistory.setValue(true);
                    });
                } catch (Exception e) {
                    logger.warn("An error occurred while getting the search user history", e);

                    mainExecutorService.execute(() -> {
                        showErrorToastEvent.setValue(new Event<>(null));
                    });
                }

                mainExecutorService.execute(() -> {
                    shouldShowLoadingIndicator.setValue(false);
                });
            });
        });
    }

    public void onSearchButtonClick() {
        doSearch();
    }

    public void onResultItemClick(SearchUsersResult.Item resultItem) {
        mainExecutorService.execute(() -> {
            openUserDetailsActivityEvent.setValue(new Event<>(resultItem.getLogin()));
        });
    }

    public void onSearchHistoryItemClick(SearchHistoryItem historyItem) {
        mainExecutorService.execute(() -> {
            query.setValue(historyItem.getQuery());
            doSearch();
        });
    }

    private void doSearch() {
        shouldShowHistory.setValue(false);
        shouldShowLoadingIndicator.setValue(true);

        ioExecutorService.execute(() -> {
            try {
                SearchUsersResult result = searchUsersUseCase.execute(query.getValue());

                logger.debug(String.format("result: %s", result));

                mainExecutorService.execute(() -> {
                    searchResultItems.setValue(result.getItems());
                    shouldShowNoResultsText.setValue(result.getTotalCount() == 0);
                });
            } catch (Exception e) {
                logger.warn("An error occurred while searching users", e);

                mainExecutorService.execute(() -> {
                    shouldShowHistory.setValue(true);
                    showErrorToastEvent.setValue(new Event<>(null));
                });
            }

            mainExecutorService.execute(() -> {
                shouldShowLoadingIndicator.setValue(false);
            });
        });
    }

    public MutableLiveData<String> getQuery() {
        return query;
    }

    public LiveData<List<SearchUsersResult.Item>> getSearchResultItems() {
        return searchResultItems;
    }

    public LiveData<List<SearchHistoryItem>> getSearchHistoryItems() {
        return searchHistoryItems;
    }

    public LiveData<Boolean> shouldShowNoResultsText() {
        return shouldShowNoResultsText;
    }

    public LiveData<Boolean> shouldShowHistory() {
        return shouldShowHistory;
    }

    public LiveData<Boolean> shouldShowLoadingIndicator() {
        return shouldShowLoadingIndicator;
    }

    public LiveData<Event<Void>> getShowErrorToastEvent() {
        return showErrorToastEvent;
    }

    public LiveData<Event<String>> getOpenUserDetailsActivityEvent() {
        return openUserDetailsActivityEvent;
    }
}
