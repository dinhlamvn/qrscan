package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.os.Bundle
import androidx.fragment.app.FragmentManager

interface OnResultDialog {

    fun showDialog(
        fragmentManager: FragmentManager,
        argument: Bundle
    )
}