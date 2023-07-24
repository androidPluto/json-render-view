package com.jsonrenderview.internal

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.jsonrenderview.Config
import org.json.JSONArray
import org.json.JSONObject

internal class OnRowClickListener(
    private var value: Any?,
    private val view: RowView,
    private val hierarchy: Int,
    private val config: Config
) : View.OnClickListener {

    private var isExpand = true
    private var isJsonArray: Boolean = value != null && value is JSONArray

    override fun onClick(v: View) {
        if (v.tag is Boolean) {
            isExpand = v.tag as Boolean
            v.tag = null
        }
        if (view.childCount == 1) {
            (if (isJsonArray) value as JSONArray? else (value as JSONObject?)?.names())?.let {
                isExpand = false

                for (i in 0 until it.length()) {
                    val childItemView = RowView(view.context, config)
                    val childValue = it.opt(i)
                    if (isJsonArray) {
                        childItemView.handleJsonObject("$i", childValue, hierarchy)
                    } else {
                        (value as JSONObject?)?.opt(childValue as String)?.let { value ->
                            childItemView.handleJsonObject(childValue, value, hierarchy)
                        }
                    }
                    view.addViewNoInvalidate(childItemView)
                }
            } ?: run {
                isExpand = !isExpand
            }
            view.showIcon(isExpand)
            view.requestLayout()
            view.invalidate()
        } else {
            isExpand = !isExpand
            view.showIcon(isExpand)
            for (i in 1 until view.childCount) {
                view.getChildAt(i).visibility = if (!isExpand) VISIBLE else GONE
            }
        }
    }
}