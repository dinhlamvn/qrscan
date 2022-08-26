package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.os.Bundle
import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.extensions.bindView
import android.vn.leo.qrscan.helper.NetworkHelper
import android.vn.leo.qrscan.model.BarcodeParsedResult
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class WifiResultDialogFragment : BaseCodeResultDialogFragment<BarcodeParsedResult.WifiResult>(),
    OnResultDialog {

    companion object {
        private const val TAG = "WifiResultDialogFragment"
    }

    private val ivScanResult: ImageView by bindView(R.id.image_review_scan_result)
    private val tvScanResult: TextView by bindView(R.id.text_view_scan_result)
    private val btnAccess: Button by bindView(R.id.button_access)
    private val btnDismiss: Button by bindView(R.id.button_dismiss)

    override val layoutRes: Int
        get() = R.layout.fragment_dialog_wifi_result

    override fun setupUI(result: BarcodeParsedResult.WifiResult) {
        Glide.with(this)
            .load(result.bitmap)
            .into(ivScanResult)

        tvScanResult.text = result.text

        btnAccess.setOnClickListener {
            NetworkHelper.accessToWifiNetwork(
                requireContext(),
                result.ssid,
                result.password,
                result.isHidden
            )
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
        WifiResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, TAG)
    }
}