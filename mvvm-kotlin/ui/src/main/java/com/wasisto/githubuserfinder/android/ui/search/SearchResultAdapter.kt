package com.wasisto.githubuserfinder.android.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wasisto.githubuserfinder.android.databinding.ItemSearchResultBinding
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult

class SearchResultAdapter(private val viewModel: SearchViewModel) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(resultItem: SearchUsersResult.Item, viewModel: SearchViewModel) {
            binding.resultItem = resultItem
            binding.viewModel = viewModel
        }
    }

    lateinit var data: List<SearchUsersResult.Item>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], viewModel)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}