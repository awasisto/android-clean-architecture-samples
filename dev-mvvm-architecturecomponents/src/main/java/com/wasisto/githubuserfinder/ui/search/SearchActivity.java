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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.*;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.ui.userdetails.UserDetailsActivity;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;

    private ProgressBar loadingIndicator;

    private EditText queryEditText;

    private Button searchButton;

    private RecyclerView resultRecyclerView;

    private RecyclerView historyRecyclerView;

    private TextView noResultsTextView;

    private HistoryAdapter historyAdapter;

    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        viewModel = ViewModelProviders.of(this, new SearchViewModelFactory(this))
                .get(SearchViewModel.class);

        loadingIndicator = findViewById(R.id.loadingIndicator);
        queryEditText = findViewById(R.id.queryEditText);
        searchButton = findViewById(R.id.searchButton);
        resultRecyclerView = findViewById(R.id.resultRecyclerView);
        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        noResultsTextView = findViewById(R.id.noResultsTextView);

        historyAdapter = new HistoryAdapter(new ArrayList<>());
        historyAdapter.setOnItemClickListener(historyItem -> {
            queryEditText.setText(historyItem.getQuery());
            loadingIndicator.setVisibility(VISIBLE);
            historyRecyclerView.setVisibility(GONE);

            viewModel.onSearch(historyItem.getQuery());
        });

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

        searchButton.setOnClickListener(v -> {
            loadingIndicator.setVisibility(VISIBLE);
            resultRecyclerView.setVisibility(GONE);
            historyRecyclerView.setVisibility(GONE);
            noResultsTextView.setVisibility(GONE);

            viewModel.onSearch(queryEditText.getText().toString());
        });

        loadingIndicator.setVisibility(VISIBLE);
        resultRecyclerView.setVisibility(GONE);
        historyRecyclerView.setVisibility(GONE);
        noResultsTextView.setVisibility(GONE);

        viewModel.getHistory().observe(this, searchHistoryItems -> {
            if (searchHistoryItems != null) {
                loadingIndicator.setVisibility(GONE);
                historyRecyclerView.setVisibility(VISIBLE);

                historyAdapter.setData(searchHistoryItems);
                historyAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getResult().observe(this, result -> {
            if (result != null) {
                loadingIndicator.setVisibility(GONE);

                if (!result.getItems().isEmpty()) {
                    resultRecyclerView.setVisibility(VISIBLE);

                    resultAdapter.setData(result.getItems());
                    resultAdapter.notifyDataSetChanged();
                } else {
                    noResultsTextView.setVisibility(VISIBLE);
                }
            }
        });

        viewModel.getErrorMessageEvent().observe(this, errorMessageEvent -> {
            if (errorMessageEvent != null) {
                Toast.makeText(this, errorMessageEvent.getData(), LENGTH_SHORT).show();
            }
        });
    }
}
