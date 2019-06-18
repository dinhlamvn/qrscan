package android.vn.leo.qrscan.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.vn.leo.qrscan.data.ResultManager;
import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.database.SQLiteHelper;

import java.util.List;

public class SyncResultService extends IntentService {

    public static final int TIME_TO_SYNC_REMOVED = 2000;

    public SyncResultService() {
        super("SyncResultService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final List<ScanResult> removedList = ResultManager.getInstance().getRemovedList();
                    for (ScanResult it : removedList) {
                        removeItem(it);
                    }
                }
            }, TIME_TO_SYNC_REMOVED);
        }
    }

    protected void removeItem(final ScanResult item) {

    }
}
