package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.net.Uri
import android.os.Bundle
import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.extensions.bindView
import android.vn.leo.qrscan.extensions.loadAsScanResult
import android.vn.leo.qrscan.model.BarcodeParsedResult
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentManager

class UrlResultDialogFragment : BaseCodeResultDialogFragment<BarcodeParsedResult.UrlResult>(),
    OnResultDialog {

    companion object {
        private const val TAG = "UrlResultDialogFragment"
    }

    private val ivScanResult: ImageView by bindView(R.id.image_review_scan_result)
    private val tvScanResult: TextView by bindView(R.id.text_view_scan_result)
    private val btnAccess: Button by bindView(R.id.button_access)
    private val btnDismiss: Button by bindView(R.id.button_dismiss)

    private val customTabBuilder = CustomTabsIntent.Builder()

    override val layoutRes: Int
        get() = R.layout.fragment_dialog_url_result

    override fun setupUI(result: BarcodeParsedResult.UrlResult) {
        result.bitmap.loadAsScanResult(this, ivScanResult)

        tvScanResult.text = result.url

        btnAccess.setOnClickListener {
            customTabBuilder.build()
                .launchUrl(requireContext(), Uri.parse(result.url))
        }

        btnDismiss.setOnClickListener {
            dismiss()
        }
    }

    override fun showDialog(
        fragmentManager: FragmentManager,
        argument: Bundle,
        dismissListener: OnDialogDismissListener?
    ) {
        UrlResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, TAG)
    }
}