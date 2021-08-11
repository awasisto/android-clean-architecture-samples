package com.wasisto.githubuserfinder.android.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wasisto.githubuserfinder.android.R;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;

import java.util.Collections;
import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView queryTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            queryTextView = itemView.findViewById(R.id.queryTextView);
        }
    }

    private List<SearchHistoryItem> data = Collections.emptyList();

    private SearchPresenter presenter;

    public SearchHistoryAdapter(SearchPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchHistoryAdapter.ViewHolder holder, int position) {
        SearchHistoryItem historyItem = data.get(position);

        holder.queryTextView.setText(historyItem.getQuery());

        holder.itemView.setOnClickListener(v -> presenter.onSearchHistoryItemClick(historyItem));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<SearchHistoryItem> data) {
        this.data = data;
    }
}