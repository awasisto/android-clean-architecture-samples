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

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.*;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl;
import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSourceImpl;
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase;
import com.wasisto.githubuserfinder.domain.SearchUseCase;
import com.wasisto.githubuserfinder.ui.userdetails.UserDetailsActivity;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.wasisto.githubuserfinder.data.Resource.Status.ERROR;
import static com.wasisto.githubuserfinder.data.Resource.Status.LOADING;
import static com.wasisto.githubuserfinder.data.Resource.Status.SUCCESS;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private SearchViewModel viewModel;

    private ProgressBar loadingIndicator;

    private EditText queryEditText;

    private Button searchButton;

    private RecyclerView resultRecyclerView;

    private RecyclerView historyRecyclerView;

    private TextView noResultsTextView;

    private HistoryAdapter historyAdapter;

    private ResultAdapter resultAdapter;

    private LoggingHelper loggingHelper = LoggingHelperImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        viewModel =
                ViewModelProviders.of(
                        this,
                        new ViewModelProvider.Factory() {
                            @NonNull
                            @Override
                            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                                // noinspection unchecked
                                return (T) new SearchViewModel(
                                        new SearchUseCase(
                                                GithubDataSourceImpl.getInstance(SearchActivity.this),
                                                SearchHistoryDataSourceImpl.getInstance(SearchActivity.this),
                                                LoggingHelperImpl.getInstance()
                                        ),
                                        new GetHistoryUseCase(
                                                SearchHistoryDataSourceImpl.getInstance(SearchActivity.this)
                                        )
                                );
                            }
                        }
                ).get(SearchViewModel.class);

        loadingIndicator = findViewById(R.id.loadingIndicator);
        queryEditText = findViewById(R.id.queryEditText);
        searchButton = findViewById(R.id.searchButton);
        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        noResultsTextView = findViewById(R.id.noResultsTextView);

        historyAdapter = new HistoryAdapter(new ArrayList<>());
        historyAdapter.setOnItemClickListener(historyItem -> viewModel.onSearch(historyItem.getQuery()));

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);

        resultAdapter = new ResultAdapter(new ArrayList<>());
        resultAdapter.setOnItemClickListener(resultItem -> {
            Intent intent = new Intent(this, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.EXTRA_USERNAME, resultItem.getLogin());

            startActivity(intent);
        });

        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultRecyclerView.setAdapter(resultAdapter);

        searchButton.setOnClickListener(v -> viewModel.onSearch(queryEditText.getText().toString()));

        viewModel.getHistory().observe(this, resource -> {
            if (resource != null) {
                if (resource.status == LOADING) {
                    loadingIndicator.setVisibility(VISIBLE);
                    resultRecyclerView.setVisibility(GONE);
                    historyRecyclerView.setVisibility(GONE);
                    noResultsTextView.setVisibility(GONE);
                } else {
                    loadingIndicator.setVisibility(GONE);

                    if (resource.status == SUCCESS) {
                        historyRecyclerView.setVisibility(VISIBLE);
                        historyAdapter.setData(resource.data);
                        historyAdapter.notifyDataSetChanged();
                    } else if (resource.status == ERROR) {
                        loggingHelper.error(TAG, "An error occurred while getting the search " +
                                "user history", resource.error);

                        Toast.makeText(this, R.string.an_error_occurred, LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewModel.getResult().observe(this, resource -> {
            if (resource != null) {
                if (resource.status == LOADING) {
                    loadingIndicator.setVisibility(VISIBLE);
                    resultRecyclerView.setVisibility(GONE);
                    historyRecyclerView.setVisibility(GONE);
                    noResultsTextView.setVisibility(GONE);
                } else {
                    loadingIndicator.setVisibility(GONE);

                    if (resource.status == SUCCESS) {
                        List<SearchUserResult.Item> resultItems = resource.data.getItems();
                        if (!resultItems.isEmpty()) {
                            resultRecyclerView.setVisibility(VISIBLE);

                            resultAdapter.setData(resultItems);
                            resultAdapter.notifyDataSetChanged();
                        } else {
                            noResultsTextView.setVisibility(VISIBLE);
                        }
                    } else if (resource.status == ERROR) {
                        loggingHelper.error(TAG, "An error occurred while searching users", resource.error);

                        Toast.makeText(this, R.string.an_error_occurred, LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
