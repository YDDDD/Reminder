package com.example.cxy100.reminders

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private var mListView : ListView? = null
    private var mDbAdapter : RemindersDbAdapter? = null
    private var mCursorAdapter : RemindersSimpleCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //actionBar.setHomeButtonEnabled(true)
        //actionBar.setDisplayShowHomeEnabled(true)

        mListView = findViewById(R.id.main_list_view)
        mListView!!.divider = null

        mDbAdapter = RemindersDbAdapter(this)
        mDbAdapter!!.open()

        if(savedInstanceState == null) {
            mDbAdapter?.deleteAllReminders()
            insertSomeReminders()
        }

        var cursor = mDbAdapter!!.fetchAllReminders()
        var from = arrayOf(RemindersDbAdapter.COL_CONTENT)
        var to = intArrayOf(R.id.row_text)

        mCursorAdapter = RemindersSimpleCursorAdapter(this, R.layout.listview_cell, cursor!!, from, to, 0)

        mListView!!.adapter = mCursorAdapter
        mListView!!.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(this@MainActivity, "Clicked $position", Toast.LENGTH_SHORT)
                var builder = AlertDialog.Builder(this@MainActivity)
                var modeListView = ListView(this@MainActivity)
                var modes = arrayOf("Edit Reminder", "Delete Reminder")
                val modeAdapter = ArrayAdapter(this@MainActivity,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes)
                modeListView.adapter = modeAdapter
                builder.setView(modeListView)
                val dialog = builder.create()
                dialog.show()
                modeListView.setOnItemClickListener { parent, view, position, id ->
                    dialog.dismiss()
                }
            }

        }


    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_new -> return true
            R.id.action_exit ->
                finish()
            else -> return false
        }
        return true
    }


    private fun insertSomeReminders() {
        mDbAdapter?.createReminder("Buy Learn Android Studio", true)
        mDbAdapter?.createReminder("Send Dad birthday gift", false)
        mDbAdapter?.createReminder("Dinner at the Gage on Friday", false)
        mDbAdapter?.createReminder("String squash racket", false)
        mDbAdapter?.createReminder("Shovel and salt walkways", false)
        mDbAdapter?.createReminder("Prepare Advanced Android syllabus", true)
        mDbAdapter?.createReminder("Buy new office chair", false)
        mDbAdapter?.createReminder("Call Auto-body shop for quote", false)
        mDbAdapter?.createReminder("Renew membership to club", false)
        mDbAdapter?.createReminder("Buy new Galaxy Android phone", true)
        mDbAdapter?.createReminder("Sell old Android phone - auction", false)
        mDbAdapter?.createReminder("Buy new paddles for kayaks", false)
        mDbAdapter?.createReminder("Call accountant about tax returns", false)
        mDbAdapter?.createReminder("Buy 300,000 shares of Google", false)
    }


}


