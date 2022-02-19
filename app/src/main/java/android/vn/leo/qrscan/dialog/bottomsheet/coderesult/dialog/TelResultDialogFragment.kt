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
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class TelResultDialogFragment : BaseCodeResultDialogFragment<BarcodeParsedResult.TelResult>(),
    OnResultDialog {

    companion object {
        private const val TAG = "TelResultDialogFragment"
    }

    private val ivScanResult: ImageView by bindView(R.id.image_review_scan_result)
    private val tvScanResult: TextView by bindView(R.id.text_view_scan_result)
    private val btnCall: Button by bindView(R.id.button_call)
    private val btnDismiss: Button by bindView(R.id.button_dismiss)

    private val customTabBuilder = CustomTabsIntent.Builder()

    override val layoutRes: Int
        get() = R.layout.fragment_dialog_tel_result

    override fun setupUI(result: BarcodeParsedResult.TelResult) {
        Glide.with(this)
            .load(result.bitmap)
            .into(ivScanResult)

        tvScanResult.text = result.url

        btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse(result.uri)
            startActivity(intent)
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
        TelResultDialogFragment().apply {
            this.dismissListener = dismissListener
            this.arguments = argument
        }.show(fragmentManager, TAG)
    }
}