package com.wasisto.githubuserfinder.domain.usecases;

import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.NoOpLogger;
import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource;
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchUsersUseCaseTest {

    private SearchUsersUseCase searchUsersUseCase;

    @Mock
    private GithubDataSource githubDataSource;

    @Mock
    private SearchHistoryDataSource searchHistoryDataSource;

    private Logger noOpLogger = new NoOpLogger();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        searchUsersUseCase = new SearchUsersUseCase(githubDataSource, searchHistoryDataSource, noOpLogger);
    }

    @Test
    public void testExecute() throws Exception {
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

        when(githubDataSource.searchUsers(query)).thenReturn(searchResult);

        assertEquals(searchResult, searchUsersUseCase.execute(query));

        ArgumentCaptor<SearchHistoryItem> searchHistoryItemArgumentCaptor = ArgumentCaptor.forClass(SearchHistoryItem.class);
        verify(searchHistoryDataSource).add(searchHistoryItemArgumentCaptor.capture());
        assertEquals(query, searchHistoryItemArgumentCaptor.getValue().getQuery());
    }

    @Test
    public void testExecute_addSearchHistoryItemThrowsException() throws Exception {
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

        when(githubDataSource.searchUsers(query)).thenReturn(searchResult);
        doThrow(new Exception()).when(searchHistoryDataSource).add(any());

        assertEquals(searchResult, searchUsersUseCase.execute(query));

        ArgumentCaptor<SearchHistoryItem> searchHistoryItemArgumentCaptor = ArgumentCaptor.forClass(SearchHistoryItem.class);
        verify(searchHistoryDataSource).add(searchHistoryItemArgumentCaptor.capture());
        assertEquals(query, searchHistoryItemArgumentCaptor.getValue().getQuery());
    }
}