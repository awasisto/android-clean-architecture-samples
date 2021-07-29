package com.wasisto.githubuserfinder.android.data.searchhistory;

import android.provider.BaseColumns;

interface SearchHistoryContract {

    class SearchHistoryEntry implements BaseColumns {

        static final String TABLE_NAME = "search_history";

        static final String COLUMN_NAME_SEARCH_QUERY = "search_query";
    }
}
