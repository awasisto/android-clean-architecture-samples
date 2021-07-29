package com.wasisto.githubuserfinder.android.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.githubuserfinder.android.databinding.ItemSearchHistoryBinding
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem

class SearchHistoryAdapter(private val viewModel: SearchViewModel) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: SearchHistoryItem, viewModel: SearchViewModel) {
            binding.historyItem = historyItem
            binding.viewModel = viewModel
        }
    }

    lateinit var data: List<SearchHistoryItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], viewModel)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}