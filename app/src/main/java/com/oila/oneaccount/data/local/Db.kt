package com.oila.oneaccount.data.local

import android.content.ContentValues
import android.database.Cursor
import com.oila.oneaccount.data.model.Name
import com.oila.oneaccount.data.model.Profile
import com.oila.oneaccount.data.model.profile.FieldType
import com.oila.oneaccount.data.model.profile.ProfileItem
import com.oila.oneaccount.util.extension.getInt
import com.oila.oneaccount.util.extension.getLong
import com.oila.oneaccount.util.extension.getString
import java.util.*

object Db {

    object ProfileTable {

        val TABLE_PROFILE = "profile"
        val COLUMN_PROFILE_FIELD_NAME = "field_name"
        val COLUMN_PROFILE_FIELD_VALUE = "field_value"
        val COLUMN_PROFILE_FIELD_TYPE = "type"

        val DB_CREATE_PROFILE = """
            CREATE TABLE $TABLE_PROFILE(
              $COLUMN_PROFILE_FIELD_NAME  TEXT PRIMARY KEY,
              $COLUMN_PROFILE_FIELD_VALUE TEXT,
              $COLUMN_PROFILE_FIELD_TYPE  TEXT
            )
        """

        fun toContentValues(fieldName: String , fieldValue: String , type: String ): ContentValues {
            val values = ContentValues()
            values.put(COLUMN_PROFILE_FIELD_NAME, fieldName)
            values.put(COLUMN_PROFILE_FIELD_VALUE, fieldValue)
            values.put(COLUMN_PROFILE_FIELD_TYPE, type)
            return values
        }

        fun parseCursor(cursor: Cursor): ProfileItem {
            val fieldName = cursor.getString(COLUMN_PROFILE_FIELD_NAME)
            val fieldValue = cursor.getString(COLUMN_PROFILE_FIELD_VALUE)
            val type = cursor.getString(COLUMN_PROFILE_FIELD_TYPE)
            return ProfileItem(key = fieldName, value = fieldValue,  type = FieldType.valueOf(type))
        }
    }

    object HistoryTable {

        val TABLE_HISTORY = "history"

        val COLUMN_HISTORY_REQUEST_ID = "id"
        val COLUMN_HISTORY_FIELD_NAME = "field_name"
        val COLUMN_HISTORY_FIELD_VALUE = "field_value"
        val COLUMN_HISTORY_SHARED = "shared"
        val COLUMN_HISTORY_REQUEST_DATE = "request_date"
        val COLUMN_HISTORY_SHARE_DATE = "share_date"

        val DB_CREATE_HISTORY = "create table " +
                TABLE_HISTORY + " (" + 
                COLUMN_HISTORY_REQUEST_ID + " integer PRIMARY KEY autoincrement, " +
                COLUMN_HISTORY_FIELD_NAME + " text not null, " +
                COLUMN_HISTORY_FIELD_VALUE + " text not null, " +
                COLUMN_HISTORY_SHARED + " integer, " +
                COLUMN_HISTORY_REQUEST_DATE + " text not null, " +
                COLUMN_HISTORY_SHARE_DATE + " text not null," +
                "FOREIGN KEY (" + COLUMN_HISTORY_FIELD_NAME + ") REFERENCES " + ProfileTable.TABLE_PROFILE + "(" +  ProfileTable.COLUMN_PROFILE_FIELD_NAME + ")," +
                "FOREIGN KEY (" + COLUMN_HISTORY_FIELD_VALUE + ") REFERENCES " + ProfileTable.TABLE_PROFILE + "(" + ProfileTable.COLUMN_PROFILE_FIELD_VALUE + "));"

    }
}
