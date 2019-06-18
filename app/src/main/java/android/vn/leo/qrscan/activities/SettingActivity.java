package android.vn.leo.qrscan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.utils.LocalStorageManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String AUTO_COPY = "auto_copy";
    public static final String AUTO_MAKE_CALL = "auto_make_call";
    public static final String AUTO_ACCESS_URL = "auto_access_url";
    public static final String ENABLE_SOUND = "enable_sound";
    public static final String ENABLE_VIBRATE = "enable_vibrate";
    public static final String ENABLE_SAVE_CODE_IMAGE = "enable_save_code_image";

    private boolean[] changes = new boolean[6];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setUpForActionBar();

        boolean isAutoCopy = LocalStorageManager.isAutoCopyAfterScan();
        changes[0] = isAutoCopy;
        final CheckBox ckbAutoCopy = findViewById(R.id.checkbox_auto_copy_result);
        ckbAutoCopy.setTag(AUTO_COPY);
        ckbAutoCopy.setOnCheckedChangeListener(this);
        ckbAutoCopy.setChecked(isAutoCopy);

        boolean isAutoMakeCall = LocalStorageManager.isAutoCallToPhoneNumber();
        changes[1] = isAutoMakeCall;
        final CheckBox ckbAutoMakeCall = findViewById(R.id.checkbox_auto_make_call);
        ckbAutoMakeCall.setTag(AUTO_MAKE_CALL);
        ckbAutoMakeCall.setOnCheckedChangeListener(this);
        ckbAutoMakeCall.setChecked(isAutoMakeCall);

        boolean isAutoAccessUrl = LocalStorageManager.isAutoOpenWebBrowser();
        changes[2] = isAutoAccessUrl;
        final CheckBox ckbAutoUrl = findViewById(R.id.checkbox_auto_access_url);
        ckbAutoUrl.setTag(AUTO_ACCESS_URL);
        ckbAutoUrl.setOnCheckedChangeListener(this);
        ckbAutoUrl.setChecked(isAutoAccessUrl);

        boolean isEnableSound = LocalStorageManager.isEnableSound();
        changes[3] = isEnableSound;
        final CheckBox ckbEnableSound = findViewById(R.id.checkbox_sound);
        ckbEnableSound.setTag(ENABLE_SOUND);
        ckbEnableSound.setOnCheckedChangeListener(this);
        ckbEnableSound.setChecked(isEnableSound);

        boolean isEnableVibrate = LocalStorageManager.isEnableVibrate();
        changes[4] = isEnableVibrate;
        final CheckBox ckbEnableVibrate = findViewById(R.id.checkbox_vibrate);
        ckbEnableVibrate.setTag(ENABLE_VIBRATE);
        ckbEnableVibrate.setOnCheckedChangeListener(this);
        ckbEnableVibrate.setChecked(isEnableVibrate);

        boolean isEnableSaveCodeImage = LocalStorageManager.isEnableSaveCodeImage();
        changes[5] = isEnableSaveCodeImage;
        final CheckBox ckbEnableSaveCodeImage = findViewById(R.id.checkbox_save_image_code);
        ckbEnableSaveCodeImage.setTag(ENABLE_SAVE_CODE_IMAGE);
        ckbEnableSaveCodeImage.setOnCheckedChangeListener(this);
        ckbEnableSaveCodeImage.setChecked(isEnableSaveCodeImage);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String tag = (String) buttonView.getTag();

        if (tag == null) {
            return;
        }

        switch (tag) {
            case AUTO_COPY: {
                LocalStorageManager.setAutoCopyAfterScan(isChecked);
                break;
            }
            case AUTO_MAKE_CALL: {
                LocalStorageManager.setAutoOpenCallToPhoneNumber(isChecked);
                break;
            }
            case AUTO_ACCESS_URL: {
                LocalStorageManager.setAutoOpenWebBrowser(isChecked);
                break;
            }
            case ENABLE_SOUND: {
                LocalStorageManager.setEnableSound(isChecked);
                break;
            }
            case ENABLE_VIBRATE: {
                LocalStorageManager.setEnableVibrate(isChecked);
                break;
            }
            case ENABLE_SAVE_CODE_IMAGE: {
                LocalStorageManager.setEnableSaveCodeImage(isChecked);
                break;
            }
        }
    }

    public boolean hasChange() {
        boolean b1 = LocalStorageManager.isAutoCopyAfterScan();
        boolean b2 = LocalStorageManager.isAutoCallToPhoneNumber();
        boolean b3 = LocalStorageManager.isAutoOpenWebBrowser();
        boolean b4 = LocalStorageManager.isEnableSound();
        boolean b5 = LocalStorageManager.isEnableVibrate();
        boolean b6 = LocalStorageManager.isEnableSaveCodeImage();

        return (changes[0] != b1 || changes[1] != b2 || changes[2] != b3 || changes[3] != b4
                || changes[4] != b5 || changes[5] != b6);
    }

    public void setUpForActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Setting");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finishWithHasChanged();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishWithHasChanged();
        super.onBackPressed();
    }

    public void finishWithHasChanged() {
        boolean hasChange = hasChange();
        if (hasChange) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
