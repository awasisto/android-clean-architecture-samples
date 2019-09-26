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

package com.wasisto.githubuserfinder.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast

import com.wasisto.githubuserfinder.R
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl
import com.wasisto.githubuserfinder.model.SearchUserResult
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryDataSourceImpl
import com.wasisto.githubuserfinder.model.SearchHistoryItem
import com.wasisto.githubuserfinder.ui.userdetails.UserDetailsActivity
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl

import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast.LENGTH_SHORT
import com.wasisto.githubuserfinder.domain.GetHistoryUseCase
import com.wasisto.githubuserfinder.domain.SearchUseCase

import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchView {

    private lateinit var presenter: SearchPresenter

    private lateinit var resultAdapter: ResultAdapter

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        presenter = SearchPresenterImpl(
            this,
            SearchUseCase(
                GithubDataSourceImpl.getInstance(this),
                SearchHistoryDataSourceImpl.getInstance(this),
                LoggingHelperImpl.getInstance()
            ),
            GetHistoryUseCase(
                SearchHistoryDataSourceImpl.getInstance(this)
            ),
            LoggingHelperImpl.getInstance()
        )

        resultAdapter = ResultAdapter(emptyList())
        resultAdapter.data = emptyList()
        resultAdapter.onItemClick = presenter::onResultItemClick

        resultRecyclerView.layoutManager = LinearLayoutManager(this)
        resultRecyclerView.adapter = resultAdapter

        historyAdapter = HistoryAdapter(emptyList())
        historyAdapter.onItemClick = presenter::onHistoryItemClick

        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = historyAdapter

        searchButton.setOnClickListener { presenter.onSearch(queryEditText.text.toString()) }

        presenter.onViewCreated()
    }

    override fun showLoadingIndicator() {
        loadingIndicator.visibility = VISIBLE
    }

    override fun hideLoadingIndicator() {
        loadingIndicator.visibility = GONE
    }

    override fun setQuery(query: String) {
        queryEditText.setText(query)
    }

    override fun showResultItems(resultItems: List<SearchUserResult.Item>) {
        resultRecyclerView.visibility = VISIBLE

        resultAdapter.data = resultItems
        resultAdapter.notifyDataSetChanged()
    }

    override fun hideResultItems() {
        resultRecyclerView.visibility = GONE
    }

    override fun showHistory(history: List<SearchHistoryItem>) {
        historyRecyclerView.visibility = VISIBLE

        historyAdapter.data = history
        historyAdapter.notifyDataSetChanged()
    }

    override fun hideHistory() {
        historyRecyclerView.visibility = GONE
    }

    override fun showNoResultsText() {
        noResultsTextView.visibility = VISIBLE
    }

    override fun hideNoResultsText() {
        noResultsTextView.visibility = GONE
    }

    override fun openUserDetailsActivity(username: String) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra(UserDetailsActivity.EXTRA_USERNAME, username)

        startActivity(intent)
    }

    override fun showToast(resId: Int) {
        Toast.makeText(this, resId, LENGTH_SHORT).show()
    }
}
