package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.extensions.bindView
import android.vn.leo.qrscan.model.BarcodeParsedResult
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class SMSResultDialogFragment : BaseCodeResultDialogFragment<BarcodeParsedResult.SmsResult>(),
    OnResultDialog {

    companion object {
        private const val TAG = "SMSResultDialogFragment"
    }

    private val ivScanResult: ImageView by bindView(R.id.image_review_scan_result)
    private val tvScanResult: TextView by bindView(R.id.text_view_scan_result)
    private val btnSend: Button by bindView(R.id.button_send)
    private val btnDismiss: Button by bindView(R.id.button_dismiss)

    override val layoutRes: Int
        get() = R.layout.fragment_dialog_sms_result

    override fun setupUI(result: BarcodeParsedResult.SmsResult) {
        Glide.with(this)
            .load(result.bitmap)
            .into(ivScanResult)

        tvScanResult.text = result.text

        btnSend.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(result.smsUri))
            intent.type = "text/plain"
            intent.putExtra("subject", result.subject)
            intent.putExtra("sms_body", result.body)
            startActivityWithImplicitIntent(intent)
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
        SMSResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, TAG)
    }
}