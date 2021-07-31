package com.wasisto.githubuserfinder.android.ui.search;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.common.util.concurrent.MoreExecutors;
import com.wasisto.githubuserfinder.android.NoOpLogger;
import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase;
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchPresenterTest {

    private SearchPresenter searchPresenter;

    @Mock
    private SearchView searchView;

    @Mock
    private SearchUsersUseCase searchUsersUseCase;

    @Mock
    private GetSearchHistoryUseCase getSearchHistoryUseCase;

    private Logger noOpLogger = new NoOpLogger();

    private ExecutorService directExecutorService = MoreExecutors.newDirectExecutorService();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        searchPresenter = new SearchPresenter(
                searchView,
                searchUsersUseCase,
                getSearchHistoryUseCase,
                noOpLogger,
                directExecutorService,
                directExecutorService
        );
    }

    @Test
    public void testLoad() throws Exception {
        List<SearchHistoryItem> searchHistory = Arrays.asList(
                new SearchHistoryItem() {{
                    setId(1);
                    setQuery("john");
                }},
                new SearchHistoryItem() {{
                    setId(2);
                    setQuery("jane");
                }}
        );

        when(getSearchHistoryUseCase.execute()).thenReturn(searchHistory);

        searchPresenter.load();

        List<SearchHistoryItem> descSortedSearchHistory = new ArrayList<>(searchHistory);
        Collections.sort(descSortedSearchHistory, (item1, item2) -> Integer.compare(item2.getId(), item1.getId()));

        verify(searchView).showSearchHistory(descSortedSearchHistory);
    }

    @Test
    public void testLoad_getSearchHistoryThrowsException() throws Exception {
        when(getSearchHistoryUseCase.execute()).thenThrow(new Exception());

        searchPresenter.load();

        verify(searchView).showErrorToast();
    }

    @Test
    public void testSearch() throws Exception {
        String query = "john";
        SearchUsersResult searchResult = new SearchUsersResult() {{
            setTotalCount(2);
            setItems(Arrays.asList(
                    new SearchUsersResult.Item() {{
                        setLogin("johnsmith");
                    }},
                    new SearchUsersResult.Item() {{
                        setLogin("johndoe");
                    }}
            ));
        }};

        when(searchUsersUseCase.execute(query)).thenReturn(searchResult);

        searchPresenter.search(query);

        verify(searchView).showSearchResult(searchResult.getItems());
    }

    @Test
    public void testSearch_noResults() throws Exception {
        String query = "john";
        SearchUsersResult searchResult = new SearchUsersResult() {{
            setTotalCount(0);
            setItems(Collections.emptyList());
        }};

        when(searchUsersUseCase.execute(query)).thenReturn(searchResult);

        searchPresenter.search(query);

        verify(searchView).showNoResultsText();
    }

    @Test
    public void testSearch_searchUsersThrowsException() throws Exception {
        String query = "john";

        when(searchUsersUseCase.execute(query)).thenThrow(new Exception());

        searchPresenter.search(query);

        verify(searchView).showErrorToast();
    }

    @Test
    public void testOnResultItemClick() throws Exception {
        String query = "john";
        SearchUsersResult.Item searchResultItem = new SearchUsersResult.Item() {{
            setLogin("johnsmith");
        }};
        when(searchUsersUseCase.execute(query)).thenReturn(
                new SearchUsersResult() {{
                    setTotalCount(1);
                    setItems(Arrays.asList(searchResultItem));
                }}
        );
        searchPresenter.search(query);

        searchPresenter.onResultItemClick(searchResultItem);

        verify(searchView).openUserDetailsActivity(searchResultItem.getLogin());
    }

    @Test
    public void testOnSearchHistoryItemClick() throws Exception {
        String query = "john";
        SearchHistoryItem searchHistoryItem = new SearchHistoryItem() {{
            setId(1);
            setQuery(query);
        }};
        SearchUsersResult searchResult = new SearchUsersResult() {{
            setTotalCount(1);
            setItems(Arrays.asList(
                    new SearchUsersResult.Item() {{
                        setLogin("johnsmith");
                    }},
                    new SearchUsersResult.Item() {{
                        setLogin("johndoe");
                    }}
            ));
        }};
        when(getSearchHistoryUseCase.execute()).thenReturn(Arrays.asList(searchHistoryItem));
        when(searchUsersUseCase.execute(query)).thenReturn(searchResult);

        searchPresenter.onSearchHistoryItemClick(searchHistoryItem);

        verify(searchView).setQuery(query);
        verify(searchView).showSearchResult(searchResult.getItems());
    }
}