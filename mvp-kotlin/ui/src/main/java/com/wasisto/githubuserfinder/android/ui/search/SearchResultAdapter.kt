package com.wasisto.githubuserfinder.android.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wasisto.githubuserfinder.android.R
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult

class SearchResultAdapter(private val presenter: SearchPresenter) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)

        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
    }

    var data: List<SearchUsersResult.Item> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultItem = data[position]

        Glide.with(holder.avatarImageView)
            .load(resultItem.avatarUrl)
            .placeholder(R.color.teal_200)
            .into(holder.avatarImageView)

        holder.usernameTextView.text = resultItem.login

        holder.itemView.setOnClickListener { presenter.onResultItemClick(resultItem) }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}