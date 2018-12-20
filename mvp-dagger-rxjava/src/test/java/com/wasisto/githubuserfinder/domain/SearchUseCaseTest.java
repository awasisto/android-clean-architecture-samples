package com.wasisto.githubuserfinder.domain;

import com.wasisto.githubuserfinder.data.github.GithubDataSource;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;
import com.wasisto.githubuserfinder.util.scheduler.SchedulerProvider;
import com.wasisto.githubuserfinder.util.scheduler.TestSchedulerProvider;
import io.reactivex.Completable;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchUseCaseTest {

    @Test
    public void createUseCaseObservable() {
        SchedulerProvider testSchedulerProvider = new TestSchedulerProvider();
        GithubDataSource githubDataSourceMock = mock(GithubDataSource.class);
        SearchHistoryDataSource searchHistoryDataSourceMock = mock(SearchHistoryDataSource.class);
        LoggingHelper loggingHelperMock = mock(LoggingHelper.class);

        when(searchHistoryDataSourceMock.add(any())).thenReturn(Completable.complete());

        SearchUseCase searchUseCase = new SearchUseCase(testSchedulerProvider, githubDataSourceMock,
                searchHistoryDataSourceMock, loggingHelperMock);

        String query = "foo";

        searchUseCase.createUseCaseObservable(query);

        verify(githubDataSourceMock).searchUser(query);

        ArgumentCaptor<SearchHistoryItem> searchHistoryItemCaptor = ArgumentCaptor.forClass(SearchHistoryItem.class);
        verify(searchHistoryDataSourceMock).add(searchHistoryItemCaptor.capture());
        assertEquals(query, searchHistoryItemCaptor.getValue().getQuery());
    }
}