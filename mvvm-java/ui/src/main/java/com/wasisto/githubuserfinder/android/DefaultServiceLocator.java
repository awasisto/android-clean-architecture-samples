package com.wasisto.githubuserfinder.android;

import android.app.Application;

import com.wasisto.githubuserfinder.android.data.github.RetrofitGithubDataSource;
import com.wasisto.githubuserfinder.android.data.logger.AndroidLogger;
import com.wasisto.githubuserfinder.android.data.searchhistory.SqliteSearchHistoryDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultServiceLocator implements ServiceLocator {

    private static DefaultServiceLocator instance;

    private Application application;

    private SqliteSearchHistoryDataSource sqliteSearchHistoryDataSource;

    private RetrofitGithubDataSource retrofitGithubDataSource;

    private ExecutorService ioExecutorService;

    private ExecutorService mainExecutorService;

    private Map<Class<?>, AndroidLogger> loggers = new HashMap<>();

    public static DefaultServiceLocator getInstance(Application application) {
        if (instance == null) {
            instance = new DefaultServiceLocator(application);
        }
        return instance;
    }

    public DefaultServiceLocator(Application application) {
        this.application = application;
    }

    @Override
    public RetrofitGithubDataSource getGithubDataSource() {
        if (retrofitGithubDataSource == null) {
            retrofitGithubDataSource = new RetrofitGithubDataSource();
        }
        return retrofitGithubDataSource;
    }

    @Override
    public SqliteSearchHistoryDataSource getSearchHistoryDataSource() {
        if (sqliteSearchHistoryDataSource == null) {
            sqliteSearchHistoryDataSource = new SqliteSearchHistoryDataSource(application);
        }
        return sqliteSearchHistoryDataSource;
    }

    @Override
    public ExecutorService getIoExecutorService() {
        if (ioExecutorService == null) {
            ioExecutorService = Executors.newCachedThreadPool();
        }
        return ioExecutorService;
    }

    @Override
    public ExecutorService getMainExecutorService() {
        if (mainExecutorService == null) {
            mainExecutorService = new MainThreadExecutorService();
        }
        return mainExecutorService;
    }

    @Override
    public AndroidLogger getLogger(Class<?> cls) {
        AndroidLogger logger = loggers.get(cls);
        if (logger == null) {
            loggers.put(cls, logger = new AndroidLogger(cls));
        }
        return logger;
    }
}
