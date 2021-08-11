package com.wasisto.githubuserfinder.android.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wasisto.githubuserfinder.android.DefaultServiceLocator;
import com.wasisto.githubuserfinder.android.R;
import com.wasisto.githubuserfinder.android.ServiceLocator;
import com.wasisto.githubuserfinder.android.ui.userdetails.UserDetailsActivity;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase;
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView {

    private SearchPresenter presenter;

    private ProgressBar loadingIndicator;

    private EditText queryEditText;

    private Button searchButton;

    private RecyclerView searchResultRecyclerView;

    private RecyclerView searchHistoryRecyclerView;

    private TextView noResultsTextView;

    private SearchResultAdapter searchResultAdapter;

    private SearchHistoryAdapter searchHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ServiceLocator serviceLocator = DefaultServiceLocator.getInstance(getApplication());

        presenter = new SearchPresenter(
                this,
                new SearchUsersUseCase(
                        serviceLocator.getGithubDataSource(),
                        serviceLocator.getSearchHistoryDataSource(),
                        serviceLocator.getLogger(SearchUsersUseCase.class)
                ),
                new GetSearchHistoryUseCase(
                        serviceLocator.getSearchHistoryDataSource()
                ),
                serviceLocator.getLogger(SearchPresenter.class),
                serviceLocator.getIoExecutorService(),
                serviceLocator.getMainExecutorService()
        );

        loadingIndicator = findViewById(R.id.loadingIndicator);
        queryEditText = findViewById(R.id.queryEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultRecyclerView = findViewById(R.id.searchResultRecyclerView);
        searchHistoryRecyclerView = findViewById(R.id.searchHistoryRecyclerView);
        noResultsTextView = findViewById(R.id.noResultsTextView);

        searchResultAdapter = new SearchResultAdapter(presenter);
        searchResultRecyclerView.setAdapter(searchResultAdapter);

        searchHistoryAdapter = new SearchHistoryAdapter(presenter);
        searchHistoryRecyclerView.setAdapter(searchHistoryAdapter);

        searchButton.setOnClickListener(v -> presenter.search(queryEditText.getText().toString()));

        presenter.load();
    }

    @Override
    public void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void setQuery(String query) {
        queryEditText.setText(query);
    }

    @Override
    public void showSearchResult(List<SearchUsersResult.Item> searchResult) {
        searchResultRecyclerView.setVisibility(View.VISIBLE);

        searchResultAdapter.setData(searchResult);
        searchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSearchHistory(List<SearchHistoryItem> searchHistory) {
        searchHistoryRecyclerView.setVisibility(View.VISIBLE);

        searchHistoryAdapter.setData(searchHistory);
        searchHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideSearchHistory() {
        searchHistoryRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showNoResultsText() {
        noResultsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoResultsText() {
        noResultsTextView.setVisibility(View.GONE);
    }

    @Override
    public void openUserDetailsActivity(String username) {
        startActivity(
                new Intent(this, UserDetailsActivity.class) {{
                    putExtra(UserDetailsActivity.EXTRA_USERNAME, username);
                }}
        );
    }

    @Override
    public void showErrorToast() {
        Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_SHORT).show();
    }
}