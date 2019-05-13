package android.vn.leo.qrscan.interfaces;

import android.graphics.Bitmap;

public interface OnClickHistoryItemCallback {

    /**
     * Callback function when user click item in history screen
     * @param result Result text
     * @param bitmap Image of QRCode or Barcode
     */
    void onClickHistoryItemCallback(String result, Bitmap bitmap);
}
