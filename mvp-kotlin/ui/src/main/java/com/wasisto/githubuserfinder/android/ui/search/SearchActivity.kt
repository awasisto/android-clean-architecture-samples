package com.wasisto.githubuserfinder.android.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wasisto.githubuserfinder.android.DefaultServiceLocator
import com.wasisto.githubuserfinder.android.R
import com.wasisto.githubuserfinder.android.ui.userdetails.UserDetailsActivity
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.Dispatchers

class SearchActivity : AppCompatActivity(), SearchView {

    private lateinit var presenter: SearchPresenter

    private lateinit var searchResultAdapter: SearchResultAdapter

    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val serviceLocator = DefaultServiceLocator.getInstance(application)

        presenter = SearchPresenter(
            this,
            SearchUsersUseCase(
                serviceLocator.githubDataSource,
                serviceLocator.searchHistoryDataSource,
                serviceLocator.getLogger(SearchUsersUseCase::class)
            ),
            GetSearchHistoryUseCase(
                serviceLocator.searchHistoryDataSource
            ),
            serviceLocator.getLogger(SearchPresenter::class),
            serviceLocator.ioDispatcher,
            serviceLocator.mainDispatcher,
            lifecycleScope
        )

        searchResultAdapter = SearchResultAdapter(presenter)
        searchResultRecyclerView.adapter = searchResultAdapter

        searchHistoryAdapter = SearchHistoryAdapter(presenter)
        searchHistoryRecyclerView.adapter = searchHistoryAdapter

        searchButton.setOnClickListener { presenter.search(queryEditText.text.toString()) }

        presenter.load()
    }

    override fun showLoadingIndicator() {
        loadingIndicator.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        loadingIndicator.visibility = View.GONE
    }

    override fun setQuery(query: String?) {
        queryEditText.setText(query)
    }

    override fun showSearchResult(searchResult: List<SearchUsersResult.Item>) {
        searchResultRecyclerView.visibility = View.VISIBLE
        searchResultAdapter.data = searchResult
        searchResultAdapter.notifyDataSetChanged()
    }

    override fun showSearchHistory(searchHistory: List<SearchHistoryItem>) {
        searchHistoryRecyclerView.visibility = View.VISIBLE
        searchHistoryAdapter.data = searchHistory
        searchHistoryAdapter.notifyDataSetChanged()
    }

    override fun hideSearchHistory() {
        searchHistoryRecyclerView.visibility = View.GONE
    }

    override fun showNoResultsText() {
        noResultsTextView.visibility = View.VISIBLE
    }

    override fun hideNoResultsText() {
        noResultsTextView.visibility = View.GONE
    }

    override fun openUserDetailsActivity(username: String) {
        startActivity(
            Intent(this, UserDetailsActivity::class.java).apply {
                putExtra(UserDetailsActivity.EXTRA_USERNAME, username)
            }
        )
    }

    override fun showErrorToast() {
        Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_SHORT).show()
    }
}