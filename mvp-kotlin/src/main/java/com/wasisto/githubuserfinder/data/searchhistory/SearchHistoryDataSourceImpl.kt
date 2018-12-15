/*
 * Copyright (c) 2018 Andika Wasisto
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
import android.os.Handler
import android.os.Looper

import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryContract.SearchHistoryEntry
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem

import java.util.concurrent.Executors

import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import android.provider.BaseColumns
import java.util.concurrent.Executor

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

    private val executor: Executor = Executors.newSingleThreadExecutor()

    private val handler = Handler(Looper.getMainLooper())

    override fun getAll(onSuccess: (List<SearchHistoryItem>) -> Unit, onError: (Throwable) -> Unit) {
        executor.execute {
            try {
                val db = dbHelper.readableDatabase

                val projection = arrayOf(BaseColumns._ID, SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY)

                val cursor = db.query(SearchHistoryEntry.TABLE_NAME, projection, null, null,
                    null, null, null)

                val searchHistory = mutableListOf<SearchHistoryItem>()

                while (cursor.moveToNext()) {
                    val searchHistoryItemId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                    val searchHistoryItemQuery = cursor.getString(cursor.getColumnIndexOrThrow(
                        SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY))

                    searchHistory.add(
                        SearchHistoryItem(
                            searchHistoryItemId,
                            searchHistoryItemQuery
                        )
                    )
                }

                cursor.close()

                handler.post { onSuccess.invoke(searchHistory) }
            } catch (t: Throwable) {
                handler.post { onError.invoke(t) }
            }
        }
    }

    override fun add(searchHistoryItem: SearchHistoryItem, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        executor.execute {
            try {
                val db = dbHelper.writableDatabase

                val contentValues = ContentValues()
                contentValues.put(SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY, searchHistoryItem.query)

                db.insertWithOnConflict(SearchHistoryEntry.TABLE_NAME, null, contentValues,
                    CONFLICT_REPLACE)

                handler.post { onSuccess.invoke() }
            } catch (t: Throwable) {
                handler.post { onError.invoke(t) }
            }
        }
    }
}
