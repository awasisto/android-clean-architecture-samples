package com.wasisto.githubuserfinder.android.ui.common

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wasisto.githubuserfinder.android.R
import com.wasisto.githubuserfinder.android.ui.search.SearchHistoryAdapter
import com.wasisto.githubuserfinder.android.ui.search.SearchResultAdapter
import com.wasisto.githubuserfinder.android.ui.search.SearchViewModel
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, imageUrl: String?) {
    Glide.with(imageView)
        .load(imageUrl)
        .placeholder(R.color.teal_200)
        .into(imageView)
}
