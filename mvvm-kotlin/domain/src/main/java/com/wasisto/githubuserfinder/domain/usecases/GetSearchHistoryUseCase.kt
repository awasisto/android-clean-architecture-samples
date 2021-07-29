package com.wasisto.githubuserfinder.domain.usecases

import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource

class GetSearchHistoryUseCase(private val searchHistoryDataSource: SearchHistoryDataSource) {
    operator fun invoke() = searchHistoryDataSource.getAll()
}