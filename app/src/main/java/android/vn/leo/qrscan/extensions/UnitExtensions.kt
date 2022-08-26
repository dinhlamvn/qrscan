package android.vn.leo.qrscan.extensions

import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.roundToInt

fun Int.toDp(metrics: DisplayMetrics) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).roundToInt()