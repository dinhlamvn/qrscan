package android.vn.leo.qrscan.dialog.bottomsheet.coderesult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.vn.leo.qrscan.base.ui.BaseBottomSheetDialogFragment
import android.vn.leo.qrscan.model.BarcodeParsedResult
import androidx.viewbinding.ViewBinding

abstract class BaseCodeResultDialogFragment<VB : ViewBinding, T : BarcodeParsedResult> :
    BaseBottomSheetDialogFragment() {

    protected lateinit var viewBinding: VB

    protected abstract fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    private val codeResult: T? by lazy {
        arguments?.getParcelable("result") as? T
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = createViewBinding(inflater, container, savedInstanceState)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeResult?.let(::onRenderScanResult)
    }

    protected abstract fun onRenderScanResult(result: T)
}