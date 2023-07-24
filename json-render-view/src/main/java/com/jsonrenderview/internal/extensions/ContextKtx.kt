package com.jsonrenderview.internal.extensions

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

@ColorInt
internal fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)

internal fun Context.font(@FontRes id: Int) = ResourcesCompat.getFont(this, id)

internal fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

