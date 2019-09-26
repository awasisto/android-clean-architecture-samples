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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.model.SearchUserResult;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<SearchUserResult.Item> data;

    private OnItemClickListener mOnItemClickListener;

    public ResultAdapter(List<SearchUserResult.Item> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchUserResult.Item resultItem = data.get(position);

        Picasso.get().load(resultItem.getAvatarUrl()).placeholder(R.color.colorAccent).into(holder.avatarImageView);

        holder.usernameTextView.setText(resultItem.getLogin());

        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(resultItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<SearchUserResult.Item> data) {
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImageView;

        public TextView usernameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(SearchUserResult.Item resultItem);
    }
}
