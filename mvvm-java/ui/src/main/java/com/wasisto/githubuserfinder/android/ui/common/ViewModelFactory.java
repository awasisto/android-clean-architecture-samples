package com.wasisto.githubuserfinder.android.ui.common;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.wasisto.githubuserfinder.android.ServiceLocator;
import com.wasisto.githubuserfinder.android.ui.search.SearchViewModel;
import com.wasisto.githubuserfinder.android.ui.userdetails.UserDetailsViewModel;
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase;
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase;
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ServiceLocator serviceLocator;

    public ViewModelFactory(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(
                    new SearchUsersUseCase(
                            serviceLocator.getGithubDataSource(),
                            serviceLocator.getSearchHistoryDataSource(),
                            serviceLocator.getLogger(SearchUsersUseCase.class)
                    ),
                    new GetSearchHistoryUseCase(
                            serviceLocator.getSearchHistoryDataSource()
                    ),
                    serviceLocator.getLogger(SearchViewModel.class),
                    serviceLocator.getIoExecutorService(),
                    serviceLocator.getMainExecutorService()
            );
        } else if (modelClass.isAssignableFrom(UserDetailsViewModel.class)) {
            return (T) new UserDetailsViewModel(
                    new GetUserUseCase(
                            serviceLocator.getGithubDataSource()
                    ),
                    serviceLocator.getLogger(UserDetailsViewModel.class),
                    serviceLocator.getIoExecutorService(),
                    serviceLocator.getMainExecutorService()
            );
        }
        throw new IllegalArgumentException("unknown model class $modelClass");
    }
}
