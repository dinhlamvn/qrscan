package android.vn.leo.qrscan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_CALL_PHONE = 1111;
    public static final int REQUEST_WEB_BROWSER = 1112;
    public static final int REQUEST_SEND_MAIL = 1113;
    public static final int REQUEST_SEND_SMS = 1114;
    public static final int REQUEST_ADD_CONTACT = 1115;

    @IntDef({
            REQUEST_CALL_PHONE,
            REQUEST_WEB_BROWSER,
            REQUEST_SEND_SMS,
            REQUEST_SEND_MAIL,
            REQUEST_ADD_CONTACT
    })@Retention(RetentionPolicy.SOURCE)
    @interface RequestHandelCode { }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
    }

    protected abstract int getLayoutResource();

    protected void startActivityWithRequestCode(Intent intent, @RequestHandelCode int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    protected void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
