package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.os.Bundle
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.fragment.app.FragmentManager

class ResultDialogDelegate(private val fragmentManager: FragmentManager) {

    private val resultDialogPicker = ResultDialogPicker()

    fun showResultDialog(
        result: BarcodeParsedResult,
        dismissListener: OnDialogDismissListener? = null,
        dialogNotFoundCallback: () -> Unit
    ) {
        resultDialogPicker.result = result
        val dialog = resultDialogPicker.getDialog() ?: return dialogNotFoundCallback.invoke()
        val bundle = Bundle().apply {
            putParcelable("result", result)
        }
        dialog.showDialog(fragmentManager, bundle, dismissListener, dialogNotFoundCallback)
    }
}