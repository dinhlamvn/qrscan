package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.vn.leo.qrscan.databinding.FragmentDialogTextResultBinding
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.extensions.copyToClipboard
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class TextResultDialogFragment :
    BaseCodeResultDialogFragment<FragmentDialogTextResultBinding, BarcodeParsedResult.TextResult>(),
    OnResultDialog {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDialogTextResultBinding {
        return FragmentDialogTextResultBinding.inflate(inflater, container, false)
    }

    override fun onRenderScanResult(result: BarcodeParsedResult.TextResult) {
        Glide.with(this)
            .load(result.bitmap)
            .into(viewBinding.imageReviewScanResult)

        viewBinding.textViewScanResult.text = result.text

        viewBinding.buttonCopy.setOnClickListener {
            result.text.copyToClipboard(requireContext())
            dismiss()
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
        TextResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, "TextResultDialogFragment")
    }
}