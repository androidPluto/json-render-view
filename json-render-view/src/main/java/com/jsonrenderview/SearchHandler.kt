package com.jsonrenderview

import android.util.Log
import android.view.ViewGroup

class SearchHandler(private val contentView: ViewGroup) {

    fun highlight(search: String): Int {
        return if(search.isEmpty()) {
            0
        } else {
            // todo traverse contentView & highlight text
            -1
        }
    }

    fun clear() {
        Log.d("prateek", "clear called")
    }

    fun navigateNext() {
        Log.d("prateek", "navigateNext called")
    }

    fun navigatePrevious() {
        Log.d("prateek", "navigatePrevious called")
    }

}
