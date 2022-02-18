package android.vn.leo.qrscan;

import android.vn.leo.qrscan.database.SQLiteHelper;
import android.vn.leo.qrscan.utils.LocalStorageManager;

import androidx.multidex.MultiDexApplication;

public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        LocalStorageManager.init(this);

        SQLiteHelper.init(this);
    }
}
