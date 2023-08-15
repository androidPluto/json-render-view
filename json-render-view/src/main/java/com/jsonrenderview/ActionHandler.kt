package com.jsonrenderview

import android.view.ViewGroup
import com.jsonrenderview.internal.RowView

class ActionHandler(private val contentView: ViewGroup) {

    fun expand() {
        clickAllView(contentView, false)
    }

    fun collapse() {
        clickAllView(contentView, true)
    }

    private fun clickAllView(viewGroup: ViewGroup, collapse: Boolean) {
        if (viewGroup is RowView) {
            operationJsonView(viewGroup, collapse)
        }
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (viewGroup is RowView) {
                operationJsonView(viewGroup, collapse)
            }
            if (view is ViewGroup) {
                clickAllView(view, collapse)
            }
        }
    }

    private fun operationJsonView(jsonView: RowView?, collapse: Boolean) {
        if (collapse) {
            jsonView?.expand()
        } else {
            jsonView?.collapse()
        }
    }
}
