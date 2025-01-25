package huce.fit.appreadstories.adapters

import android.view.View

fun interface ClickListener {
    fun onItemClick(position: Int, view: View, isLongClick: Boolean)
}