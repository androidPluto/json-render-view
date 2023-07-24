package com.srtvprateek.jsonrenderview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import com.srtvprateek.jsonrenderview.internal.OnRowClickListener
import com.srtvprateek.jsonrenderview.internal.RowView
import com.srtvprateek.jsonrenderview.internal.extensions.color
import com.srtvprateek.jsonrenderview.internal.spannable.createSpan
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class JsonRenderView : ScrollView {
    private var mJSONObject: JSONObject? = null
    private var mJSONArray: JSONArray? = null
    private val contentView: LinearLayout = LinearLayout(context)
    private val hasAlreadyBounded: Boolean
        get() = null != mJSONObject || null != mJSONArray

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        initView()
    }

    private var config = Config()

    private fun initView() {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.layoutParams = layoutParams
        contentView.orientation = LinearLayout.VERTICAL
        val horizontalScrollView = HorizontalScrollView(context)
        horizontalScrollView.layoutParams = layoutParams
        horizontalScrollView.isFillViewport = true
        horizontalScrollView.setPadding(0, 12, 0, 28)
        horizontalScrollView.addView(contentView)
        this.addView(horizontalScrollView)
    }

    fun applyConfig(config: Config) {
        this.config = config
    }

    fun bind(jsonStr: String) {
        require(!hasAlreadyBounded) { "JsonRenderView is already bound." }
        try {
            when (val data = JSONTokener(jsonStr).nextValue()) {
                is JSONObject -> mJSONObject = data
                is JSONArray -> mJSONArray = data
                else -> throw IllegalArgumentException("JSON string is invalid.")
            }
            createView()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun bind(jsonObject: JSONObject) {
        require(!hasAlreadyBounded) { "JsonRenderView is already bound." }
        this.mJSONObject = jsonObject
        createView()
    }

    fun bind(jsonArray: JSONArray) {
        require(!hasAlreadyBounded) { "JsonRenderView is already bound." }
        this.mJSONArray = jsonArray
        createView()
    }

    private fun createView() {
        val jsonView = RowView(context, config)
        jsonView.showIcon(true)
        jsonView.hideValue()
        val value = mJSONObject ?: mJSONArray
        jsonView.showKey(context.createSpan {
            append(
                italic(
                    fontColor(
                        if (value is JSONObject) "Object {...} " else "Array [...] ",
                        context.color(R.color.jrv__root_text_color)
                    )
                )
            )
        })
        jsonView.setOnClickListener(OnRowClickListener(value, jsonView, 0, config))
        contentView.addView(jsonView)
    }

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