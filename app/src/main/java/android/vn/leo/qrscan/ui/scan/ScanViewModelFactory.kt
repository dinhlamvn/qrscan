package android.vn.leo.qrscan.ui.scan

import android.vn.leo.qrscan.parser.BarcodeResultParser
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScanViewModelFactory(private val barcodeResultParser: BarcodeResultParser) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScanViewModel(barcodeResultParser) as T
    }
}