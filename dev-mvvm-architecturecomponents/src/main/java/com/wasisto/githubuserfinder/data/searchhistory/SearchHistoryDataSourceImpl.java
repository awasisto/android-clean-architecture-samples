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

package com.wasisto.githubuserfinder.data.searchhistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;
import com.wasisto.githubuserfinder.Callback;
import com.wasisto.githubuserfinder.data.searchhistory.SearchHistoryContract.SearchHistoryEntry;
import com.wasisto.githubuserfinder.data.searchhistory.model.SearchHistoryItem;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

public class SearchHistoryDataSourceImpl implements SearchHistoryDataSource {

    private static volatile SearchHistoryDataSourceImpl instance;

    private SearchHistoryDbHelper dbHelper;

    private Executor executor = Executors.newSingleThreadExecutor();

    private Handler handler = new Handler(Looper.getMainLooper());
    
    private SearchHistoryDataSourceImpl(Context context) {
        dbHelper = new SearchHistoryDbHelper(context);
    }

    public static synchronized SearchHistoryDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new SearchHistoryDataSourceImpl(context.getApplicationContext());
        }

        return instance;
    }


    @Override
    public void getAll(Callback<List<SearchHistoryItem>> callback) {
        executor.execute(() -> {
            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                String[] projection = {
                        SearchHistoryEntry._ID,
                        SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY
                };

                Cursor cursor = db.query(SearchHistoryEntry.TABLE_NAME, projection, null, null,
                        null, null, null);

                List<SearchHistoryItem> searchHistory = new ArrayList<>();

                while (cursor.moveToNext()) {
                    int searchHistoryItemId = cursor.getInt(cursor.getColumnIndexOrThrow(SearchHistoryEntry._ID));
                    String searchHistoryItemQuery = cursor.getString(cursor.getColumnIndexOrThrow(
                            SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY));

                    SearchHistoryItem searchHistoryItem = new SearchHistoryItem();
                    searchHistoryItem.setId(searchHistoryItemId);
                    searchHistoryItem.setQuery(searchHistoryItemQuery);

                    searchHistory.add(searchHistoryItem);
                }

                cursor.close();

                handler.post(() -> callback.onSuccess(searchHistory));
            } catch (Throwable t) {
                handler.post(() -> callback.onError(t));
            }
        });
    }

    @Override
    public void add(SearchHistoryItem searchHistoryItem, Callback<Void> callback) {
        executor.execute(() -> {
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY, searchHistoryItem.getQuery());

                db.insertWithOnConflict(SearchHistoryEntry.TABLE_NAME, null, contentValues,
                        CONFLICT_REPLACE);

                handler.post(() -> callback.onSuccess(null));
            } catch (Throwable t) {
                handler.post(() -> callback.onError(t));
            }
        });
    }
}
