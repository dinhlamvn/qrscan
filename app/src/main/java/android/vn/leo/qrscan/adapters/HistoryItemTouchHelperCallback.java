package android.vn.leo.qrscan.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

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
