package android.vn.leo.qrscan.interfaces;

import android.graphics.Bitmap;
import android.vn.leo.qrscan.adapters.HistoryAdapter;
import android.vn.leo.qrscan.data.ScanResult;

import java.util.List;

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

    /**
     * Callback function when user delete list item in action mode of history screen
     * @param positions The position of item
     * @param onChangeCallback Callback when user restore
     */
    void onListItemRemoveConfirm(HistoryAdapter.OnChangeCallback onChangeCallback);
}
