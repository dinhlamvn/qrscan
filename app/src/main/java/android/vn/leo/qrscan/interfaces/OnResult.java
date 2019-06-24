package android.vn.leo.qrscan.interfaces;

import com.journeyapps.barcodescanner.BarcodeResult;

public interface OnResult {

    /**
     * Check if client disable handel with result scan
     */
    boolean isDisabled();

    /**
     * Check client has result and it was in handle
     * @return true if it is handle, otherwise false
     */
    boolean isOnResult();
    /**
     * Handle result after user scan
     * @param result Barcode result of user
     */
    void onResult(BarcodeResult result);

    /**
     *
     */
    void release();
}
