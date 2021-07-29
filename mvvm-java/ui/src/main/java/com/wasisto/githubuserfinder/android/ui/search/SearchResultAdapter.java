package com.wasisto.githubuserfinder.android.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.wasisto.githubuserfinder.android.databinding.ItemSearchResultBinding;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSearchResultBinding binding;

        public ViewHolder(ItemSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SearchUsersResult.Item resultItem, SearchViewModel viewModel) {
            binding.setResultItem(resultItem);
            binding.setViewModel(viewModel);
        }
    }

    private SearchViewModel viewModel;

    private List<SearchUsersResult.Item> data;

    public SearchResultAdapter(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSearchResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position), viewModel);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public List<SearchUsersResult.Item> getData() {
        return data;
    }

    public void setData(List<SearchUsersResult.Item> data) {
        this.data = data;
    }
}
