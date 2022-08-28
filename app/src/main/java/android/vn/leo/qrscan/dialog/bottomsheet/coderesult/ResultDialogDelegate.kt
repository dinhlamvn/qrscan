package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.os.Bundle
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.fragment.app.FragmentManager

class ResultDialogDelegate(private val fragmentManager: FragmentManager) {

    private val resultDialogPicker = ResultDialogPicker()

    fun showResultDialog(result: BarcodeParsedResult) {
        val dialog = resultDialogPicker.getDialog(result)
        val bundle = Bundle().apply {
            putParcelable("result", result)
        }
        dialog.showDialog(fragmentManager, bundle)
    }
}