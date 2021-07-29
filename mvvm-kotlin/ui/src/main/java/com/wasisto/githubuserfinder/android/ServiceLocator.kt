package com.wasisto.githubuserfinder.android

import com.wasisto.githubuserfinder.common.Logger
import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.reflect.KClass

interface ServiceLocator {

    val githubDataSource: GithubDataSource

    val searchHistoryDataSource: SearchHistoryDataSource

    val ioDispatcher: CoroutineDispatcher

    val mainDispatcher: CoroutineDispatcher

    fun getLogger(cls: KClass<*>): Logger
}