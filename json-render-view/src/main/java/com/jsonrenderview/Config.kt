package com.jsonrenderview

import android.graphics.Color
import androidx.annotation.ColorInt

data class Config internal constructor(
    val keyFieldColor: Int = Color.parseColor("#9911141c"),
    val keyObjectColor: Int = Color.parseColor("#cc11141c"),
    val valueStringColor: Int = Color.parseColor("#f50057"),
    val valueNumberColor: Int = Color.parseColor("#1190bd"),
    val valueBooleanColor: Int = Color.parseColor("#ff00ff"),
    val valueNullColor: Int = Color.parseColor("#66231f40")
) {

    class Builder {

        @ColorInt private var _keyFieldColor: Int = Color.parseColor("#9911141c")
        @ColorInt private var _keyObjectColor: Int = Color.parseColor("#cc11141c")
        @ColorInt private var _valueStringColor: Int = Color.parseColor("#f50057")
        @ColorInt private var _valueNumberColor: Int = Color.parseColor("#1190bd")
        @ColorInt private var _valueBooleanColor: Int = Color.parseColor("#ff00ff")

        fun keyFieldColor(@ColorInt color: Int): Builder {
            _keyFieldColor = color
            return this
        }

        fun keyObjectColor(@ColorInt color: Int): Builder {
            _keyObjectColor = color
            return this
        }

        fun valueStringColor(@ColorInt color: Int): Builder {
            _valueStringColor = color
            return this
        }

        fun valueNumberColor(@ColorInt color: Int): Builder {
            _valueNumberColor = color
            return this
        }

        fun valueBooleanColor(@ColorInt color: Int): Builder {
            _valueBooleanColor = color
            return this
        }

        fun build(): Config {
            return Config(
                keyFieldColor = _keyFieldColor,
                keyObjectColor = _keyObjectColor,
                valueStringColor = _valueStringColor,
                valueNumberColor = _valueNumberColor,
                valueBooleanColor = _valueBooleanColor
            )
        }
    }
}
