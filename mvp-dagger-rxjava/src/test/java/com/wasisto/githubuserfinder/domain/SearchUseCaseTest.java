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

package com.wasisto.githubuserfinder.domain;

import com.wasisto.githubuserfinder.data.github.GithubDataSource;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;
import io.reactivex.Completable;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchUseCaseTest {

    @Test
    public void execute() {
        GithubDataSource githubDataSourceMock = mock(GithubDataSource.class);
        SearchHistoryDataSource searchHistoryDataSourceMock = mock(SearchHistoryDataSource.class);
        LoggingHelper loggingHelperMock = mock(LoggingHelper.class);

        when(searchHistoryDataSourceMock.add(any())).thenReturn(Completable.complete());

        SearchUseCase searchUseCase = new SearchUseCase(githubDataSourceMock, searchHistoryDataSourceMock,
                loggingHelperMock);

        String query = "foo";

        searchUseCase.execute(query);

        verify(githubDataSourceMock).searchUser(query);

        ArgumentCaptor<SearchHistoryItem> searchHistoryItemCaptor = ArgumentCaptor.forClass(SearchHistoryItem.class);
        verify(searchHistoryDataSourceMock).add(searchHistoryItemCaptor.capture());
        assertEquals(query, searchHistoryItemCaptor.getValue().getQuery());
    }
}