package com.wasisto.githubuserfinder.domain.interfaces

import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem

interface SearchHistoryDataSource {

    fun getAll(): List<SearchHistoryItem>

    fun add(searchHistoryItem: SearchHistoryItem)
}