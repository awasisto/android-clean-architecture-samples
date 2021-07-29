package com.wasisto.githubuserfinder.android.ui.userdetails;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.common.util.concurrent.MoreExecutors;
import com.wasisto.githubuserfinder.android.NoOpLogger;
import com.wasisto.githubuserfinder.android.ui.search.SearchViewModel;
import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.models.User;
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase;
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserDetailsViewModelTest {

    private UserDetailsViewModel userDetailsViewModel;

    @Mock
    private GetUserUseCase getUserUseCase;

    private Logger noOpLogger = new NoOpLogger();

    private ExecutorService directExecutorService = MoreExecutors.newDirectExecutorService();

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userDetailsViewModel = new UserDetailsViewModel(
                getUserUseCase,
                noOpLogger,
                directExecutorService,
                directExecutorService
        );
    }

    @Test
    public void testLoad() throws Exception {
        String username = "octocat";
        User user = new User() {{
            setLogin(username);
            setAvatarUrl("https://avatars.githubusercontent.com/u/583231?v=4");
            setBlog("https://github.blog");
        }};

        when(getUserUseCase.execute(username)).thenReturn(user);

        userDetailsViewModel.load(username);

        assertEquals(user, userDetailsViewModel.getUser().getValue());
    }

    @Test
    public void testLoad_getUserThrowsException() throws Exception {
        String username = "octocat";

        when(getUserUseCase.execute(username)).thenThrow(new Exception());

        userDetailsViewModel.load(username);

        assertNotNull(userDetailsViewModel.getShowErrorToastEvent().getValue());
        assertNotNull(userDetailsViewModel.getCloseActivityEvent().getValue());
    }

    @Test
    public void testOnBlogClick() throws Exception {
        String username = "octocat";
        User user = new User() {{
            setLogin(username);
            setAvatarUrl("https://avatars.githubusercontent.com/u/583231?v=4");
            setBlog("https://github.blog");
        }};
        when(getUserUseCase.execute(username)).thenReturn(user);
        userDetailsViewModel.load(username);

        userDetailsViewModel.onBlogClick();

        assertNotNull(userDetailsViewModel.getOpenBrowserEvent().getValue());
    }
}