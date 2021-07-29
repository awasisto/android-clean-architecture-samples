package com.wasisto.githubuserfinder.android.data.searchhistory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.wasisto.githubuserfinder.android.data.searchhistory.SearchHistoryContract.*;
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource;
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class SqliteSearchHistoryDataSource implements SearchHistoryDataSource {

    private SearchHistoryDbHelper dbHelper;

    public SqliteSearchHistoryDataSource(Context context) {
        dbHelper = new SearchHistoryDbHelper(context);
    }

    @Override
    public List<SearchHistoryItem> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {BaseColumns._ID, SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY};

        Cursor cursor = db.query(SearchHistoryEntry.TABLE_NAME, projection, null, null, null, null, null);

        List<SearchHistoryItem> searchHistory = new ArrayList<>();

        while (cursor.moveToNext()) {
            int searchHistoryItemId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String searchHistoryItemQuery = cursor.getString(cursor.getColumnIndexOrThrow(SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY));

            searchHistory.add(
                    new SearchHistoryItem() {{
                        setId(searchHistoryItemId);
                        setQuery(searchHistoryItemQuery);
                    }}
            );
        }

        cursor.close();

        return searchHistory;
    }

    @Override
    public void add(SearchHistoryItem searchHistoryItem) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY, searchHistoryItem.getQuery());

        db.insertWithOnConflict(SearchHistoryEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
