package android.vn.leo.qrscan.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.vn.leo.qrscan.BaseActivity;
import android.vn.leo.qrscan.BuildConfig;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.adapters.MainScreenAdapter;
import android.vn.leo.qrscan.data.ResultManager;
import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.database.SQLiteHelper;
import android.vn.leo.qrscan.extensions.ExtensionsKt;
import android.vn.leo.qrscan.fragments.HistoryFragment;
import android.vn.leo.qrscan.fragments.ScanFragment;
import android.vn.leo.qrscan.helper.AlertHelper;
import android.vn.leo.qrscan.helper.IntentProviderHelper;
import android.vn.leo.qrscan.helper.parser.ContentParser;
import android.vn.leo.qrscan.interfaces.IDialogCallback;
import android.vn.leo.qrscan.interfaces.OnExecuteResult;
import android.vn.leo.qrscan.interfaces.OnAppMenuItemSelected;
import android.vn.leo.qrscan.interfaces.OnResult;
import android.vn.leo.qrscan.interfaces.ResultWorker;
import android.vn.leo.qrscan.utils.Const;
import android.vn.leo.qrscan.utils.FormatUtility;
import android.vn.leo.qrscan.utils.LocalStorageManager;
import android.vn.leo.qrscan.utils.CommonMethod;
import android.vn.leo.qrscan.utils.StringUtility;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.SMSParsedResult;
import com.google.zxing.client.result.TelParsedResult;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.google.zxing.client.result.WifiParsedResult;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends BaseActivity implements OnResult
        , ResultWorker, OnExecuteResult, OnAppMenuItemSelected {

    public static final int REQUEST_SETTING_SCREEN = 1100;

    private ViewPager mViewPager;
    private MainScreenAdapter pageAdapter;
    private ScanResult scanResult;
    private boolean isHandlingResult = false;
    private OnExecuteResult handleResult;

    private DrawerLayout mDrawerLayout;
    private OnAppMenuItemSelected menuListener;
    private AlertDialog useCodeAlert = null;
    private boolean isDisableHandleResult = false;
    private ContentParser parser;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = mDrawerLayout.findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return onDrawerMenuItemSelected(menuItem);
            }
        });

        parser = new ContentParser(this);

        handleResult = this;

        menuListener = this;

        loadFromDatabase();

        setUpViewPager();

        setUpForDrawerLayout();

//        // Init google ads
//        MobileAds.initialize(this);
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        });
//
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.google_admob_interstitial));
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }


    public boolean onDrawerMenuItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_btn: {
                menuListener.onHomeSelected();
                break;
            }
            case R.id.setting_btn: {
                menuListener.onSettingSelected();
                break;
            }
            case R.id.rate_btn: {
                menuListener.onRatingSelected();
                break;
            }
            case R.id.share_btn: {
                menuListener.onSharingSelected();
                break;
            }
            case R.id.about_btn: {
                menuListener.onAboutSelected();
                break;
            }
            case R.id.exit_btn: {
                menuListener.onExitSelected();
                break;
            }
        }

        return true;
    }

    @Override
    protected void onResume() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            showToast(getResources().getString(R.string.permission_camera_allow));
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            showToast(getResources().getString(R.string.permission_camera_denied));
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (useCodeAlert != null) {
            useCodeAlert.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_SETTING_SCREEN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.toast_message_accept_setting, Toast.LENGTH_SHORT).show();
            }
            //showAdsAfterSetting();
            release();
        } else if (isRequestCodeUse(requestCode)){
            showAdsAfterUse();
            release();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showAdsAfterScan() {
        if (mInterstitialAd.isLoaded()) {
            Random random = new Random();
            int ratio = random.nextInt(10) + 1;
            if (ratio <= 3) {
                mInterstitialAd.show();
            }
        }
    }

    public void showAdsAfterSetting() {
        if (mInterstitialAd.isLoaded()) {
            Random random = new Random();
            int ratio = random.nextInt(10) + 1;
            if (ratio <= 2) {
                mInterstitialAd.show();
            }
        }
    }

    public void showAdsAfterUse() {
        if (mInterstitialAd.isLoaded()) {
            Random random = new Random();
            int ratio = random.nextInt(10) + 1;
            if (ratio <= 5) {
                mInterstitialAd.show();
            }
        }
    }

    private void loadFromDatabase() {
        List<String> removedList = CommonMethod.fetchDeleteList();

        boolean isRemoveAll = false;

        if (removedList != null && removedList.size() > 0) {
            for (String s : removedList) {
                isRemoveAll = SQLiteHelper.getInstance().remove(s);
            }
        }

        if (isRemoveAll) {
            CommonMethod.clearDeleteList();
        }

        ResultManager.getInstance().release();
        ResultManager.getInstance().addAll(SQLiteHelper.getInstance().read());
    }

    public void setUpViewPager() {
        mViewPager = findViewById(R.id.view_pager);
        pageAdapter = new MainScreenAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOffscreenPageLimit(1);

        final List<String> titles = Arrays.asList(
                getResources().getString(R.string.screen_scan), getResources().getString(R.string.screen_history));
        pageAdapter.withListTitle(titles);
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        setUpTabIcons(tabLayout);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                onPageChange(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setUpTabIcons(final TabLayout tabLayout) {
        int[] tabIcons = {R.drawable.ic_scan, R.drawable.ic_history};
        TabLayout.Tab tabScan = tabLayout.getTabAt(0);
        TabLayout.Tab tabHistory = tabLayout.getTabAt(1);

        if (tabScan != null) {
            tabScan.setIcon(tabIcons[0]);
        }

        if (tabHistory != null) {
            tabHistory.setIcon(tabIcons[1]);
        }
    }

    public void setUpForDrawerLayout() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_menu));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                openOrCloseAppMenu();
                return true;
            }
        }
        return false;
    }

    private void openOrCloseAppMenu() {
        if (mDrawerLayout == null) {
            return;
        }
        if (!openAppMenu()) {
            closeAppMenu();
        }
    }

    @Override
    public void onHomeSelected() {
        if (mDrawerLayout == null) {
            return;
        }
        closeAppMenu();
    }

    public void closeAppMenu() {
        if (mDrawerLayout == null) {
            return;
        }
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START, true);
        }
    }

    public boolean openAppMenu() {
        if (mDrawerLayout == null) {
            return false;
        }
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START, true);
            return true;
        }
        return false;
    }

    @Override
    public void onSettingSelected() {
        closeAppMenu();
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivityForResult(intent, REQUEST_SETTING_SCREEN);
    }

    @Override
    public void onRatingSelected() {
        closeAppMenu();
        final String appPackageName = BuildConfig.APPLICATION_ID;
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void onSharingSelected() {
        String shareMessage = getResources().getString(R.string.share_message) + "\n"
                + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
        ExtensionsKt.share(shareMessage, this, "", true);
        closeAppMenu();
    }

    @Override
    public void onAboutSelected() {
        closeAppMenu();
        final String title = getResources().getString(R.string.dialog_about_title);
        final String applicationName = "<b>" + getResources().getString(R.string.app_name) + "</b>";
        final String version = "<b>" + BuildConfig.VERSION_NAME + "</b>";
        final String developer = "<b>" + getResources().getString(R.string.developer) + "</b>";
        final String contact = "<b>" + getResources().getString(R.string.developer_contact) + "</b>";
        final String message = String.format(getResources().getString(R.string.dialog_about_message), applicationName, version, developer, contact);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message.replaceAll("\n", "<br>")));
        builder.setCancelable(true);
        builder.setPositiveButton(getResources().getString(R.string.dialog_about_button), null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onExitSelected() {
        finish();
    }

    @Override
    public boolean isDisabled() {
        return isDisableHandleResult;
    }

    @Override
    public boolean isOnResult() {
        return isHandlingResult;
    }

    @Override
    public void release() {
        this.scanResult = null;
        isHandlingResult = false;
        this.useCodeAlert = null;
        ScanFragment scanFragment = (ScanFragment) pageAdapter.getItem(0);
        isDisableHandleResult = false;
        scanFragment.onResume();
    }

    @Override
    public void onResult(BarcodeResult result) {
        showAdsAfterScan();
        this.isHandlingResult = true;
        ParsedResult parsedResult = ResultParser.parseResult(result.getResult());

        if (parsedResult == null || parsedResult.getDisplayResult().isEmpty()) {
            release();
            return;
        }

        boolean isCaptureImage = LocalStorageManager.isEnableSaveCodeImage();

        if (isCaptureImage) {
            this.scanResult = new ScanResult(result.getText(), result.getBitmap());
        } else {
            this.scanResult = new ScanResult(result.getText(), null);
        }
        this.scanResult.setType(parsedResult.getType());

        // Vibrate device
        handleResult.vibrate();

        // Play the sound
        handleResult.sound();

        // Check the setting of user for auto use result after scanned was on/off
        boolean isAutoUseResult = LocalStorageManager.isAutoUseAfterScan();

        if (isAutoUseResult) {
            // Call the request use that code if user turn on auto use feature
            parser.startParse(scanResult);
        } else {
            // If user turn off auto use feature, please show a result dialog to notify
            // Show alert dialog with result
            handleResult.showResult(scanResult);
        }
        // Copy result to clipboard
        handleResult.copyResult(scanResult);

        // Update to history screen
        handleResult.updateResultToHistory(scanResult);

        // Update to database
        handleResult.saveResultToDatabase(scanResult);
    }

    @Override
    public void vibrate() {
        boolean isVibrateEnabled = LocalStorageManager.isEnableVibrate();

        if (isVibrateEnabled) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            if (CommonMethod.isNotNull(vibrator)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                }
            }
        }
    }

    @Override
    public void sound() {
        boolean isSoundEnabled = LocalStorageManager.isEnableSound();

        if (isSoundEnabled) {
            final MediaPlayer player = MediaPlayer.create(this, R.raw.beep);
            player.start();
        }
    }

    @Override
    public void showResult(final ScanResult scanResult) {
        String title = FormatUtility.getTitleResultFromType(this, scanResult.getType());
        String btnUse = FormatUtility.getUseStringFromType(this, scanResult.getType());
        String btnCopy = getString(R.string.dialog_result_confirm_3);
        String btnShare = getString(R.string.dialog_result_confirm_2);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_image, null);
        ImageView imageView = dialogView.findViewById(R.id.image_view);
        if (scanResult.getImage() != null) {
            Glide.with(this)
                    .load(scanResult.getImage())
                    .transform(new RoundedCorners(32))
                    .skipMemoryCache(true)
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
        TextView textMessage = dialogView.findViewById(R.id.text_message);
        textMessage.setText(scanResult.getResult());

        builder.setView(dialogView);
        builder.setCancelable(true);
        builder.setTitle(title);

        builder.setPositiveButton(btnUse, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                parser.startParse(scanResult);
            }
        });

        builder.setNegativeButton(btnCopy, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonMethod.copyResultToClipboard(MainActivity.this, scanResult.getResult());
                release();
            }
        });

        builder.setNeutralButton(btnShare, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = scanResult.getResult();
                CommonMethod.share(MainActivity.this, result);
                release();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                release();
            }
        });

        useCodeAlert = builder.create();
        useCodeAlert.setCanceledOnTouchOutside(true);
        useCodeAlert.show();
    }

    @Override
    public void copyResult(final ScanResult scanResult) {
        boolean isAutoCopy = LocalStorageManager.isAutoCopyAfterScan();
        if (isAutoCopy) {
            CommonMethod.copyResultToClipboard(MainActivity.this, scanResult.getResult());
        }
    }

    @Override
    public void updateResultToHistory(final ScanResult scanResult) {
        Intent intent = new Intent();
        intent.putExtra("item", scanResult);
        intent.setAction(HistoryFragment.ACTION_UPDATE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void saveResultToDatabase(final ScanResult scanResult) {
        boolean isSaveImage = LocalStorageManager.isEnableSaveCodeImage();
        if (isSaveImage) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                saveCaptureImageCodeToExternalStorage(scanResult);
            } else {
                Dexter.withActivity(this)
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showToast(getResources().getString(R.string.permission_storage_allow));
                                    saveCaptureImageCodeToExternalStorage(scanResult);
                                } else {
                                    showToast(getResources().getString(R.string.permission_storage_denied));
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        }
        scanResult.setId((int) SQLiteHelper.getInstance().insert(scanResult));
    }

    /**
     * Handel on page change when user swipe screen to left or right. It will pause scan when on history
     * screen and resume when on scan screen
     * @param position Current screen
     */
    public void onPageChange(int position) {
        ScanFragment scanFragment = (ScanFragment) pageAdapter.getItem(0);
        if (position == 0) {
            HistoryFragment historyFragment = (HistoryFragment) pageAdapter.getItem(1);
            historyFragment.releaseActionMode();
            isDisableHandleResult = false;
            scanFragment.onResume();
            return;
        }
        isDisableHandleResult = true;
        scanFragment.onPause();
    }

    @Override
    public void copyText(final ParsedResult result) {
        if (result == null) {
            release();
            return;
        }
        CommonMethod.copyResultToClipboard(this, scanResult.getResult());
        release();
    }

    @Override
    public void sendSMS(final ParsedResult result) {
        SMSParsedResult sms = (SMSParsedResult) result;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.fromParts("sms", StringUtility.getStringListIfNotNullFromArray(sms.getNumbers()), null));
        intent.putExtra("sms_body", sms.getBody());
        startActivityWithRequestCode(intent, REQUEST_SEND_SMS);
    }

    @Override
    public void callPhone(final ParsedResult result) {
        final TelParsedResult tell = (TelParsedResult) result;
        boolean isTurnOnConfirmCallPhone = LocalStorageManager.isTurnOnConfirmCallPhone();

        if (!isTurnOnConfirmCallPhone) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                startCallPhoneNumber(tell.getTelURI());
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                showToast(getResources().getString(R.string.permission_call_denied));
                                release();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            } else {
                startCallPhoneNumber(tell.getTelURI());
            }
            return;
        }

        showNoticeCallToPhoneNumber(tell.getTelURI());
    }

    @Override
    public void sendEmail(final ParsedResult result) {
        EmailAddressParsedResult email = (EmailAddressParsedResult) result;

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, email.getTos());
        intent.putExtra(Intent.EXTRA_SUBJECT, email.getSubject());
        intent.putExtra(Intent.EXTRA_TEXT, email.getBody());
        intent.putExtra(Intent.EXTRA_CC, email.getCCs());
        intent.putExtra(Intent.EXTRA_BCC, email.getBCCs());

        startActivityWithRequestCode(intent, REQUEST_SEND_MAIL);
    }

    @Override
    public void addNewContact(ParsedResult result) {
        AddressBookParsedResult contact = (AddressBookParsedResult) result;

        String[] addresses = contact.getAddresses();
        String[] emails = contact.getEmails();
        String[] phoneNumbers = contact.getPhoneNumbers();

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        // Data list for contact
        ArrayList<ContentValues> data = new ArrayList<>();

        // Phone number
        intent.putExtra(ContactsContract.Intents.Insert.PHONE,
                StringUtility.getStringValueIfNotNullFromArray(phoneNumbers, 0));
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE,
                StringUtility.getStringValueIfNotNullFromArray(phoneNumbers, 1));
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE,
                StringUtility.getStringValueIfNotNullFromArray(phoneNumbers, 2));

        // Email address
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL,
                StringUtility.getStringValueIfNotNullFromArray(emails, 0));
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL,
                StringUtility.getStringValueIfNotNullFromArray(emails, 1));
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL,
                StringUtility.getStringValueIfNotNullFromArray(emails, 2));

        // Address
        ContentValues address = new ContentValues();
        address.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
        address.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, StringUtility.getStringValueIfNotNullFromArray(addresses, 0));
        address.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK);
        data.add(address);

        // Website address
        ContentValues website = new ContentValues();
        website.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
        website.put(ContactsContract.CommonDataKinds.Website.URL, StringUtility.getStringListIfNotNullFromArray(contact.getURLs()));
        data.add(website);

        // Contact name
        intent.putExtra(ContactsContract.Intents.Insert.NAME,
                StringUtility.getStringValueIfNotNullFromArray(contact.getNames(), 0));

        // Put data to intent
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, contact.getOrg());
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contact.getTitle());
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data);
        intent.putExtra(ContactsContract.Intents.Insert.NOTES, StringUtility.getStringValueIfNotNull(contact.getNote()));

        // Set type for data
        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);

        startActivityWithRequestCode(intent, REQUEST_ADD_CONTACT);
    }

    @Override
    public void accessUri(final ParsedResult result) {
        URIParsedResult uri = (URIParsedResult) result;
        boolean isTurnOnConfirmMoveWeb = LocalStorageManager.isTurnOnConfirmMoveWeb();

        if (!isTurnOnConfirmMoveWeb) {
            startWebBrowser(uri.getURI());
            return;
        }
        showNoticeOpenWebBrowser(uri.getURI());
    }

    @Override
    public void accessWifi(ParsedResult result) {
        release();
        WifiParsedResult wifi = (WifiParsedResult) result;

        String wifiSSID = wifi.getSsid();
        String wifiPass = wifi.getPassword();
        String wifiType = wifi.getNetworkEncryption();

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", wifiSSID);

        if ("WEP".equals(wifiType)) {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            if (wifiPass.matches("^[0-9a-fA-F]+$")) {
                conf.wepKeys[0] = wifiPass;
            } else {
                conf.wepKeys[0] = String.format("\"%s\"", wifiPass);
            }
        } else if (wifiType.toUpperCase().contains("WPA")) {
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.preSharedKey = String.format("\"%s\"", wifiPass);
        } else {
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedAuthAlgorithms.clear();
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        if (!wifiManager.isWifiEnabled()) {
            showToast(getResources().getString(R.string.enable_wifi_toast));
            wifiManager.setWifiEnabled(true);
        }
        int netId = wifiManager.addNetwork(conf);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
        String toast = String.format(getResources().getString(R.string.wifi_connect_toast),
                wifiSSID);
        showToast(toast);
    }

    @Override
    public void researchProduct(ParsedResult result) {
        TextParsedResult text = (TextParsedResult) result;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Const.PRODUCT_RESEARCH_URL + text.getText()));
        startActivityWithRequestCode(intent, REQUEST_WEB_BROWSER);
    }

    public void saveCaptureImageCodeToExternalStorage(final ScanResult scanResult) {
        String fileName = "code_" + scanResult.getDate().getTime() + ".png";
        try (FileOutputStream out = openFileOutput(fileName, MODE_PRIVATE)) {
            scanResult.getImage().compress(Bitmap.CompressFormat.PNG, 80, out);
        } catch (IOException ex) {
            Toast.makeText(this, getResources().getString(R.string.can_not_save_image), Toast.LENGTH_SHORT).show();
        }
    }

    private void startWebBrowser(String uri) {
        Intent intent = IntentProviderHelper.provideIntent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivityWithRequestCode(intent, REQUEST_WEB_BROWSER);
    }

    private void startCallPhoneNumber(String telUri) {
        Intent intent = IntentProviderHelper.provideIntent(Intent.ACTION_CALL, Uri.parse(telUri));
        startActivityWithRequestCode(intent, REQUEST_CALL_PHONE);
    }

    private void showNoticeOpenWebBrowser(final String uri) {

        String webBrowser = "<b>" + getResources().getString(R.string.dialog_open_web_web_browser) + "</b>";
        String result = "<b><u>" + uri + "</u></b>";

        String title = getString(R.string.dialog_notice_title);
        String message = String.format(getResources().getString(R.string.dialog_open_web_confirm_message), webBrowser, result);
        String btnOpen = getString(R.string.dialog_button_open);
        String btnCancel = getString(R.string.dialog_button_cancel);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_use_code, null);
        TextView textMessage = view.findViewById(R.id.text_message);
        textMessage.setText(Html.fromHtml(message));

        AlertHelper.showConfirmDialog(this,
                title, null, new IDialogCallback() {
                    @Override
                    public void onAction1() {
                        startWebBrowser(uri);
                    }

                    @Override
                    public void onAction2() {
                        release();
                    }

                    @Override
                    public void onAction3() {
                        release();
                    }

                    @Override
                    public void onActionCancel() {
                        release();
                    }
                }, true, view , btnOpen, btnCancel);
    }

    private void showNoticeCallToPhoneNumber(final String telUri) {
        String ret = scanResult.getResult();
        String phoneNumber = ret.substring(ret.indexOf(":") + 1);

        String call = "<b>" + getResources().getString(R.string.dialog_call_call) + "</b>";
        String result = "<b><u>" + phoneNumber + "</u></b>";

        String title = getString(R.string.dialog_notice_title);
        String message = String.format(getResources().getString(R.string.dialog_call_confirm_message), call, result);
        String btnCall = getString(R.string.dialog_button_call);
        String btnCancel = getString(R.string.dialog_button_cancel);

        AlertHelper.showConfirmDialog(this,
                title, message, new IDialogCallback() {
                    @Override
                    public void onAction1() {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            Dexter.withActivity(MainActivity.this)
                                    .withPermission(Manifest.permission.CALL_PHONE)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {
                                            startCallPhoneNumber(telUri);
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {
                                            showToast(getResources().getString(R.string.permission_call_denied));
                                            release();
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                            token.continuePermissionRequest();
                                        }
                                    }).check();
                        } else {
                            startCallPhoneNumber(telUri);
                        }
                    }

                    @Override
                    public void onAction2() {
                        release();
                    }

                    @Override
                    public void onAction3() {
                        release();
                    }

                    @Override
                    public void onActionCancel() {
                        release();
                    }
                }, true, null , btnCall, btnCancel);
    }
}