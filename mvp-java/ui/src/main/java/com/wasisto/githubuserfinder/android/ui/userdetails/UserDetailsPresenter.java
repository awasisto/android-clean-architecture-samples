package com.wasisto.githubuserfinder.android.ui.userdetails;

import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.models.User;
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase;

import java.util.concurrent.ExecutorService;

public class UserDetailsPresenter {

    private UserDetailsView view;

    private GetUserUseCase getUserUseCase;

    private Logger logger;

    private ExecutorService ioExecutorService;

    private ExecutorService mainExecutorService;

    private User user;

    public UserDetailsPresenter(UserDetailsView view, GetUserUseCase getUserUseCase, Logger logger,
                                ExecutorService ioExecutorService, ExecutorService mainExecutorService) {
        this.view = view;
        this.getUserUseCase = getUserUseCase;
        this.logger = logger;
        this.ioExecutorService = ioExecutorService;
        this.mainExecutorService = mainExecutorService;
    }

    public void load(String username) {
        mainExecutorService.execute(() -> {
            view.showLoadingIndicator();

            ioExecutorService.execute(() -> {
                try {
                    user = getUserUseCase.execute(username);

                    logger.debug(String.format("user: %s", user));

                    mainExecutorService.execute(() -> {
                        view.hideLoadingIndicator();
                        view.showUser(user);
                    });
                } catch (Exception e) {
                    logger.warn("An error occurred while getting user details", e);

                    mainExecutorService.execute(() -> {
                        view.showErrorToast();
                        view.closeActivity();
                    });
                }
            });
        });
    }

    public void onBlogClick() {
        mainExecutorService.execute(() -> {
            if (user != null && user.getBlog() != null) {
                view.openBrowser(user.getBlog());
            }
        });
    }
}
