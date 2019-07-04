package android.vn.leo.qrscan.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.vn.leo.qrscan.BaseActivity;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.utils.LocalStorageManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String AUTO_COPY = "auto_copy";
    public static final String AUTO_USE = "auto_use";
    public static final String AUTO_MAKE_CALL = "auto_make_call";
    public static final String AUTO_ACCESS_URL = "auto_access_url";
    public static final String ENABLE_SOUND = "enable_sound";
    public static final String ENABLE_VIBRATE = "enable_vibrate";
    public static final String ENABLE_SAVE_CODE_IMAGE = "enable_save_code_image";

    private boolean[] changes = new boolean[7];

    private CheckBox ckbEnableSaveCodeImage;

    private boolean isNewOpen = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpForActionBar();

        boolean isAutoCopy = LocalStorageManager.isAutoCopyAfterScan();
        changes[0] = isAutoCopy;
        final CheckBox ckbAutoCopy = findViewById(R.id.checkbox_auto_copy_result);
        ckbAutoCopy.setTag(AUTO_COPY);
        ckbAutoCopy.setOnCheckedChangeListener(this);
        ckbAutoCopy.setChecked(isAutoCopy);

        boolean isAutoUse = LocalStorageManager.isAutoUseAfterScan();
        changes[1] = isAutoUse;
        final CheckBox ckbAutoUse = findViewById(R.id.checkbox_auto_use_result);
        ckbAutoUse.setTag(AUTO_USE);
        ckbAutoUse.setOnCheckedChangeListener(this);
        ckbAutoUse.setChecked(isAutoUse);

        boolean isAutoMakeCall = LocalStorageManager.isAutoCallToPhoneNumber();
        changes[2] = isAutoMakeCall;
        final CheckBox ckbAutoMakeCall = findViewById(R.id.checkbox_auto_make_call);
        ckbAutoMakeCall.setTag(AUTO_MAKE_CALL);
        ckbAutoMakeCall.setOnCheckedChangeListener(this);
        ckbAutoMakeCall.setChecked(isAutoMakeCall);

        boolean isAutoAccessUrl = LocalStorageManager.isAutoOpenWebBrowser();
        changes[3] = isAutoAccessUrl;
        final CheckBox ckbAutoUrl = findViewById(R.id.checkbox_auto_access_url);
        ckbAutoUrl.setTag(AUTO_ACCESS_URL);
        ckbAutoUrl.setOnCheckedChangeListener(this);
        ckbAutoUrl.setChecked(isAutoAccessUrl);

        boolean isEnableSound = LocalStorageManager.isEnableSound();
        changes[4] = isEnableSound;
        final CheckBox ckbEnableSound = findViewById(R.id.checkbox_sound);
        ckbEnableSound.setTag(ENABLE_SOUND);
        ckbEnableSound.setOnCheckedChangeListener(this);
        ckbEnableSound.setChecked(isEnableSound);

        boolean isEnableVibrate = LocalStorageManager.isEnableVibrate();
        changes[5] = isEnableVibrate;
        final CheckBox ckbEnableVibrate = findViewById(R.id.checkbox_vibrate);
        ckbEnableVibrate.setTag(ENABLE_VIBRATE);
        ckbEnableVibrate.setOnCheckedChangeListener(this);
        ckbEnableVibrate.setChecked(isEnableVibrate);

        boolean isEnableSaveCodeImage = LocalStorageManager.isEnableSaveCodeImage();
        changes[6] = isEnableSaveCodeImage;
        ckbEnableSaveCodeImage = findViewById(R.id.checkbox_save_image_code);
        ckbEnableSaveCodeImage.setTag(ENABLE_SAVE_CODE_IMAGE);
        ckbEnableSaveCodeImage.setOnCheckedChangeListener(this);
        ckbEnableSaveCodeImage.setChecked(isEnableSaveCodeImage);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setting;
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
            case AUTO_USE: {
                saveSettingEnableAutoUse(isChecked);
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
                saveSettingEnableSaveCodeImage(isChecked);
                break;
            }
        }
    }

    private void saveSettingEnableAutoUse(boolean isChecked) {
        LocalStorageManager.setAutoUseAfterScan(isChecked);
        if (isChecked && !isNewOpen) {
            showToast(getResources().getString(R.string.toast_message_auto_use));
        }
    }

    private void saveSettingEnableSaveCodeImage(boolean isChecked) {
        LocalStorageManager.setEnableSaveCodeImage(isChecked);
        if (isChecked && (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showToast(getResources().getString(R.string.permission_storage_allow));
                            } else {
                                showToast(getResources().getString(R.string.permission_storage_denied));
                                LocalStorageManager.setEnableSaveCodeImage(false);
                                ckbEnableSaveCodeImage.setChecked(false);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .check();
        }
    }

    public boolean hasChange() {
        boolean b1 = LocalStorageManager.isAutoCopyAfterScan();
        boolean b2 = LocalStorageManager.isAutoUseAfterScan();
        boolean b3 = LocalStorageManager.isAutoCallToPhoneNumber();
        boolean b4 = LocalStorageManager.isAutoOpenWebBrowser();
        boolean b5 = LocalStorageManager.isEnableSound();
        boolean b6 = LocalStorageManager.isEnableVibrate();
        boolean b7 = LocalStorageManager.isEnableSaveCodeImage();

        return (changes[0] != b1 || changes[1] != b2 || changes[2] != b3 || changes[3] != b4
                || changes[4] != b5 || changes[5] != b6 || changes[6] != b7);
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
        isNewOpen = false;
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
