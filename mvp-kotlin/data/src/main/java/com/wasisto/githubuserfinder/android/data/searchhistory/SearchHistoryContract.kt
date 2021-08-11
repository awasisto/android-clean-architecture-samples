package com.wasisto.githubuserfinder.android.data.searchhistory

import android.provider.BaseColumns

internal class SearchHistoryContract {

    object SearchHistoryEntry : BaseColumns {

        const val TABLE_NAME = "search_history"

        const val COLUMN_NAME_SEARCH_QUERY = "search_query"
    }
}