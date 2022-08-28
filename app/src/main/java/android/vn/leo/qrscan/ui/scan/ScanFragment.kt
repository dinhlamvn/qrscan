package android.vn.leo.qrscan.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.base.ui.BaseFragment
import android.vn.leo.qrscan.databinding.FragmentScanBinding
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.ResultDialogDelegate
import android.vn.leo.qrscan.dialog.listener.OnDialogDismissListener
import android.vn.leo.qrscan.extensions.screenSize
import android.vn.leo.qrscan.extensions.showToast
import android.vn.leo.qrscan.extensions.toPx
import android.vn.leo.qrscan.model.BarcodeParsedResult
import android.vn.leo.qrscan.parser.BarcodeResultParser
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.Size

class ScanFragment : BaseFragment<FragmentScanBinding>(), BarcodeCallback,
    DecoratedBarcodeView.TorchListener, OnDialogDismissListener {

    override fun onDismiss() {
        viewBinding.zxingBarcodeScanner.resume()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentScanBinding {
        return FragmentScanBinding.inflate(inflater, container, false)
    }

    private val scanViewModelFactory = ScanViewModelFactory(BarcodeResultParser())

    private val scanViewModel: ScanViewModel by lazy {
        ViewModelProvider(this, scanViewModelFactory)[ScanViewModel::class.java]
    }

    private val resultDialogDelegate: ResultDialogDelegate by lazy {
        ResultDialogDelegate(childFragmentManager)
    }

    override fun onRenderView(view: View, savedInstanceState: Bundle?) {
        viewBinding.zxingBarcodeScanner.barcodeView.framingRectSize =
            Size(screenSize().width - 64.toPx(), 400.toPx())
        viewBinding.zxingBarcodeScanner.decodeContinuous(this)
        viewBinding.zxingBarcodeScanner.setTorchListener(this)
        scanViewModel.handleResultEvent.observe(viewLifecycleOwner, ::handleBarcodeParsedResult)
        scanViewModel.loadingEvent.observe(viewLifecycleOwner, ::handleLoading)
        scanViewModel.toastEvent.observe(viewLifecycleOwner, ::showToast)
    }

    private fun handleBarcodeParsedResult(barcodeParsedResult: BarcodeParsedResult) {
        resultDialogDelegate.showResultDialog(barcodeParsedResult)
    }

    private fun handleLoading(showLoading: Boolean) {
        viewBinding.progressCircular.isVisible = showLoading
    }

    override fun barcodeResult(result: BarcodeResult?) {
        viewBinding.zxingBarcodeScanner.pauseAndWait()
        scanViewModel.handleBarcodeResult(result)
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {

    }

    override fun onTorchOn() {
        showToast(R.string.light_flash_on)
    }

    override fun onTorchOff() {
        showToast(R.string.light_flash_on)
    }

    override fun onPause() {
        super.onPause()
        scanViewModel.validateBarcodeViewOnPause {
            viewBinding.zxingBarcodeScanner.pauseAndWait()
        }
    }

    override fun onResume() {
        super.onResume()
        scanViewModel.validateBarcodeViewOnResume {
            viewBinding.zxingBarcodeScanner.resume()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.zxingBarcodeScanner.decodeContinuous(null)
        viewBinding.zxingBarcodeScanner.setTorchListener(null)
    }
}