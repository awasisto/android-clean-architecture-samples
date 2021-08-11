package com.wasisto.githubuserfinder.android

import android.app.Application
import com.wasisto.githubuserfinder.android.data.github.RetrofitGithubDataSource
import com.wasisto.githubuserfinder.android.data.logger.AndroidLogger
import com.wasisto.githubuserfinder.android.data.searchhistory.SqliteSearchHistoryDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KClass

class DefaultServiceLocator private constructor(application: Application) : ServiceLocator {

    companion object {

        private var instance: DefaultServiceLocator? = null

        fun getInstance(application: Application) = instance ?: DefaultServiceLocator(application).also { instance = it }
    }

    override val githubDataSource by lazy { RetrofitGithubDataSource() }

    override val searchHistoryDataSource by lazy { SqliteSearchHistoryDataSource(application) }

    override val ioDispatcher = Dispatchers.IO

    override val mainDispatcher = Dispatchers.Main.immediate

    private val loggers = mutableMapOf<KClass<*>, AndroidLogger>()

    override fun getLogger(cls: KClass<*>) = loggers[cls] ?: AndroidLogger(cls).also { loggers[cls] = it }
}