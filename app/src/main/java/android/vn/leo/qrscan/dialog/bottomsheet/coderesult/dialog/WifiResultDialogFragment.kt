package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.vn.leo.qrscan.databinding.FragmentDialogWifiResultBinding
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.helper.NetworkHelper
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class WifiResultDialogFragment :
    BaseCodeResultDialogFragment<FragmentDialogWifiResultBinding, BarcodeParsedResult.WifiResult>(),
    OnResultDialog {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDialogWifiResultBinding {
        return FragmentDialogWifiResultBinding.inflate(inflater, container, false)
    }

    override fun onRenderScanResult(result: BarcodeParsedResult.WifiResult) {
        Glide.with(this)
            .load(result.bitmap)
            .into(viewBinding.imageReviewScanResult)

        viewBinding.textViewScanResult.text = result.text

        viewBinding.buttonAccess.setOnClickListener {
            NetworkHelper.accessToWifiNetwork(
                requireContext(),
                result.ssid,
                result.password,
                result.isHidden
            )
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
        WifiResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, "WifiResultDialogFragment")
    }
}