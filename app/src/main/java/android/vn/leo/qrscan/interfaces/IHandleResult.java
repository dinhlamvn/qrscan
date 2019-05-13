package android.vn.leo.qrscan.interfaces;

import android.graphics.Bitmap;

public interface IHandleResult {

    /**
     * Show alert dialog with result text and image of QRCode or Barcode user scan
     * @param result Result text
     * @param bitmap Image of QRCode or Barcode
     */
    void showResult(final String result, final Bitmap bitmap);

    /**
     * Copy result to clipboard when user turn on auto copy from setting
     * @param result Barcode result user scan
     */
    void copyResult(final String result);

    /**
     * Update result scan to history screen, user will see this when swipe to screen
     * @param result Result text
     * @param bitmap Image of QRCode or Barcode
     */
    void updateResultToHistory(final String result, final Bitmap bitmap);

    /**
     * Save result to database
     * @param result Result text
     * @param bitmap Image of QRCode or Barcode
     */
    void saveResultToDatabase(final String result, final Bitmap bitmap);
}
