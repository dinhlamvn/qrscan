package android.vn.leo.qrscan;

import android.app.Application;
import android.vn.leo.qrscan.database.SQLiteHelper;
import android.vn.leo.qrscan.utils.LocalStorageManager;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LocalStorageManager.init(this);

        SQLiteHelper.init(this);
    }
}
