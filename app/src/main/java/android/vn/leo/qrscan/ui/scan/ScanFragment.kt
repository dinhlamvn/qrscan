package android.vn.leo.qrscan.ui.scan

import android.os.Bundle
import android.view.View
import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.base.ui.BaseFragment
import android.vn.leo.qrscan.dialog.bottomsheet.coderesult.ResultDialogDelegate
import android.vn.leo.qrscan.extensions.bindView
import android.vn.leo.qrscan.extensions.screenSize
import android.vn.leo.qrscan.extensions.showToast
import android.vn.leo.qrscan.extensions.toPx
import android.vn.leo.qrscan.model.BarcodeParsedResult
import android.vn.leo.qrscan.parser.BarcodeResultParser
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.Size

class ScanFragment : BaseFragment(), BarcodeCallback, DecoratedBarcodeView.TorchListener {

    override val layoutRes: Int
        get() = R.layout.fragment_scan

    private val barcodeView: BarcodeView by bindView(R.id.zxing_barcode_surface)
    private val decoratedBarcodeView: DecoratedBarcodeView by bindView(R.id.zxing_barcode_scanner)
    private val loadingView: View by bindView(R.id.loading_view)

    private val scanViewModelFactory = ScanViewModelFactory(BarcodeResultParser())

    private val scanViewModel: ScanViewModel by lazy {
        ViewModelProvider(this, scanViewModelFactory)[ScanViewModel::class.java]
    }

    private val resultDialogDelegate: ResultDialogDelegate by lazy {
        ResultDialogDelegate(childFragmentManager)
    }

    override fun setupUI(view: View, savedInstanceState: Bundle?) {
        barcodeView.framingRectSize = Size(screenSize().width - 64.toPx(), 400.toPx())
        decoratedBarcodeView.decodeContinuous(this)
        decoratedBarcodeView.setTorchListener(this)
        scanViewModel.handleResultEvent.observe(viewLifecycleOwner, ::handleBarcodeParsedResult)
        scanViewModel.loadingEvent.observe(viewLifecycleOwner, ::handleLoading)
        scanViewModel.toastEvent.observe(viewLifecycleOwner, ::showToast)
    }

    private fun handleBarcodeParsedResult(barcodeParsedResult: BarcodeParsedResult) {
        val blockOnDismiss = {
            Toast.makeText(requireContext(), R.string.error_scan_result, Toast.LENGTH_SHORT).show()
            decoratedBarcodeView.resume()
        }
        resultDialogDelegate.showResultDialog(barcodeParsedResult, blockOnDismiss, blockOnDismiss)
    }

    private fun handleLoading(showLoading: Boolean) {
        loadingView.isVisible = showLoading
    }

    override fun barcodeResult(result: BarcodeResult?) {
        decoratedBarcodeView.pauseAndWait()
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
            decoratedBarcodeView.pauseAndWait()
        }
    }

    override fun onResume() {
        super.onResume()
        scanViewModel.validateBarcodeViewOnResume {
            decoratedBarcodeView.resume()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        decoratedBarcodeView.decodeContinuous(null)
        decoratedBarcodeView.setTorchListener(null)
    }
}