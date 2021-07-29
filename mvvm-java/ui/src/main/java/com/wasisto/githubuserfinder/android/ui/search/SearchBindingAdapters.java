package com.wasisto.githubuserfinder.android.ui.search;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;

import java.util.List;

public class SearchBindingAdapters {

    @BindingAdapter({"searchResultItems", "viewModel"})
    public static void searchResultItems(RecyclerView recyclerView, List<SearchUsersResult.Item> searchResultItems,
                                         SearchViewModel viewModel) {
        if (searchResultItems != null) {
            SearchResultAdapter searchResultAdapter = (SearchResultAdapter) recyclerView.getAdapter();
            if (searchResultAdapter == null) {
                recyclerView.setAdapter(
                        new SearchResultAdapter(viewModel) {{
                            setData(searchResultItems);
                        }}
                );
            }
        }
    }

    @BindingAdapter({"searchHistoryItems", "viewModel"})
    public static void searchHistoryItems(RecyclerView recyclerView, List<SearchHistoryItem> searchHistoryItems,
                                          SearchViewModel viewModel) {
        if (searchHistoryItems != null) {
            SearchHistoryAdapter searchHistoryAdapter = (SearchHistoryAdapter) recyclerView.getAdapter();
            if (searchHistoryAdapter == null) {
                recyclerView.setAdapter(
                        new SearchHistoryAdapter(viewModel) {{
                            setData(searchHistoryItems);
                        }}
                );
            }
        }
    }
}
