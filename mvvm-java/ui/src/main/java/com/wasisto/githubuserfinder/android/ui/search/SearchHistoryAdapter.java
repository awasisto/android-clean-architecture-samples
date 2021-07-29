package com.wasisto.githubuserfinder.android.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.wasisto.githubuserfinder.android.databinding.ItemSearchHistoryBinding;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSearchHistoryBinding binding;

        public ViewHolder(ItemSearchHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SearchHistoryItem historyItem, SearchViewModel viewModel) {
            binding.setHistoryItem(historyItem);
            binding.setViewModel(viewModel);
        }
    }

    private SearchViewModel viewModel;

    private List<SearchHistoryItem> data;

    public SearchHistoryAdapter(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(SearchHistoryAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position), viewModel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<SearchHistoryItem> getData() {
        return data;
    }

    public void setData(List<SearchHistoryItem> data) {
        this.data = data;
    }
}
