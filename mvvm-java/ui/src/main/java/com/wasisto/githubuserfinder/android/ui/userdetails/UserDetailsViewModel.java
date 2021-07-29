package com.wasisto.githubuserfinder.android.ui.userdetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wasisto.githubuserfinder.android.ui.common.Event;
import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.models.User;
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase;
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase;
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase;

import java.util.concurrent.ExecutorService;

public class UserDetailsViewModel extends ViewModel {

    private GetUserUseCase getUserUseCase;

    private Logger logger;

    private ExecutorService ioExecutorService;

    private ExecutorService mainExecutorService;

    private MutableLiveData<User> user = new MutableLiveData<>();

    private MutableLiveData<Boolean> shouldShowLoadingIndicator = new MutableLiveData<>();

    private MutableLiveData<Event<String>> openBrowserEvent = new MutableLiveData<>();

    private MutableLiveData<Event<Void>> showErrorToastEvent = new MutableLiveData<>();

    private MutableLiveData<Event<Void>> closeActivityEvent = new MutableLiveData<>();

    public UserDetailsViewModel(GetUserUseCase getUserUseCase, Logger logger, ExecutorService ioExecutorService,
                                ExecutorService mainExecutorService) {
        this.getUserUseCase = getUserUseCase;
        this.logger = logger;
        this.ioExecutorService = ioExecutorService;
        this.mainExecutorService = mainExecutorService;
    }

    public void load(String username) {
        mainExecutorService.execute(() -> {
            shouldShowLoadingIndicator.setValue(true);

            ioExecutorService.execute(() -> {
                try {
                    User retrievedUser = getUserUseCase.execute(username);

                    logger.debug(String.format("retrievedUser: %s", retrievedUser));

                    mainExecutorService.execute(() -> {
                        shouldShowLoadingIndicator.setValue(false);
                        user.setValue(retrievedUser);
                    });
                } catch (Exception e) {
                    logger.warn("An error occurred while getting user details", e);

                    mainExecutorService.execute(() -> {
                        showErrorToastEvent.setValue(new Event<>(null));
                        closeActivityEvent.setValue(new Event<>(null));
                    });
                }
            });
        });
    }

    public void onBlogClick() {
        if (user.getValue() != null && user.getValue().getBlog() != null) {
            mainExecutorService.execute(() -> {
                openBrowserEvent.setValue(new Event<>(null));
            });
        }
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Boolean> shouldShowLoadingIndicator() {
        return shouldShowLoadingIndicator;
    }

    public LiveData<Event<String>> getOpenBrowserEvent() {
        return openBrowserEvent;
    }

    public LiveData<Event<Void>> getShowErrorToastEvent() {
        return showErrorToastEvent;
    }

    public LiveData<Event<Void>> getCloseActivityEvent() {
        return closeActivityEvent;
    }
}
