/*
 * Copyright (c) 2019 Andika Wasisto
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

package com.wasisto.githubuserfinder.ui.search;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase;
import com.wasisto.githubuserfinder.domain.SearchUseCase;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private SearchUseCase searchUseCase;

    private GetHistoryUseCase getHistoryUseCase;

    private LoggingHelper loggingHelper;

    public SearchViewModelFactory(SearchUseCase searchUseCase, GetHistoryUseCase getHistoryUseCase,
                                  LoggingHelper loggingHelper) {
        this.searchUseCase = searchUseCase;
        this.getHistoryUseCase = getHistoryUseCase;
        this.loggingHelper = loggingHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // noinspection unchecked
        return (T) new SearchViewModel(searchUseCase, getHistoryUseCase, loggingHelper);
    }
}
