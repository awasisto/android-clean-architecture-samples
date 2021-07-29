package com.wasisto.githubuserfinder.android.ui.search

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult

@BindingAdapter("searchResultItems", "viewModel")
fun searchResultItems(recyclerView: RecyclerView, searchResultItems: List<SearchUsersResult.Item>?,
                      viewModel: SearchViewModel) {
    searchResultItems?.let {
        recyclerView.adapter = (recyclerView.adapter as? SearchResultAdapter ?: SearchResultAdapter(viewModel)).apply {
            data = searchResultItems
        }
    }
}

@BindingAdapter("searchHistoryItems", "viewModel")
fun searchHistoryItems(recyclerView: RecyclerView, searchHistoryItems: List<SearchHistoryItem>?,
                       viewModel: SearchViewModel) {
    searchHistoryItems?.let {
        recyclerView.adapter = (recyclerView.adapter as? SearchHistoryAdapter ?: SearchHistoryAdapter(viewModel)).apply {
            data = searchHistoryItems
        }
    }
}