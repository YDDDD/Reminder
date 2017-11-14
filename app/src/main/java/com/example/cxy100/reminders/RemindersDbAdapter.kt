package com.example.cxy100.reminders

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by cxy100 on 2017/11/8.
 */
class RemindersDbAdapter(private val mCtx: Context) {
    private var mDbHelper: DatabaseHelper? = null
    private var mDb: SQLiteDatabase? = null
    //open
    @Throws(SQLException::class)
    fun open() {
        mDbHelper = DatabaseHelper(mCtx)
        mDb = mDbHelper?.writableDatabase
    }

    //close
    fun close() {
        mDbHelper?.close()
    }

    //CREATE
    //note that the id will be created for you automatically
    fun createReminder(name: String, important: Boolean) {
        val values = ContentValues()
        values.put(COL_CONTENT, name)
        values.put(COL_ISIMPORTANT, if (important) 1 else 0)
        mDb!!.insert(TABLE_NAME, null, values)
    }

    //overloaded to take a reminder
    fun createReminder(reminder: ReminderModel): Long {
        val values = ContentValues()
        values.put(COL_CONTENT, reminder.content)
        values.put(COL_ISIMPORTANT, reminder.isImportant)
        // Inserting Row
        return mDb!!.insert(TABLE_NAME, null, values)
    }

    //READ
    fun fetchReminderById(id: Int): ReminderModel {

        val cursor = mDb!!.query(TABLE_NAME, arrayOf(COL_ID, COL_CONTENT, COL_ISIMPORTANT), COL_ID + "=?",
                arrayOf(id.toString()), null, null, null, null
        )

        cursor?.moveToFirst()
        return ReminderModel(
                cursor!!.getInt(INDEX_ID),
                cursor!!.getString(INDEX_CONTENT),
                cursor!!.getInt(INDEX_ISIMPORTANT) != 0
        )
    }

    fun fetchAllReminders(): Cursor? {
        val mCursor = mDb!!.query(TABLE_NAME, arrayOf(COL_ID, COL_CONTENT, COL_ISIMPORTANT), null, null, null, null, null)

        mCursor?.moveToFirst()

        return mCursor
    }

    //UPDATE
    fun updateReminder(reminder: ReminderModel) {
        val values = ContentValues()
        values.put(COL_CONTENT, reminder.content)
        values.put(COL_ISIMPORTANT, reminder.isImportant)
        mDb!!.update(TABLE_NAME, values,
                COL_ID + "=?", arrayOf(reminder.id.toString()))
    }

    //DELETE
    fun deleteReminderById(nId: Int) {
        mDb!!.delete(TABLE_NAME, COL_ID + "=?", arrayOf(nId.toString()))
    }

    fun deleteAllReminders() {
        mDb!!.delete(TABLE_NAME, null, null)
    }

    private class DatabaseHelper internal constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            Log.w(TAG, DATABASE_CREATE)
            db.execSQL(DATABASE_CREATE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            Log.w(TAG, "Upgrading database from version $oldVersion to $newVersion, which will destroy all old data")
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
            onCreate(db)
        }
    }

    companion object {

        //these are the column names
        val COL_ID = "_id"
        val COL_CONTENT = "content"
        val COL_ISIMPORTANT = "important"
        //these are the corresponding indices
        val INDEX_ID = 0
        val INDEX_CONTENT = INDEX_ID + 1
        val INDEX_ISIMPORTANT = INDEX_ID + 2
        //used for logging
        private val TAG = "RemindersDbAdapter"
        private val DATABASE_NAME = "dba_remdrs"
        private val TABLE_NAME = "tbl_remdrs"
        private val DATABASE_VERSION = 1
        //SQL statement used to create the database
        private val DATABASE_CREATE = "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                COL_CONTENT + " TEXT, " +
                COL_ISIMPORTANT + " INTEGER );"
    }
}