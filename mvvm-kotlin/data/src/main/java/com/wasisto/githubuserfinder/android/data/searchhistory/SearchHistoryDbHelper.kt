package com.wasisto.githubuserfinder.android.data.searchhistory

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns



import com.wasisto.githubuserfinder.android.data.searchhistory.SearchHistoryContract.*

internal class SearchHistoryDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "search_history.db"

        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE ${SearchHistoryEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${SearchHistoryEntry.COLUMN_NAME_SEARCH_QUERY} TEXT UNIQUE)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${SearchHistoryEntry.TABLE_NAME}")
        onCreate(db)
    }
}