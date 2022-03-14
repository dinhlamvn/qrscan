package android.vn.leo.qrscan.extensions

import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.color


fun TextView.setSpannableString(list: List<TextStyle>) {
    val spannableString = buildSpannedString {
        list.forEach { textStyle ->
            when (textStyle) {
                is TextStyle.Normal -> {
                    append(textStyle.text)
                }
                is TextStyle.ColorStyle -> {
                    color(textStyle.color) {
                        append(textStyle.text)
                    }
                }
            }
        }
    }

    setText(spannableString, TextView.BufferType.SPANNABLE)
}