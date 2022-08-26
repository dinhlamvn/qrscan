package android.vn.leo.qrscan.extensions

import android.content.res.Resources
import android.util.TypedValue
import android.vn.leo.qrscan.model.Size

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

fun screenSize(): Size =
    Resources.getSystem().displayMetrics.run { Size(widthPixels, heightPixels) }