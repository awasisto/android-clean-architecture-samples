package com.wasisto.githubuserfinder.android.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wasisto.githubuserfinder.android.R;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarImageView;

        private TextView usernameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
        }
    }

    private List<SearchUsersResult.Item> data;

    private SearchPresenter presenter;

    public SearchResultAdapter(List<SearchUsersResult.Item> data, SearchPresenter presenter) {
        this.data = data;
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder holder, int position) {
        SearchUsersResult.Item resultItem = data.get(position);

        Glide.with(holder.avatarImageView)
                .load(resultItem.getAvatarUrl())
                .placeholder(R.color.teal_200)
                .into(holder.avatarImageView);

        holder.usernameTextView.setText(resultItem.getLogin());

        holder.itemView.setOnClickListener(v -> presenter.onResultItemClick(resultItem));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<SearchUsersResult.Item> data) {
        this.data = data;
    }
}
