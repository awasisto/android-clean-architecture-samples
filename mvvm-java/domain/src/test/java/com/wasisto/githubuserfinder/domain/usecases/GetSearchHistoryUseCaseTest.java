package com.wasisto.githubuserfinder.domain.usecases;

import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class GetSearchHistoryUseCaseTest {

    private GetSearchHistoryUseCase getSearchHistoryUseCase;

    @Mock
    private SearchHistoryDataSource searchHistoryDataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        getSearchHistoryUseCase = new GetSearchHistoryUseCase(searchHistoryDataSource);
    }

    @Test
    public void testExecute() throws Exception {
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

        when(searchHistoryDataSource.getAll()).thenReturn(searchHistory);

        assertEquals(searchHistory, getSearchHistoryUseCase.execute());
    }
}