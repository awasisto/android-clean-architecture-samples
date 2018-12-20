package com.wasisto.githubuserfinder.domain;

import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.util.scheduler.SchedulerProvider;
import com.wasisto.githubuserfinder.util.scheduler.TestSchedulerProvider;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GetSearchHistoryUseCaseTest {

    @Test
    public void createUseCaseObservable() {
        SchedulerProvider testSchedulerProvider = new TestSchedulerProvider();
        SearchHistoryDataSource searchHistoryDataSourceMock = mock(SearchHistoryDataSource.class);

        GetSearchHistoryUseCase getSearchHistoryUseCase = new GetSearchHistoryUseCase(testSchedulerProvider,
                searchHistoryDataSourceMock);

        getSearchHistoryUseCase.createUseCaseObservable(null);

        verify(searchHistoryDataSourceMock).getAll();
    }
}