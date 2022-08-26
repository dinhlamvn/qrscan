package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.databinding.FragmentDialogSmsResultBinding
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.extensions.showToast
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class SMSResultDialogFragment :
    BaseCodeResultDialogFragment<FragmentDialogSmsResultBinding, BarcodeParsedResult.SmsResult>(),
    OnResultDialog {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDialogSmsResultBinding {
        return FragmentDialogSmsResultBinding.inflate(inflater, container, false)
    }

    @SuppressLint("IntentReset")
    override fun onRenderScanResult(result: BarcodeParsedResult.SmsResult) {
        Glide.with(this)
            .load(result.bitmap)
            .into(viewBinding.imageReviewScanResult)

        viewBinding.textViewScanResult.text = result.text

        viewBinding.buttonSend.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(result.smsUri))
            intent.type = "text/plain"
            intent.putExtra("subject", result.subject)
            intent.putExtra("sms_body", result.body)
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            } else {
                showToast(R.string.application_handle_not_found)
            }
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
        SMSResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, "SMSResultDialogFragment")
    }
}