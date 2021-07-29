package com.wasisto.githubuserfinder.android.data.searchhistory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.wasisto.githubuserfinder.android.data.searchhistory.SearchHistoryContract.*;

class SearchHistoryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "search_history.db";

    private static final int DATABASE_VERSION = 1;

    public SearchHistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + SearchHistoryEntry.TABLE_NAME + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY,"
                        + SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY + " TEXT UNIQUE)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SearchHistoryEntry.TABLE_NAME);
        onCreate(db);
    }
}
