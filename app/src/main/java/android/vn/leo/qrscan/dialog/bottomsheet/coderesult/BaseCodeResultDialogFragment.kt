package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.base.ui.BaseBottomSheetDialogFragment
import android.vn.leo.qrscan.extensions.showToast
import android.vn.leo.qrscan.model.BarcodeParsedResult

abstract class BaseCodeResultDialogFragment<T : BarcodeParsedResult> :
    BaseBottomSheetDialogFragment() {

    private val codeResult: T? by lazy {
        arguments?.getParcelable("result") as? T
    }

    protected abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeResult?.let(::setupUI)
    }

    abstract fun setupUI(result: T)

    protected fun startActivityWithImplicitIntent(intent: Intent) {
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            showToast(R.string.application_handle_not_found)
        }
    }
}