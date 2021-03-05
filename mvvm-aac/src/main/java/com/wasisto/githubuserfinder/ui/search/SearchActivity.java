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

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSourceImpl;
import com.wasisto.githubuserfinder.databinding.ActivitySearchBinding;
import com.wasisto.githubuserfinder.usecase.GetHistoryUseCase;
import com.wasisto.githubuserfinder.usecase.SearchUseCase;
import com.wasisto.githubuserfinder.ui.userdetails.UserDetailsActivity;
import com.wasisto.githubuserfinder.util.executor.ExecutorProviderImpl;
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchViewModelFactory viewModelFactory =
                new SearchViewModelFactory(
                        new SearchUseCase(
                                ExecutorProviderImpl.getInstance(),
                                GithubDataSourceImpl.getInstance(this),
                                SearchHistoryDataSourceImpl.getInstance(this),
                                LoggingHelperImpl.getInstance()
                        ),
                        new GetHistoryUseCase(
                                ExecutorProviderImpl.getInstance(),
                                SearchHistoryDataSourceImpl.getInstance(this)
                        ),
                        LoggingHelperImpl.getInstance()
                );

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);

        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        viewModel.getOpenUserDetailsActivityEvent().observe(this, event -> {
            if (!event.hasBeenHandled()) {
                Intent intent = new Intent(this, UserDetailsActivity.class);
                intent.putExtra(UserDetailsActivity.EXTRA_USERNAME, event.getContentIfNotHandled());

                startActivity(intent);
            }
        });

        viewModel.getShowToastEvent().observe(this, event -> {
            if (!event.hasBeenHandled()) {
                Toast.makeText(SearchActivity.this, event.getContentIfNotHandled(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
