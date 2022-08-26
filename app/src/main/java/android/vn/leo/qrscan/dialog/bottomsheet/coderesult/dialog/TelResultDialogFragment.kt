package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.vn.leo.qrscan.databinding.FragmentDialogTelResultBinding
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.extensions.loadAsScanResult
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.fragment.app.FragmentManager

class TelResultDialogFragment :
    BaseCodeResultDialogFragment<FragmentDialogTelResultBinding, BarcodeParsedResult.TelResult>(),
    OnResultDialog {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDialogTelResultBinding {
        return FragmentDialogTelResultBinding.inflate(inflater, container, false)
    }

    override fun onRenderScanResult(result: BarcodeParsedResult.TelResult) {
        result.bitmap.loadAsScanResult(this, viewBinding.imageReviewScanResult)

        viewBinding.textViewScanResult.text = result.text

        viewBinding.buttonCall.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(result.uri)))
        }

        viewBinding.buttonDismiss.setOnClickListener {
            dismiss()
        }
    }

    override fun showDialog(
        fragmentManager: FragmentManager,
        argument: Bundle,
        dismissListener: OnDialogDismissListener?
    ) {
        TelResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, "TelResultDialogFragment")
    }
}