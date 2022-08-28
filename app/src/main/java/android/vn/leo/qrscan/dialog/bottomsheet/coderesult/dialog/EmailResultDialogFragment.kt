package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.vn.leo.qrscan.databinding.FragmentDialogEmailResultBinding
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.extensions.loadAsScanResult
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.fragment.app.FragmentManager

class EmailResultDialogFragment :
    BaseCodeResultDialogFragment<FragmentDialogEmailResultBinding, BarcodeParsedResult.EmailResult>(),
    OnResultDialog {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDialogEmailResultBinding {
        return FragmentDialogEmailResultBinding.inflate(inflater, container, false)
    }

    override fun onRenderScanResult(result: BarcodeParsedResult.EmailResult) {
        result.bitmap.loadAsScanResult(this, viewBinding.imageReviewScanResult)

        viewBinding.textViewScanResult.text = result.text

        viewBinding.buttonSend.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.putStringArrayListExtra(Intent.EXTRA_EMAIL, ArrayList(result.receivers))
            intent.putStringArrayListExtra(Intent.EXTRA_CC, ArrayList(result.ccs))
            intent.putStringArrayListExtra(Intent.EXTRA_BCC, ArrayList(result.bcCs))
            intent.putExtra(Intent.EXTRA_SUBJECT, result.subject)
            intent.putExtra(Intent.EXTRA_TEXT, result.body)
            startActivity(intent)
        }

        viewBinding.buttonDismiss.setOnClickListener {
            dismiss()
        }
    }

    override fun showDialog(
        fragmentManager: FragmentManager,
        argument: Bundle
    ) {
        EmailResultDialogFragment().apply {
            this.arguments = argument
        }.show(fragmentManager, "EmailResultDialogFragment")
    }
}