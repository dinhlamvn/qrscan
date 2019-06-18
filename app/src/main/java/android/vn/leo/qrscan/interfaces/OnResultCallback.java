package android.vn.leo.qrscan.interfaces;

import com.journeyapps.barcodescanner.BarcodeResult;

public interface OnResultCallback {

    /**
     * Check if client disable handel with result scan
     */
    boolean isDisableHandel();

    /**
     * Check client has result and it was in handle
     * @return true if it is handle, otherwise false
     */
    boolean isHandlingResult();
    /**
     * Handle result after user scan
     * @param result Barcode result of user
     */
    void onResult(BarcodeResult result);

    /**
     *
     */
    void finishHandleResult();
}
