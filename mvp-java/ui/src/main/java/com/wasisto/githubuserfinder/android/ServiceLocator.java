package com.wasisto.githubuserfinder.android;

import androidx.lifecycle.ViewModelProvider;

import com.wasisto.githubuserfinder.common.Logger;
import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource;
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource;

import java.util.concurrent.ExecutorService;

public interface ServiceLocator {

    GithubDataSource getGithubDataSource();

    SearchHistoryDataSource getSearchHistoryDataSource();

    ExecutorService getIoExecutorService();

    ExecutorService getMainExecutorService();

    Logger getLogger(Class<?> cls);
}
