package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog.*
import android.vn.leo.qrscan.model.BarcodeParsedResult

class ResultDialogPicker {
    fun getDialog(result: BarcodeParsedResult? = null): OnResultDialog? = when (result) {
        is BarcodeParsedResult.TextResult -> TextResultDialogFragment()
        is BarcodeParsedResult.UrlResult -> UrlResultDialogFragment()
        is BarcodeParsedResult.TelResult -> TelResultDialogFragment()
        is BarcodeParsedResult.SmsResult -> SMSResultDialogFragment()
        is BarcodeParsedResult.EmailResult -> EmailResultDialogFragment()
        is BarcodeParsedResult.WifiResult -> WifiResultDialogFragment()
        else -> TextResultDialogFragment()
    }
}