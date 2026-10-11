package com.example.medicineremainder

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Medicine.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Medicine"
        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_DATE = "date"
        private const val COL_TIME = "timeOfDay"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT,
                $COL_DATE TEXT,
                $COL_TIME TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertMedicine(
        name: String,
        date: String,
        timeOfDay: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, name)
        values.put(COL_DATE, date)
        values.put(COL_TIME, timeOfDay)
        return db.insert(
            TABLE_NAME,
            null,
            values
        )
    }
}
