package com.wasisto.githubuserfinder.domain.usecases;

import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource;
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.domain.models.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class GetUserUseCaseTest {

    private GetUserUseCase getUserUseCase;

    @Mock
    private GithubDataSource githubDataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        getUserUseCase = new GetUserUseCase(githubDataSource);
    }

    @Test
    public void testExecute() throws Exception {
        String username = "octocat";
        User user = new User() {{
            setLogin(username);
            setAvatarUrl("https://avatars.githubusercontent.com/u/583231?v=4");
            setBlog("https://github.blog");
        }};

        when(githubDataSource.getUser(username)).thenReturn(user);

        assertEquals(user, getUserUseCase.execute(username));
    }
}