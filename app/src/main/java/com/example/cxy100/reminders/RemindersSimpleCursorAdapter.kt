package com.example.cxy100.reminders

import android.content.Context
import android.database.Cursor
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SimpleCursorAdapter
import android.view.View
import android.view.ViewGroup

/**
 * Created by cxy100 on 2017/11/8.
 */
class RemindersSimpleCursorAdapter(context: Context, layout: Int, c: Cursor, from: Array<String>, to: IntArray, flags: Int) : SimpleCursorAdapter(context, layout, c, from, to, flags) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup): View {
        return super.newView(context, cursor, parent)
    }

    override fun bindView(view: View, context: Context?, cursor: Cursor) {
        super.bindView(view, context, cursor)
        var holder: ViewHolder? = view.tag as ViewHolder?
        if (holder == null) {
            holder = ViewHolder()
            holder.colImp = cursor.getColumnIndexOrThrow(RemindersDbAdapter.COL_ISIMPORTANT)
            holder.listTab = view.findViewById(R.id.row_tab)
            view.tag = holder
        }
        if (cursor.getInt(holder.colImp) > 0) {
            holder.listTab?.setBackgroundColor(ContextCompat.getColor(context, R.color.orange))
        } else {
            holder.listTab?.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
    }

    internal class ViewHolder {
        var colImp: Int = 0
        //store the view
        var listTab: View? = null
    }
}