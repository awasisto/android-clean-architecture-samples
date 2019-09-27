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

package com.wasisto.githubuserfinder.data.searchhistory

import android.content.ContentValues
import android.content.Context

import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryContract.SearchHistoryEntry
import com.wasisto.githubuserfinder.model.SearchHistoryItem

import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import android.provider.BaseColumns

class SearchHistoryDataSourceImpl private constructor(context: Context) : SearchHistoryDataSource {

    companion object {

        @Volatile
        private var instance: SearchHistoryDataSourceImpl? = null

        @Synchronized
        fun getInstance(context: Context): SearchHistoryDataSourceImpl {
            return instance ?: SearchHistoryDataSourceImpl(context).also { instance = it }
        }
    }

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

        db.insertWithOnConflict(SearchHistoryEntry.TABLE_NAME, null, contentValues, CONFLICT_REPLACE)
    }
}
