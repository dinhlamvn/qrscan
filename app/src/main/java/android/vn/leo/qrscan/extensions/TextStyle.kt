package android.vn.leo.qrscan.extensions

import androidx.annotation.ColorInt

sealed class TextStyle(open val text: CharSequence) {
    data class Normal(override val text: CharSequence) : TextStyle(text)
    data class ColorStyle(@ColorInt val color: Int, override val text: CharSequence) :
        TextStyle(text)
}