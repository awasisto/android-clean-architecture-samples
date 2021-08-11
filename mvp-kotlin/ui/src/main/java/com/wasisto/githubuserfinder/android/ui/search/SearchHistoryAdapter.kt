package com.wasisto.githubuserfinder.android.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.githubuserfinder.android.R
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem

class SearchHistoryAdapter(private val presenter: SearchPresenter) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val queryTextView: TextView = itemView.findViewById(R.id.queryTextView)
    }

    var data: List<SearchHistoryItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = data[position]

        holder.queryTextView.text = historyItem.query

        holder.itemView.setOnClickListener { presenter.onSearchHistoryItemClick(historyItem) }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}