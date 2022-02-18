package android.vn.leo.qrscan.interfaces;

import android.vn.leo.qrscan.data.ScanResult;

public interface OnExecuteResult {

    /**
     * Vibrate the device if user turn on feature from setting
     */
    void vibrate();

    /**
     * Sound the device if user turn on feature from setting
     */
    void sound();

    /**
     * Show alert dialog with result text and image of QRCode or Barcode user scan
     *
     * @param scanResult Result of code
     */
    void showResult(final ScanResult scanResult);

    /**
     * Copy result to clipboard when user turn on auto copy from setting
     *
     * @param scanResult Result of code
     */
    void copyResult(final ScanResult scanResult);

    /**
     * Update result scan to history screen, user will see this when swipe to screen
     *
     * @param scanResult Result of code
     */
    void updateResultToHistory(final ScanResult scanResult);

    /**
     * Save result to database
     *
     * @param scanResult Result text of code
     */
    void saveResultToDatabase(final ScanResult scanResult);
}
