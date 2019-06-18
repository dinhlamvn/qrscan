package android.vn.leo.qrscan.adapters;

public interface OnItemSwipedOrMovedCallback {

    void onItemSwiped(int position);

    void onItemMoved(int start, int end);
}
