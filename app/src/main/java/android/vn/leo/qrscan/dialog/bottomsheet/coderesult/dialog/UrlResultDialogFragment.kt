package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.vn.leo.qrscan.databinding.FragmentDialogUrlResultBinding
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.extensions.loadAsScanResult
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentManager

class UrlResultDialogFragment :
    BaseCodeResultDialogFragment<FragmentDialogUrlResultBinding, BarcodeParsedResult.UrlResult>(),
    OnResultDialog {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDialogUrlResultBinding {
        return FragmentDialogUrlResultBinding.inflate(inflater, container, false)
    }

    private val customTabBuilder = CustomTabsIntent.Builder()

    override fun onRenderScanResult(result: BarcodeParsedResult.UrlResult) {
        result.bitmap.loadAsScanResult(this, viewBinding.imageReviewScanResult)

        viewBinding.textViewScanResult.text = result.url

        viewBinding.buttonAccess.setOnClickListener {
            customTabBuilder.build()
                .launchUrl(requireContext(), Uri.parse(result.url))
        }

        viewBinding.buttonDismiss.setOnClickListener {
            dismiss()
        }
    }

    override fun showDialog(
        fragmentManager: FragmentManager,
        argument: Bundle
    ) {
        UrlResultDialogFragment().apply {
            this.arguments = argument
        }.show(fragmentManager, "UrlResultDialogFragment")
    }
}