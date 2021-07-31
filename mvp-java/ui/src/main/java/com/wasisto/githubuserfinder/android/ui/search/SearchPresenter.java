package com.wasisto.githubuserfinder.android.ui.search;

import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase;
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SearchPresenter {

    private SearchView view;

    private SearchUsersUseCase searchUsersUseCase;

    private GetSearchHistoryUseCase getSearchHistoryUseCase;

    private Logger logger;

    private ExecutorService ioExecutorService;

    private ExecutorService mainExecutorService;

    private List<SearchHistoryItem> descSortedHistory;

    public SearchPresenter(SearchView view, SearchUsersUseCase searchUsersUseCase, GetSearchHistoryUseCase getSearchHistoryUseCase,
                           Logger logger, ExecutorService ioExecutorService, ExecutorService mainExecutorService) {
        this.view = view;
        this.getSearchHistoryUseCase = getSearchHistoryUseCase;
        this.searchUsersUseCase = searchUsersUseCase;
        this.logger = logger;
        this.ioExecutorService = ioExecutorService;
        this.mainExecutorService = mainExecutorService;
    }

    public void load() {
        mainExecutorService.execute(() -> {
            view.showLoadingIndicator();

            ioExecutorService.execute(() -> {
                try {
                    List<SearchHistoryItem> history = getSearchHistoryUseCase.execute();

                    logger.debug(String.format("history: %s", history));

                    descSortedHistory = new ArrayList<>(history);
                    Collections.sort(this.descSortedHistory, (item1, item2) -> Integer.compare(item2.getId(), item1.getId()));

                    mainExecutorService.execute(() -> {
                        view.showSearchHistory(descSortedHistory);
                    });
                } catch (Exception e) {
                    logger.warn("An error occurred while getting the search user history", e);

                    mainExecutorService.execute(() -> {
                        view.showErrorToast();
                    });
                }

                mainExecutorService.execute(() -> {
                    view.hideLoadingIndicator();
                });
            });
        });
    }

    public void search(String username) {
        mainExecutorService.execute(() -> {
            view.hideSearchHistory();
            view.showLoadingIndicator();

            ioExecutorService.execute(() -> {
                try {
                    SearchUsersResult result = searchUsersUseCase.execute(username);

                    logger.debug(String.format("result: %s", result));

                    mainExecutorService.execute(() -> {
                        if (result.getTotalCount() == 0) {
                            view.showNoResultsText();
                        } else {
                            view.hideNoResultsText();
                            view.showSearchResult(result.getItems());
                        }
                    });
                } catch (Exception e) {
                    logger.warn("An error occurred while searching users", e);

                    mainExecutorService.execute(() -> {
                        view.showSearchHistory(descSortedHistory);
                        view.showErrorToast();
                    });
                }

                mainExecutorService.execute(() -> {
                    view.hideLoadingIndicator();
                });
            });
        });
    }

    public void onResultItemClick(SearchUsersResult.Item resultItem) {
        mainExecutorService.execute(() -> {
            view.openUserDetailsActivity(resultItem.getLogin());
        });
    }

    public void onSearchHistoryItemClick(SearchHistoryItem historyItem) {
        mainExecutorService.execute(() -> {
            String query = historyItem.getQuery();
            view.setQuery(query);
            search(query);
        });
    }
}
