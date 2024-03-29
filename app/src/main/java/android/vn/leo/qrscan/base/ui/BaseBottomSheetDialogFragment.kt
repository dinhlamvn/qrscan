package android.vn.leo.qrscan.base.ui

import android.content.DialogInterface
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val dismissListener = parentFragment as? OnDialogDismissListener ?: return
        dismissListener.onDismiss()
    }
}