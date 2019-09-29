/*
 * Copyright (c) 2019 Andika Wasisto
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

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.wasisto.githubuserfinder.model.SearchUserResult;
import com.wasisto.githubuserfinder.model.SearchHistoryItem;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SearchBindingAdapters {

    @BindingAdapter({"resultItems", "viewModel"})
    public static void resultItems(RecyclerView recyclerView, List<SearchUserResult.Item> resultItems,
                                   SearchViewModel viewModel) {
        ResultAdapter resultAdapter = (ResultAdapter) recyclerView.getAdapter();

        if (resultAdapter == null) {
            resultAdapter = new ResultAdapter(viewModel);
            recyclerView.setAdapter(resultAdapter);
        }

        if (resultItems == null) {
            recyclerView.setVisibility(GONE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            resultAdapter.setData(resultItems);
            resultAdapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter({"historyItems", "viewModel"})
    public static void historyItems(RecyclerView recyclerView, List<SearchHistoryItem> historyItems,
                                    SearchViewModel viewModel) {
        HistoryAdapter historyAdapter = (HistoryAdapter) recyclerView.getAdapter();

        if (historyAdapter == null) {
            historyAdapter = new HistoryAdapter(viewModel);
            recyclerView.setAdapter(historyAdapter);
        }

        if (historyItems == null) {
            recyclerView.setVisibility(GONE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            historyAdapter.setData(historyItems);
            historyAdapter.notifyDataSetChanged();
        }
    }
}
