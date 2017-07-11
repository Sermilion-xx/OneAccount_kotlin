package com.oila.oneaccount.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import timber.log.Timber

import java.sql.SQLException

class OneDBOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, name, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        try {
            db.execSQL(Db.HistoryTable.DB_CREATE_HISTORY)
            db.execSQL(Db.ProfileTable.DB_CREATE_PROFILE)
            db.setTransactionSuccessful()
        } catch (e: android.database.SQLException) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.beginTransaction()
        try {
            db.execSQL(DROP_TABLE_IF_EXISTS + Db.ProfileTable.TABLE_PROFILE)
            db.execSQL(DROP_TABLE_IF_EXISTS + Db.HistoryTable.TABLE_HISTORY)
            db.setTransactionSuccessful()
        } catch (e: android.database.SQLException) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
        onCreate(db)
    }

    companion object {

        val DATABASE_NAME = "com.oila.one_account.db"
        val DATABASE_VERSION = 1
        val DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS "

        private var sqliteDb: SQLiteDatabase? = null
        private var INSTANCE: OneDBOpenHelper? = null

        fun getConnection(context: Context): OneDBOpenHelper? {
            if (INSTANCE == null) {
                INSTANCE = OneDBOpenHelper(context, DATABASE_NAME, null)
                try {
                    sqliteDb = INSTANCE?.writableDatabase
                } catch (ex: SQLiteException) {
                    sqliteDb = INSTANCE?.readableDatabase
                }
            }
            return INSTANCE
        }
    }
}
