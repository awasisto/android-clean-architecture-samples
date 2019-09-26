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

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.model.SearchUserResult;
import com.wasisto.githubuserfinder.databinding.ItemResultBinding;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<SearchUserResult.Item> data;

    private SearchViewModel viewModel;

    public ResultAdapter(List<SearchUserResult.Item> data, SearchViewModel viewModel) {
        this.data = data;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemResultBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_result, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(
                data.get(position),
                resultItem -> viewModel.onResultItemClick(resultItem)
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<SearchUserResult.Item> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemResultBinding binding;

        public ViewHolder(ItemResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SearchUserResult.Item resultItem, ResultItemActionsListener listener) {
            binding.setResultItem(resultItem);
            binding.setListener(listener);
        }
    }
}
