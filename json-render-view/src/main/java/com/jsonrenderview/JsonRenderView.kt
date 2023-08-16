package com.jsonrenderview

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import com.jsonrenderview.internal.OnRowClickListener
import com.jsonrenderview.internal.RowView
import com.jsonrenderview.internal.extensions.color
import com.jsonrenderview.internal.spannable.createSpan
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
        context, attrs, defStyleAttr
    )

    init {
        initView()
    }

    private var config = Config()
    val action = ActionHandler(contentView)
    val search = SearchHandler(contentView)

    private fun initView() {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.layoutParams = layoutParams
        contentView.orientation = LinearLayout.VERTICAL
        this.addView(
            HorizontalScrollView(context).apply {
                this.layoutParams = layoutParams
                isFillViewport = true
                setPadding(PADDING_START, PADDING_TOP, PADDING_END, PADDING_BOTTOM)
                addView(contentView)
            }
        )
    }

    fun applyConfig(config: Config) {
        this.config = config
    }

    fun bind(jsonStr: String) {
        checkAlreadyBound()
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
        checkAlreadyBound()
        this.mJSONObject = jsonObject
        createView()
    }

    fun bind(jsonArray: JSONArray) {
        checkAlreadyBound()
        this.mJSONArray = jsonArray
        createView()
    }

    private fun checkAlreadyBound() {
        require(!hasAlreadyBounded) { "JsonRenderView is already bound." }
    }

    private fun createView() {
        val value = mJSONObject ?: mJSONArray
        val rootKey = if (value is JSONObject) "Object {...} " else "Array [...] "
        contentView.addView(
            RowView(context, config).apply {
                showIcon(true)
                hideValue()
                showKey(
                    context.createSpan {
                        append(italic(fontColor(rootKey, context.color(R.color.jrv__root_text_color))))
                    }
                )
                setOnClickListener(OnRowClickListener(value, this, 0, config))
            }
        )
    }

    private companion object {
        const val PADDING_START = 0
        const val PADDING_TOP = 12
        const val PADDING_END = 0
        const val PADDING_BOTTOM = 28
    }
}
