package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.content.Intent
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
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class EmailResultDialogFragment : BaseCodeResultDialogFragment<BarcodeParsedResult.EmailResult>(),
    OnResultDialog {

    companion object {
        private const val TAG = "EmailResultDialogFragment"
    }

    private val ivScanResult: ImageView by bindView(R.id.image_review_scan_result)
    private val tvScanResult: TextView by bindView(R.id.text_view_scan_result)
    private val btnSend: Button by bindView(R.id.button_send)
    private val btnDismiss: Button by bindView(R.id.button_dismiss)

    override val layoutRes: Int
        get() = R.layout.fragment_dialog_email_result

    override fun setupUI(result: BarcodeParsedResult.EmailResult) {
        result.bitmap.loadAsScanResult(this, ivScanResult)

        tvScanResult.text = result.text

        btnSend.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "text/plain"
            intent.putStringArrayListExtra(Intent.EXTRA_EMAIL, ArrayList(result.receivers))
            intent.putStringArrayListExtra(Intent.EXTRA_CC, ArrayList(result.ccs))
            intent.putStringArrayListExtra(Intent.EXTRA_BCC, ArrayList(result.bcCs))
            intent.putExtra(Intent.EXTRA_SUBJECT, result.subject)
            intent.putExtra(Intent.EXTRA_TEXT, result.body)
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
        EmailResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, TAG)
    }
}