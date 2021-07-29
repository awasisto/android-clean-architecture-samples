package com.wasisto.githubuserfinder.android.data.searchhistory

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.wasisto.githubuserfinder.android.data.searchhistory.SearchHistoryContract.*
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem

class SqliteSearchHistoryDataSource(context: Context) : SearchHistoryDataSource {

    private val dbHelper: SearchHistoryDbHelper = SearchHistoryDbHelper(context)

    override fun getAll(): List<SearchHistoryItem> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY)

        val cursor = db.query(SearchHistoryEntry.TABLE_NAME, projection, null, null, null, null, null)

        val searchHistory = mutableListOf<SearchHistoryItem>()

        while (cursor.moveToNext()) {
            val searchHistoryItemId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val searchHistoryItemQuery = cursor.getString(cursor.getColumnIndexOrThrow(SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY))

            searchHistory.add(SearchHistoryItem(searchHistoryItemId, searchHistoryItemQuery))
        }

        cursor.close()

        return searchHistory
    }

    override fun add(searchHistoryItem: SearchHistoryItem) {
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY, searchHistoryItem.query)

        db.insertWithOnConflict(SearchHistoryEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
    }
}