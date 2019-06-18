package android.vn.leo.qrscan.interfaces;

import android.graphics.Bitmap;
import android.vn.leo.qrscan.adapters.HistoryAdapter;
import android.vn.leo.qrscan.data.ScanResult;

public interface OnClickHistoryItemCallback {

    /**
     * Callback function when user click item in history screen
     * @param position Position of item
     */
    void onClickHistoryItemCallback(int position);

    /**
     * Callback function when user remove a item in list and show dialog to confirm this
     * @param position the position of item
     * @param item the item user removed
     */
    void onItemRemoveConfirm(int position, ScanResult item, HistoryAdapter.OnChangeCallback onChangeCallback);

    /**
     * Callback when long click a item in history screen
     * @param position the position of item
     */
    void onLongClickItem(int position);
}
