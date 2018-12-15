/*
 * Copyright (c) 2018 Andika Wasisto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wasisto.githubuserfinder.di;

import android.app.Application;
import android.content.Context;
import com.wasisto.githubuserfinder.data.github.GithubDataSource;
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSourceImpl;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl;
import com.wasisto.githubuserfinder.util.scheduler.SchedulerProvider;
import com.wasisto.githubuserfinder.util.scheduler.SchedulerProviderImpl;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public abstract class ApplicationModule {

    @Singleton
    @Binds
    public abstract Context bindContext(Application application);

    @Singleton
    @Binds
    public abstract GithubDataSource bindGithubDataSource(GithubDataSourceImpl githubDataSource);

    @Singleton
    @Binds
    public abstract SearchHistoryDataSource bindSearchHistoryDataSource(SearchHistoryDataSourceImpl searchHistoryDataSource);

    @Singleton
    @Binds
    public abstract SchedulerProvider bindSchedulerProvider(SchedulerProviderImpl schedulerProvider);

    @Singleton
    @Binds
    public abstract LoggingHelper bindLoggingHelper(LoggingHelperImpl loggingHelper);
}
