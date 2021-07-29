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
import static org.mockito.Mockito.when;

public class SearchViewModelTest {

    private SearchViewModel searchViewModel;

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
        searchViewModel = new SearchViewModel(
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

        searchViewModel.load();

        List<SearchHistoryItem> descSortedSearchHistory = new ArrayList<>(searchHistory);
        Collections.sort(descSortedSearchHistory, (item1, item2) -> Integer.compare(item2.getId(), item1.getId()));

        assertEquals(descSortedSearchHistory, searchViewModel.getSearchHistoryItems().getValue());
        assertEquals(true, searchViewModel.shouldShowHistory().getValue());
    }

    @Test
    public void testLoad_getSearchHistoryThrowsException() throws Exception {
        when(getSearchHistoryUseCase.execute()).thenThrow(new Exception());

        searchViewModel.load();

        assertNotNull(searchViewModel.getShowErrorToastEvent().getValue());
    }

    @Test
    public void testOnSearchButtonClick() throws Exception {
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

        searchViewModel.getQuery().setValue(query);
        searchViewModel.onSearchButtonClick();

        assertEquals(searchResult.getItems(), searchViewModel.getSearchResultItems().getValue());
    }

    @Test
    public void testOnSearchButtonClick_noResults() throws Exception {
        String query = "john";
        SearchUsersResult searchResult = new SearchUsersResult() {{
            setTotalCount(0);
            setItems(Collections.emptyList());
        }};

        when(searchUsersUseCase.execute(query)).thenReturn(searchResult);

        searchViewModel.getQuery().setValue(query);
        searchViewModel.onSearchButtonClick();

        assertEquals(true, searchViewModel.shouldShowNoResultsText().getValue());
    }

    @Test
    public void testOnSearchButtonClick_searchUsersThrowsException() throws Exception {
        String query = "john";

        when(searchUsersUseCase.execute(query)).thenThrow(new Exception());

        searchViewModel.getQuery().setValue(query);
        searchViewModel.onSearchButtonClick();

        assertNotNull(searchViewModel.getShowErrorToastEvent().getValue());
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
        searchViewModel.onSearchButtonClick();

        searchViewModel.onResultItemClick(searchResultItem);

        assertEquals(searchResultItem.getLogin(), searchViewModel.getOpenUserDetailsActivityEvent().getValue().getContentIfNotHandled());
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

        searchViewModel.onSearchHistoryItemClick(searchHistoryItem);

        assertEquals(query, searchViewModel.getQuery().getValue());
        assertEquals(searchResult.getItems(), searchViewModel.getSearchResultItems().getValue());
    }
}