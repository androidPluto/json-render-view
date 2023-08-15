package com.jsonrenderview.internal

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.jsonrenderview.Config
import com.jsonrenderview.R
import com.jsonrenderview.databinding.JrvViewRowBinding
import com.jsonrenderview.internal.extensions.color
import com.jsonrenderview.internal.spannable.createSpan
import com.jsonrenderview.internal.spannable.setSpan
import org.json.JSONArray
import org.json.JSONObject

internal class RowView(context: Context, private val config: Config) : LinearLayout(context) {

    private val binding = JrvViewRowBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        initView()
    }

    private fun initView() {
        orientation = VERTICAL
        binding.icon.visibility = GONE
        binding.key.visibility = GONE
        binding.value.visibility = GONE
    }

    private fun hideIcon() {
        binding.icon.visibility = GONE
    }

    fun showIcon(canExpand: Boolean) {
        binding.icon.visibility = VISIBLE
        binding.icon.setImageResource(if (canExpand) R.drawable.jrv__indicator_expand else R.drawable.jrv__indicator_collapse)
    }

    fun hideValue() {
        binding.value.visibility = GONE
    }

    private fun showValue(s: CharSequence?) {
        binding.value.visibility = VISIBLE
        binding.value.text = s
        binding.value.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun showChildCount(type: Type, count: Int) {
        binding.keyInfo.visibility = VISIBLE
        binding.keyInfo.setSpan {
            when (type) {
                is Type.Object -> {
                    append(bold("{ "))
                    append("$count")
                    append(bold(" }"))
                }
                is Type.Array -> {
                    append(bold("[ "))
                    append("$count")
                    append(bold(" ]"))
                }
            }
        }
    }

    fun showKey(s: CharSequence?) {
        binding.key.visibility = VISIBLE
        binding.key.text = s
    }

    fun expand() {
        tag = false
        performClick()
    }

    fun collapse() {
        tag = true
        performClick()
    }

    fun addViewNoInvalidate(child: View) {
        var params = child.layoutParams
        if (params == null) {
            params = generateDefaultLayoutParams()
            requireNotNull(params) { "generateDefaultLayoutParams() cannot return null" }
        }
        addViewInLayout(child, -1, params)
    }

    fun handleJsonObject(key: String, value: Any, hierarchy: Int) {
        val newHierarchy = hierarchy + 1
        setIndentation()
        hideIcon()
        val keyStringValue = "\"$key\""
        when (value) {
            is JSONObject -> {
                val keySpan: CharSequence = context.createSpan {
                    append(fontColor(keyStringValue, config.keyObjectColor))
                    append(fontColor(" : ", context.color(R.color.jrv__colon_color)))
                }
                showIcon(true)
                setOnClickListener(OnRowClickListener(value, this, newHierarchy, config))
                showChildCount(Type.Object, value.length())
                showKey(keySpan)
            }

            is JSONArray -> {
                val keySpan: CharSequence = context.createSpan {
                    append(fontColor(keyStringValue, config.keyObjectColor))
                    append(fontColor(" : ", context.color(R.color.jrv__colon_color)))
                }
                showIcon(true)
                setOnClickListener(OnRowClickListener(value, this, newHierarchy, config))
                showChildCount(Type.Array, value.length())
                showKey(keySpan)
            }

            else -> {
                val keySpan: CharSequence = context.createSpan {
                    append(fontColor(keyStringValue, config.keyFieldColor))
                    append(fontColor(" : ", context.color(R.color.jrv__colon_color)))
                }
                val valueColor: Int = when (value) {
                    is String -> config.valueStringColor
                    is Boolean -> config.valueBooleanColor
                    is Number -> config.valueNumberColor
                    else -> config.valueNullColor
                }
                showValue(
                    context.createSpan {
                        append(fontColor("$value", valueColor))
                    }
                )
                showKey(keySpan)
            }
        }
    }

    private fun setIndentation() {
        val startMarginInPixels = resources.getDimensionPixelSize(R.dimen.jrv__indentation_width)
        setPadding(
            startMarginInPixels,
            paddingTop,
            paddingEnd,
            paddingBottom
        )
    }

    sealed class Type {
        object Object : Type()
        object Array : Type()
    }
}
