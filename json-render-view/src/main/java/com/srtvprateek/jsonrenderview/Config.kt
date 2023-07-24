package com.srtvprateek.jsonrenderview

import android.graphics.Color
import androidx.annotation.ColorInt

class Config internal constructor() {
    @ColorInt internal var keyFieldColor: Int = Color.parseColor("#9911141c")
    @ColorInt internal var keyObjectColor: Int = Color.parseColor("#cc11141c")
    @ColorInt internal var valueStringColor: Int = Color.parseColor("#f50057")
    @ColorInt internal var valueNumberColor: Int = Color.parseColor("#1190bd")
    @ColorInt internal var valueBooleanColor: Int = Color.parseColor("#ff00ff")
    @ColorInt internal val valueNullColor: Int = Color.parseColor("#66231f40")

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
            return Config().apply {
                this.keyFieldColor = _keyFieldColor
                this.keyObjectColor = _keyObjectColor
                this.valueStringColor = _valueStringColor
                this.valueNumberColor = _valueNumberColor
                this.valueBooleanColor = _valueBooleanColor
            }
        }
    }
}
