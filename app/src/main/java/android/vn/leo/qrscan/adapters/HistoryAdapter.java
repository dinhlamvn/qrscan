package android.vn.leo.qrscan.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.data.ResultManager;
import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.interfaces.OnClickHistoryItemCallback;
import android.vn.leo.qrscan.utils.CommonMethod;
import android.vn.leo.qrscan.utils.FormatUtility;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemSwipedOrMovedCallback {

    private OnClickHistoryItemCallback clickHistoryItemCallback;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public interface OnChangeCallback {
        void onChange();
    }

    public HistoryAdapter(OnClickHistoryItemCallback callback) {
        this.clickHistoryItemCallback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        switch (viewType) {
            case 0: {
                View view = inflater.inflate(R.layout.list_history_date_group_item, viewGroup, false);
                return new HistoryWithDateGroupViewHolder(view);
            }
            default: {
                View view = inflater.inflate(R.layout.list_history_item, viewGroup, false);
                return new HistoryViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        final ScanResult item = ResultManager.getInstance().get(i);
        switch (viewHolder.getItemViewType()) {
            case 0: {
                HistoryWithDateGroupViewHolder holder = (HistoryWithDateGroupViewHolder) viewHolder;
                holder.bind(item);
                holder.itemView.setActivated(selectedItems.get(i, false));
                break;
            }
            default: {
                HistoryViewHolder holder = (HistoryViewHolder) viewHolder;
                holder.bind(item);
                holder.itemView.setActivated(selectedItems.get(i, false));
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        final DateFormat dfmGroup = new SimpleDateFormat("yyyy.MM.dd ", Locale.getDefault());
        String s1 = dfmGroup.format(ResultManager.getInstance().get(position).getDate());
        String s2 = dfmGroup.format(ResultManager.getInstance().get(position - 1).getDate());
        return s1.equals(s2) ? 1 : 0;
    }

    @Override
    public void onItemMoved(int start, int end) {

    }

    @Override
    public void onListDeleted() {
        clickHistoryItemCallback.onListItemRemoveConfirm(new OnChangeCallback() {
            @Override
            public void onChange() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemSwiped(final int position) {
        final ScanResult result = ResultManager.getInstance().remove(position);
        clickHistoryItemCallback.onItemRemoveConfirm(position, result, new OnChangeCallback() {
            @Override
            public void onChange() {
                notifyItemInserted(position);
            }
        });
    }

    public OnItemSwipedOrMovedCallback getItemSwipedOrMovedCallback() {
        return this;
    }

    @Override
    public int getItemCount() {
        return ResultManager.getInstance().size();
    }


    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

    /**
     * Count of item was selected
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Reset selected position in action mode
     */
    public void resetToggleSelectedItem() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    /**
     * Toggle item selection on action mode status
     *
     * @param position The position of item
     */
    public void toggleSelection(int position) {
        ScanResult result = ResultManager.getInstance().get(position);
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
            ResultManager.getInstance().clearItemInRemoved(position);
        } else {
            selectedItems.put(position, true);
            ResultManager.getInstance().addRemoveItem(position, result);
        }
        notifyItemChanged(position);
    }

    public void toggleLongClickSelection(int position) {
        toggleSelection(position);
        int min = position - 1;
        int max = position + 1;

        while (min >= 0) {
            if (selectedItems.get(min, false)) break;
            min--;
        }

        while (max < getItemCount()) {
            if (selectedItems.get(max, false)) break;
            max++;
        }

        if (min >= 0) {
            for (int i = position - 1; i > min; i--) {
                toggleSelection(i);
            }
        }

        if (max < getItemCount()) {
            for (int i = position + 1; i < max; i++) {
                toggleSelection(i);
            }
        }
    }

    /**
     * Count item was selected
     */
    public int getItemSelectedCount() {
        return selectedItems.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imgScan;
        TextView txtResult;
        TextView txtDateScan;
        TextView txtType;
        ImageView imgCopy;

        HistoryViewHolder(View itemView) {
            super(itemView);
            imgScan = itemView.findViewById(R.id.result_image);
            txtResult = itemView.findViewById(R.id.result_text);
            txtDateScan = itemView.findViewById(R.id.timestamp);
            txtType = itemView.findViewById(R.id.text_type);
            imgCopy = itemView.findViewById(R.id.iconCopy);
        }

        void bind(final ScanResult item) {
            final String result = item.getResult();
            final String scanDate = FormatUtility.formatTimeStamp(item.getDate());

            Glide.with(itemView.getContext())
                    .load(item.getImage())
                    .error(R.drawable.no_image)
                    .transform(new RoundedCorners(32))
                    .skipMemoryCache(false)
                    .into(imgScan);
            txtResult.setEllipsize(TextUtils.TruncateAt.END);
            txtResult.setText(result);
            txtDateScan.setText(scanDate);
            txtType.setText(FormatUtility.getTitleResultFromType(itemView.getContext(), item.getType()));

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (clickHistoryItemCallback != null) {
                        clickHistoryItemCallback.onLongClickItem(getAdapterPosition());
                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickHistoryItemCallback != null) {
                        clickHistoryItemCallback.onClickHistoryItemCallback(getAdapterPosition());
                    }
                }
            });

            imgCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethod.copyResultToClipboard(itemView.getContext(), item.getResult());
                }
            });
        }
    }

    class HistoryWithDateGroupViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCopy;
        TextView textDateGroup;
        ImageView imgScan;
        TextView txtResult;
        TextView txtDateScan;
        TextView txtType;


        HistoryWithDateGroupViewHolder(View itemView) {
            super(itemView);
            textDateGroup = itemView.findViewById(R.id.text_date_group);
            imgScan = itemView.findViewById(R.id.result_image);
            txtResult = itemView.findViewById(R.id.result_text);
            txtDateScan = itemView.findViewById(R.id.timestamp);
            txtType = itemView.findViewById(R.id.text_type);
            imgCopy = itemView.findViewById(R.id.iconCopy);
        }

        void bind(final ScanResult item) {
            final String result = item.getResult();
            final String scanDate = FormatUtility.formatTimeStamp(item.getDate());
            final String dateGroup = FormatUtility.formatDateGroup(item.getDate());

            textDateGroup.setText(dateGroup);
            Glide.with(itemView.getContext())
                    .load(item.getImage())
                    .error(R.drawable.no_image)
                    .transform(new RoundedCorners(32))
                    .skipMemoryCache(false)
                    .into(imgScan);
            txtResult.setEllipsize(TextUtils.TruncateAt.END);
            txtResult.setText(result);
            txtDateScan.setText(scanDate);
            txtType.setText(FormatUtility.getTitleResultFromType(itemView.getContext(), item.getType()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickHistoryItemCallback != null) {
                        clickHistoryItemCallback.onClickHistoryItemCallback(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (clickHistoryItemCallback != null) {
                        clickHistoryItemCallback.onLongClickItem(getAdapterPosition());
                    }
                    return true;
                }
            });

            imgCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethod.copyResultToClipboard(itemView.getContext(), item.getResult());
                }
            });
        }
    }
}
