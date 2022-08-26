package android.vn.leo.qrscan.ui;

import android.os.Bundle;
import android.vn.leo.qrscan.R;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new android.vn.leo.qrscan.ui.scan.ScanFragment())
                .commit();
    }
}