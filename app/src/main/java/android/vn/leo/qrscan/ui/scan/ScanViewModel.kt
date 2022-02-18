package android.vn.leo.qrscan.ui.scan

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

    private var isHandlingResult = false

    fun handleBarcodeResult(result: BarcodeResult?) = viewModelScope.launch(Dispatchers.IO) {
        isHandlingResult = true
        triggerLoading()
        val parsedResult =
            barcodeResultParser.parse(result) ?: return@launch triggerLoading(false)
        triggerLoading(false)
        _handleResultEvent.postValue(parsedResult)
    }

    private fun triggerLoading(showLoading: Boolean = true) =
        _loadingEvent.postValue(showLoading)

    fun validStateOnPause(block: () -> Unit) {
        if (isHandlingResult) return
        block.invoke()
    }

    fun validStateOnResume(block: () -> Unit) {
        if (isHandlingResult) return
        block.invoke()
    }
}