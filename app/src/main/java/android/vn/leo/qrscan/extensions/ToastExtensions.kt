package android.vn.leo.qrscan.extensions

import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.showToast(
    text: CharSequence?,
    duration: Int = Toast.LENGTH_SHORT
) {
    text?.takeIf { it.isNotBlank() } ?: return
    Toast(requireContext()).apply {
        setText(text)
        setDuration(duration)
    }.show()
}

fun Fragment.showToast(
    @StringRes textRes: Int,
    duration: Int = Toast.LENGTH_SHORT
) {
    if (textRes == 0) return
    showToast(getString(textRes), duration)
}