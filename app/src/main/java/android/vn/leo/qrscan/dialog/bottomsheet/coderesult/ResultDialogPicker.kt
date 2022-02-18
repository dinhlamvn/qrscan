package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog.TextResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog.UrlResultDialogFragment
import android.vn.leo.qrscan.model.BarcodeParsedResult

class ResultDialogPicker {
    var result: BarcodeParsedResult? = null

    fun getDialog(): OnResultDialog? = when (result) {
        is BarcodeParsedResult.TextResult -> TextResultDialogFragment()
        is BarcodeParsedResult.UrlResult -> UrlResultDialogFragment()
        else -> null
    }
}