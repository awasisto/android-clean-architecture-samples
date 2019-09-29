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

package com.wasisto.githubuserfinder.ui.search

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.githubuserfinder.model.SearchHistoryItem
import com.wasisto.githubuserfinder.model.SearchUserResult

@BindingAdapter("resultItems", "viewModel")
fun resultItems(recyclerView: RecyclerView,
                resultItems: List<SearchUserResult.Item>?,
                viewModel: SearchViewModel) {
    val resultAdapter = (recyclerView.adapter as? ResultAdapter ?: ResultAdapter(viewModel)).also {
        recyclerView.adapter = it
    }

    if (resultItems == null) {
        recyclerView.visibility = View.GONE
    } else {
        recyclerView.visibility = View.VISIBLE
        resultAdapter.data = resultItems
        resultAdapter.notifyDataSetChanged()
    }
}

@BindingAdapter("historyItems", "viewModel")
fun historyItems(recyclerView: RecyclerView,
                 historyItems: List<SearchHistoryItem>?,
                 viewModel: SearchViewModel) {
    val historyAdapter = (recyclerView.adapter as? HistoryAdapter ?: HistoryAdapter(viewModel)).also {
        recyclerView.adapter = it
    }

    if (historyItems == null) {
        recyclerView.visibility = View.GONE
    } else {
        recyclerView.visibility = View.VISIBLE
        historyAdapter.data = historyItems
        historyAdapter.notifyDataSetChanged()
    }
}
