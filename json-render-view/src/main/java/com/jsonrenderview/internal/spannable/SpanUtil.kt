package com.jsonrenderview.internal.spannable

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.annotation.ColorInt
import com.jsonrenderview.internal.extensions.color
import com.jsonrenderview.internal.extensions.font
import com.srtvprateek.jsonrenderview.R
import java.text.Normalizer

internal inline fun TextView.setSpan(
    bufferType: TextView.BufferType? = null,
    spanBuilder: Builder.() -> Unit
) {
    val builder = Builder(context)
    builder.spanBuilder()
    if (bufferType != null) {
        setText(builder.build(), bufferType)
    } else {
        text = builder.build()
    }
}

internal inline fun Context.createSpan(spanBuilder: Builder.() -> Unit): CharSequence {
    val builder = Builder(this)
    builder.spanBuilder()
    return builder.build()
}

internal class Builder(private val context: Context) {
    private val spanBuilder = SpannableStringBuilder()

    fun append(text: String) {
        append(SpannableString(text))
    }

    fun append(span: Spannable) {
        spanBuilder.append(span)
    }

    fun append(span: CharSequence) {
        spanBuilder.append(span)
    }

    private fun span(s: CharSequence, o: Any) = when (s) {
        is String -> SpannableString(s).apply {
            setSpan(o, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        is SpannableStringBuilder -> s.apply {
            setSpan(o, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        is SpannableString -> s.apply {
            setSpan(o, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        else -> throw IllegalArgumentException("unhandled type $o")
    }

    fun bold(span: CharSequence): CharSequence {
        return span(span, getFontSpan(R.font.muli_bold))
    }

    fun fontColor(span: CharSequence, @ColorInt color: Int): CharSequence {
        return span(span, ForegroundColorSpan(color))
    }

    fun italic(span: CharSequence): CharSequence {
        return span(span, StyleSpan(Typeface.ITALIC))
    }

    fun highlight(span: CharSequence, search: String?): CharSequence {
        if (search.isNullOrEmpty()) return span
        val normalizedText = Normalizer.normalize(span, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()

        val startIndexes = normalizedText.allOccurrences(search)
        if (startIndexes.isNotEmpty()) {
            val highlighted: Spannable = SpannableString(span)
            startIndexes.forEach {
                highlighted.setSpan(
                    BackgroundColorSpan(context.color(R.color.jrv__text_highlight)),
                    it,
                    it + search.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return highlighted
        }
        return span
    }

    fun build(): CharSequence {
        return spanBuilder
    }

    private fun getFontSpan(font: Int) = CustomTypefaceSpan(context.font(font)!!)
}

private fun String.allOccurrences(search: String): ArrayList<Int> {
    val indexes = arrayListOf<Int>()
    var idx = 0
    while (indexOf(search, idx, true).also { idx = it } >= 0) {
        indexes.add(idx)
        idx++
    }
    return indexes
}
