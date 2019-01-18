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

package com.wasisto.githubuserfinder.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.*;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSourceImpl;
import com.wasisto.githubuserfinder.databinding.ActivitySearchBinding;
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase;
import com.wasisto.githubuserfinder.domain.SearchUseCase;
import com.wasisto.githubuserfinder.ui.userdetails.UserDetailsActivity;
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchViewModelFactory viewModelFactory =
                new SearchViewModelFactory(
                        new SearchUseCase(
                                GithubDataSourceImpl.getInstance(this),
                                SearchHistoryDataSourceImpl.getInstance(this),
                                LoggingHelperImpl.getInstance()
                        ),
                        new GetHistoryUseCase(
                                SearchHistoryDataSourceImpl.getInstance(this)
                        ),
                        LoggingHelperImpl.getInstance()
                );

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        HistoryAdapter historyAdapter = new HistoryAdapter(new ArrayList<>(), viewModel);
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setAdapter(historyAdapter);

        ResultAdapter resultAdapter = new ResultAdapter(new ArrayList<>(), viewModel);
        binding.resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.resultRecyclerView.setAdapter(resultAdapter);

        viewModel.getOpenUserDetailsActivityEvent().observe(this, event -> {
            if (event != null) {
                Intent intent = new Intent(this, UserDetailsActivity.class);
                intent.putExtra(UserDetailsActivity.EXTRA_USERNAME, event.getData());

                startActivity(intent);
            }
        });

        viewModel.getShowToastEvent().observe(this, event -> {
            if (event != null) {
                Toast.makeText(SearchActivity.this, event.getData(), LENGTH_SHORT).show();
            }
        });
    }
}
