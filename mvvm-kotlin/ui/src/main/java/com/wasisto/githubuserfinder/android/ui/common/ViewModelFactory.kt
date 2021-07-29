package com.wasisto.githubuserfinder.android.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wasisto.githubuserfinder.android.ServiceLocator
import com.wasisto.githubuserfinder.android.ui.search.SearchViewModel
import com.wasisto.githubuserfinder.android.ui.userdetails.UserDetailsViewModel
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase

class ViewModelFactory(private val serviceLocator: ServiceLocator) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(SearchViewModel::class.java) ->
                    SearchViewModel(
                        SearchUsersUseCase(
                            serviceLocator.githubDataSource,
                            serviceLocator.searchHistoryDataSource,
                            serviceLocator.getLogger(SearchUsersUseCase::class)
                        ),
                        GetSearchHistoryUseCase(
                            serviceLocator.searchHistoryDataSource
                        ),
                        serviceLocator.getLogger(SearchViewModel::class),
                        serviceLocator.ioDispatcher,
                        serviceLocator.mainDispatcher
                    )
                isAssignableFrom(UserDetailsViewModel::class.java) ->
                    UserDetailsViewModel(
                        GetUserUseCase(
                            serviceLocator.githubDataSource
                        ),
                        serviceLocator.getLogger(UserDetailsViewModel::class),
                        serviceLocator.ioDispatcher,
                        serviceLocator.mainDispatcher
                    )
                else -> throw IllegalArgumentException("unknown model class $modelClass")
            }
        } as T
}