package com.wasisto.githubuserfinder.domain;

import com.wasisto.githubuserfinder.data.github.GithubDataSource;
import com.wasisto.githubuserfinder.util.scheduler.SchedulerProvider;
import com.wasisto.githubuserfinder.util.scheduler.TestSchedulerProvider;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GetUserUseCaseTest {

    @Test
    public void createUseCaseObservable() {
        SchedulerProvider testSchedulerProvider = new TestSchedulerProvider();
        GithubDataSource githubDataSourceMock = mock(GithubDataSource.class);

        GetUserUseCase getUserUseCase = new GetUserUseCase(testSchedulerProvider, githubDataSourceMock);

        String username = "foo";

        getUserUseCase.createUseCaseObservable(username);

        verify(githubDataSourceMock).getUser(username);
    }
}
