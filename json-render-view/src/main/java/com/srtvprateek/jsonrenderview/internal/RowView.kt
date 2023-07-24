package com.srtvprateek.jsonrenderview.internal

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.srtvprateek.jsonrenderview.Config
import com.srtvprateek.jsonrenderview.R
import com.srtvprateek.jsonrenderview.databinding.JrvViewRowBinding
import com.srtvprateek.jsonrenderview.internal.extensions.color
import com.srtvprateek.jsonrenderview.internal.spannable.createSpan
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

    private fun showKeyInfo(text: CharSequence) {
        binding.keyInfo.visibility = VISIBLE
        binding.keyInfo.text = text
    }

    fun showKey(s: CharSequence?) {
        binding.key.visibility = VISIBLE
        binding.key.text = s
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        binding.root.setOnClickListener(listener)
    }

    fun expand() {
        binding.icon.tag = false
        binding.icon.callOnClick()
    }

    fun collapse() {
        binding.icon.tag = true
        binding.icon.callOnClick()
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
        val keySpan: CharSequence = context.createSpan {
            append(fontColor("\"$key\"", config.keyFieldColor))
            append(fontColor(" : ", context.color(R.color.jrv__colon_color)))
        }
        setIndentation(newHierarchy)
        hideIcon()
        when (value) {
            is JSONObject -> {
                val keySpan: CharSequence = context.createSpan {
                    append(fontColor("\"$key\"", config.keyObjectColor))
                    append(fontColor(" : ", context.color(R.color.jrv__colon_color)))
                }
                showIcon(true)
                setOnClickListener(OnRowClickListener(value, this, newHierarchy, config))
                showKeyInfo("{ ${value.length()} }")
                showKey(keySpan)
            }

            is JSONArray -> {
                val keySpan: CharSequence = context.createSpan {
                    append(fontColor("\"$key\"", config.keyObjectColor))
                    append(fontColor(" : ", context.color(R.color.jrv__colon_color)))
                }
                showIcon(true)
                setOnClickListener(OnRowClickListener(value, this, newHierarchy, config))
                showKeyInfo("[ ${value.length()} ]")
                showKey(keySpan)
            }

            else -> {
                val keySpan: CharSequence = context.createSpan {
                    append(fontColor("\"$key\"", config.keyFieldColor))
                    append(fontColor(" : ", context.color(R.color.jrv__colon_color)))
                }
                val valueColor: Int = when (value) {
                    is String -> config.valueStringColor
                    is Boolean -> config.valueBooleanColor
                    is Number -> config.valueNumberColor
                    else -> config.valueNullColor
                }
                showValue(context.createSpan {
                    append(fontColor("$value", valueColor))
                })
                showKey(keySpan)
            }
        }
    }

    private fun setIndentation(newHierarchy: Int) {
        val startMarginInPixels = resources.getDimensionPixelSize(R.dimen.jrv__indentation_width)
        binding.root.setPadding(
            startMarginInPixels * newHierarchy,
            binding.root.paddingTop,
            binding.root.paddingEnd,
            binding.root.paddingBottom
        )
    }
}