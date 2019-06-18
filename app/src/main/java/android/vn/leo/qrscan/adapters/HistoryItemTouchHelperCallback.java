package android.vn.leo.qrscan.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class HistoryItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private OnItemSwipedOrMovedCallback callback;

    public HistoryItemTouchHelperCallback(OnItemSwipedOrMovedCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlag = ItemTouchHelper.START;
        return makeMovementFlags(0, swipeFlag);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (callback != null) {
            callback.onItemSwiped(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }
}
