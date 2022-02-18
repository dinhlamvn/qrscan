package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.os.Bundle
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.fragment.app.FragmentManager

interface OnResultDialog {

    fun showDialog(
        fragmentManager: FragmentManager,
        argument: Bundle,
        dismissListener: OnDialogDismissListener? = null,
        dialogNotFoundCallback: () -> Unit
    )
}