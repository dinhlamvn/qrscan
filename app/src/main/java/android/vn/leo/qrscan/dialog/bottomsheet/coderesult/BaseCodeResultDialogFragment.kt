package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.vn.leo.qrscan.base.ui.BaseBottomSheetDialogFragment
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
}