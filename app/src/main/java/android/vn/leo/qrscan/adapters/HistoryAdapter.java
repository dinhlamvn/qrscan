package android.vn.leo.qrscan.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.data.ResultManager;
import android.vn.leo.qrscan.interfaces.OnClickHistoryItemCallback;
import android.vn.leo.qrscan.utils.CommonMethod;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private OnClickHistoryItemCallback clickHistoryItemCallback;

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.list_history_item, viewGroup, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder viewHolder, int i) {
        final String result = ResultManager.getInstance().get(i).getResult();
        final Bitmap bitmap = ResultManager.getInstance().get(i).getImage();
        final DateFormat dfm = new SimpleDateFormat("E yyyy.MM.dd hh:mm a", Locale.getDefault());
        final String scanDate = dfm.format(ResultManager.getInstance().get(i).getDate());
        viewHolder.itemView.setBackgroundResource(R.drawable.list_history_item_bg);

        viewHolder.imgScan.setImageBitmap(bitmap);
        viewHolder.txtResult.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtResult.setText(result);
        viewHolder.txtDateScan.setText(scanDate);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickHistoryItemCallback != null) {
                    clickHistoryItemCallback.onClickHistoryItemCallback(result, bitmap);
                }
            }
        });

        viewHolder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethod.copyResultToClipboard(viewHolder.itemView.getContext(), result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ResultManager.getInstance().size();
    }

    public void addClickHistoryItemCallback(OnClickHistoryItemCallback onClickHistoryItemCallback) {
        this.clickHistoryItemCallback = onClickHistoryItemCallback;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        ImageView imgScan;
        TextView txtResult;
        ImageView btnCopy;
        TextView txtDateScan;

        HistoryViewHolder(View itemView) {
            super(itemView);
            imgScan = itemView.findViewById(R.id.result_image);
            txtResult = itemView.findViewById(R.id.result_text);
            btnCopy = itemView.findViewById(R.id.copy_button);
            txtDateScan = itemView.findViewById(R.id.date_text);
        }
    }
}
