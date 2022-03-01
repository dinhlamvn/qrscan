package android.vn.leo.qrscan.extensions

import android.os.Build
import android.telephony.PhoneNumberUtils
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatDate(format: String = "dd/MM/yyyy"): String =
    SimpleDateFormat(format, Locale.getDefault())
        .format(this)

fun String.formatDate(format: String = "dd/MM/yyyy"): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    val date = formatter.runCatching {
        parse(this@formatDate)
    }.getOrNull() ?: return ""
    return date.formatDate(format)
}

fun Number.format(
    locale: Locale = Locale.US,
    minimumIntegerDigits: Int = 1, // minimum of left number length
    maximumIntegerDigits: Int = Int.MAX_VALUE, // maximum of left number length
    minimumFractionDigits: Int = 2, // minimum of right number length
    maximumFractionDigits: Int = 5 // maximum of right number length
): String = NumberFormat.getInstance(locale).apply {
    this.minimumFractionDigits = minimumFractionDigits
    this.maximumFractionDigits = maximumFractionDigits
    this.minimumIntegerDigits = minimumIntegerDigits
    this.maximumIntegerDigits = maximumIntegerDigits
}.format(this)

fun Number.formatCurrency(locale: Locale = Locale.US): String =
    NumberFormat.getCurrencyInstance(locale)
        .format(this)

fun String.formatPhoneNumber(countryCodeIso: String = "VN"): String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        PhoneNumberUtils.formatNumber(
            this,
            PhoneNumberUtils.formatNumberToE164(this, countryCodeIso),
            countryCodeIso
        )
    } else {
        PhoneNumberUtils.formatNumber(this)
    }