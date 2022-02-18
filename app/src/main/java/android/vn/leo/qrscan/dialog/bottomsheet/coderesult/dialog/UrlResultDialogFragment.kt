package android.vn.leo.qrscan.dialog.bottomsheet.coderesult.dialog

import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.BaseCodeResultDialogFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.OnResultDialog
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.extensions.bindView
import android.vn.leo.qrscan.model.BarcodeParsedResult
import android.vn.leo.qrscan.utils.CommonMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

class UrlResultDialogFragment : BaseCodeResultDialogFragment<BarcodeParsedResult.UrlResult>(), OnResultDialog {

    private val ivScanResult: ImageView by bindView(R.id.image_review_scan_result)
    private val tvScanResult: TextView by bindView(R.id.text_view_scan_result)
    private val btnCopyAndClose: Button by bindView(R.id.button_copy)
    private val btnDismiss: Button by bindView(R.id.button_dismiss)

    override val layoutRes: Int
        get() = R.layout.fragment_dialog_text_result

    override fun setupUI(result: BarcodeParsedResult.UrlResult) {
        Glide.with(this)
            .load(result.bitmap)
            .into(ivScanResult)

        tvScanResult.text = result.url

        btnCopyAndClose.setOnClickListener {
            CommonMethod.copyResultToClipboard(requireContext(), result.text)
            dismiss()
        }

        btnDismiss.setOnClickListener {
            dismiss()
        }
    }

    override fun showDialog(
        fragmentManager: FragmentManager,
        dismissListener: OnDialogDismissListener?,
        dialogNotFoundCallback: () -> Unit
    ) {

    }
}