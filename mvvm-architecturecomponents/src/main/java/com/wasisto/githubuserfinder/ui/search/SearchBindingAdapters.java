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

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;

import java.util.List;

public class SearchBindingAdapters {

    @BindingAdapter("items")
    public static void setResultItems(RecyclerView recyclerView, List<SearchUserResult.Item> resultItems) {
        ResultAdapter resultAdapter = (ResultAdapter) recyclerView.getAdapter();
        if (resultAdapter != null) {
            resultAdapter.setData(resultItems);
            resultAdapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter("items")
    public static void setHistory(RecyclerView recyclerView, List<SearchHistoryItem> history) {
        HistoryAdapter historyAdapter = (HistoryAdapter) recyclerView.getAdapter();
        if (historyAdapter != null) {
            historyAdapter.setData(history);
            historyAdapter.notifyDataSetChanged();
        }
    }
}
