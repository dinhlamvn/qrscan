package android.vn.leo.qrscan.ui.scan

import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.model.BarcodeParsedResult
import android.vn.leo.qrscan.parser.BarcodeResultParser
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScanViewModel(private val barcodeResultParser: BarcodeResultParser) : ViewModel() {

    private val _handleResultEvent = MutableLiveData<BarcodeParsedResult>()
    val handleResultEvent: LiveData<BarcodeParsedResult> = _handleResultEvent

    private val _loadingEvent = MutableLiveData(false)
    val loadingEvent: LiveData<Boolean> = _loadingEvent

    private val _toastEvent = MutableLiveData(0)
    val toastEvent: LiveData<Int> = _toastEvent

    private var isHandlingResult = false

    fun handleBarcodeResult(result: BarcodeResult?) = viewModelScope.launch(Dispatchers.IO) {
        isHandlingResult = true
        toggleLoading()
        val parsedResult =
            barcodeResultParser.parse(result) ?: return@launch kotlin.run {
                toggleLoading()
                _toastEvent.postValue(R.string.error_scan_result)
            }
        toggleLoading()
        _handleResultEvent.postValue(parsedResult)
    }

    private fun toggleLoading() =
        _loadingEvent.postValue(_loadingEvent.value?.not() ?: true)

    fun validateBarcodeViewOnPause(block: () -> Unit) {
        if (isHandlingResult) return
        block.invoke()
    }

    fun validateBarcodeViewOnResume(block: () -> Unit) {
        if (isHandlingResult) return
        block.invoke()
    }
}