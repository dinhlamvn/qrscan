package android.vn.leo.qrscan.extensions

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.vn.leo.qrscan.model.Size
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

fun <T : View> Fragment.bindView(@IdRes viewId: Int): Lazy<T> =
    lazy { requireNotNull(view).findViewById(viewId) }

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

fun Fragment.screenSize(): Size =
    Resources.getSystem().displayMetrics.run { Size(widthPixels, heightPixels) }