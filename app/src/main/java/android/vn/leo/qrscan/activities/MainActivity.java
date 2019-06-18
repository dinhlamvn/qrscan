package android.vn.leo.qrscan.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.vn.leo.qrscan.BaseActivity;
import android.vn.leo.qrscan.BuildConfig;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.adapters.MainScreenAdapter;
import android.vn.leo.qrscan.data.qrcode.ContactInfo;
import android.vn.leo.qrscan.data.QRCodeType;
import android.vn.leo.qrscan.data.ResultManager;
import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.data.qrcode.QRCodeEmail;
import android.vn.leo.qrscan.database.SQLiteHelper;
import android.vn.leo.qrscan.fragments.HistoryFragment;
import android.vn.leo.qrscan.fragments.ScanFragment;
import android.vn.leo.qrscan.helper.AlertHelper;
import android.vn.leo.qrscan.helper.parser.ContactParser;
import android.vn.leo.qrscan.helper.IntentProviderHelper;
import android.vn.leo.qrscan.helper.parser.QRCodeEmailParser;
import android.vn.leo.qrscan.interfaces.IDialogCallback;
import android.vn.leo.qrscan.interfaces.IHandleResult;
import android.vn.leo.qrscan.interfaces.IQRCodeParser;
import android.vn.leo.qrscan.interfaces.OnAppMenuItemSelected;
import android.vn.leo.qrscan.interfaces.OnResultCallback;
import android.vn.leo.qrscan.interfaces.OnUseCallback;
import android.vn.leo.qrscan.utils.FormatUtility;
import android.vn.leo.qrscan.utils.LocalStorageManager;
import android.vn.leo.qrscan.utils.CommonMethod;
import android.vn.leo.qrscan.utils.QRCodeCommon;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.EmailAddressResultParser;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements OnResultCallback
        , OnUseCallback, IHandleResult, OnAppMenuItemSelected {

    public static final int REQUEST_SETTING_SCREEN = 1100;

    private ViewPager mViewPager;
    private MainScreenAdapter pageAdapter;
    private ScanResult scanResult;
    private boolean isHandlingResult = false;
    private IHandleResult handleResult;

    private DrawerLayout mDrawerLayout;
    private OnAppMenuItemSelected menuListener;
    private AlertDialog useCodeAlert = null;
    private boolean isDisableHandleResult = false;

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

        handleResult = this;

        menuListener = this;

        loadFromDatabase();

        setUpViewPager();

        setUpForDrawerLayout();
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
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCallPhoneNumber();
            } else {
                finishHandleResult();
            }
        } else if (requestCode == REQUEST_READ_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveCaptureImageCodeToExternalStorage();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_SETTING_SCREEN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.toast_message_accept_setting, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CALL_PHONE || requestCode == REQUEST_SEND_MAIL
                || requestCode == REQUEST_SEND_SMS || requestCode == REQUEST_WEB_BROWSER
                || requestCode == REQUEST_ADD_CONTACT) {
            finishHandleResult();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFromDatabase() {
        ResultManager.getInstance().release();
        ResultManager.getInstance().addAll(SQLiteHelper.getInstance().read());
    }

    public void setUpViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        pageAdapter = new MainScreenAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOffscreenPageLimit(1);

        final List<String> titles = Arrays.asList(
                getResources().getString(R.string.screen_scan), getResources().getString(R.string.screen_history));
        pageAdapter.withListTitle(titles);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                openOrCloseAppMenu();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
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

    public boolean closeAppMenu() {
        if (mDrawerLayout == null) {
            return false;
        }
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START, true);
            return true;
        }
        return false;
    }

    public boolean openAppMenu() {
        if (mDrawerLayout == null) {
            return false;
        }
        if (!mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.openDrawer(Gravity.START, true);
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
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        sharedIntent.setType("text/plain");
        sharedIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        String shareMessage = getResources().getString(R.string.share_message) + "\n"
                + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
        sharedIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(sharedIntent, getResources().getString(R.string.chose_app_share)));
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
    public boolean isDisableHandel() {
        return isDisableHandleResult;
    }

    @Override
    public boolean isHandlingResult() {
        return isHandlingResult;
    }

    @Override
    public void finishHandleResult() {
        this.scanResult = null;
        isHandlingResult = false;
        this.useCodeAlert = null;
    }

    @Override
    public void onResult(BarcodeResult result) {
        this.isHandlingResult = true;
        this.scanResult = new ScanResult(result.getText(), result.getBitmap());
        if (result.getBarcodeFormat() == BarcodeFormat.QR_CODE) {
            this.scanResult.setType(QRCodeType.getType(result.getText()));
        } else {
            this.scanResult.setType(QRCodeType.BARCODE);
        }
        if (scanResult.getResult() == null || scanResult.getImage() == null) {
            finishHandleResult();
            return;
        }

        // Show alert dialog with result
        handleResult.showResult(scanResult);

        // Copy result to clipboard
        handleResult.copyResult(scanResult);

        // Update to history screen
        handleResult.updateResultToHistory(scanResult);

        // Update to database
        handleResult.saveResultToDatabase(scanResult);
    }

    @Override
    public void showResult(final ScanResult scanResult) {
        String title = FormatUtility.convertTypeToString(scanResult.getType());
        String btnUse = FormatUtility.getUseStringFromType(this, scanResult.getType());
        String btnCancel = getString(R.string.dialog_result_confirm_3);

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
                QRCodeCommon.getTypeQRCodeToUse(scanResult, MainActivity.this);
            }
        });

        builder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishHandleResult();
            }
        });

        if (scanResult.getType() != QRCodeType.TEXT && scanResult.getType() != QRCodeType.BARCODE) {
            String btnCopy = getString(R.string.dialog_result_confirm_2);
            builder.setNeutralButton(btnCopy, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommonMethod.copyResultToClipboard(MainActivity.this, scanResult.getResult());
                }
            });
        }

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finishHandleResult();
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
                saveCaptureImageCodeToExternalStorage();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_WRITE_EXTERNAL_STORAGE);
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
            isDisableHandleResult = false;
            scanFragment.onResume();
            return;
        }
        isDisableHandleResult = true;
        scanFragment.onPause();
    }

    @Override
    public void useWithNone(final ScanResult scanResult) {
        Toast.makeText(this, R.string.toast_result_empty, Toast.LENGTH_SHORT).show();
        finishHandleResult();
    }

    @Override
    public void useWithText(final ScanResult scanResult) {
        CommonMethod.copyResultToClipboard(this, scanResult.getResult());
        finishHandleResult();
    }

    @Override
    public void useWithUrl(final ScanResult scanResult) {
        this.scanResult = scanResult;
        boolean isAutoOpenWeb = LocalStorageManager.isAutoOpenWebBrowser();

        if (isAutoOpenWeb) {
            startWebBrowser();
            return;
        }
        showNoticeOpenWebBrowser();
    }

    @Override
    public void useWithSms(final ScanResult scanResult) {
        String ret = scanResult.getResult();
        String text = ret.replace(QRCodeType.SMS_PREFIX, "");
        String[] data = text.split(":");
        String phone = "";
        String message = "";

        if (data.length == 1) {
            phone = data[0];
        } else if (data.length == 2) {
            phone = data[0];
            message = data[1];
        }

        Uri uri = Uri.parse("sms:" + phone);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.putExtra("sms_body", message);
        startActivityWithRequestCode(intent, REQUEST_SEND_SMS);
    }

    @Override
    public void useWithCall(final ScanResult scanResult) {
        this.scanResult = scanResult;
        boolean isAutoCall = LocalStorageManager.isAutoCallToPhoneNumber();

        if (isAutoCall) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                startCallPhoneNumber();
            }
            return;
        }

        showNoticeCallToPhoneNumber();
    }

    @Override
    public void useWithEmail(final ScanResult scanResult) {
        IQRCodeParser parser = new QRCodeEmailParser();

        QRCodeEmail email = (QRCodeEmail) parser.parse(scanResult.getResult());

        Intent intent = new Intent();

        // Set action send mail to intent
        intent.setAction(Intent.ACTION_SENDTO);

        // Set type of content
        intent.setType("text/plain");

        // Set data uri for intent
        intent.setData(Uri.parse("mailto:" + email.getReceiver()));

        // Put subject to email
        intent.putExtra(Intent.EXTRA_SUBJECT, email.getSubject());

        // Put the message to email
        intent.putExtra(Intent.EXTRA_TEXT, email.getContent());

        // Start a chooser intent for activity send mail
        startActivityWithRequestCode(Intent.createChooser(intent, getResources().getString(R.string.chose_app_send_mail)), REQUEST_SEND_MAIL);
    }

    @Override
    public void useWithVCard(final ScanResult scanResult) {
        IQRCodeParser parser = new ContactParser();
        ContactInfo contactInfo = (ContactInfo) parser.parse(scanResult.getResult());

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        // Data list for contact
        ArrayList<ContentValues> data = new ArrayList<>();

        // Address
        ContentValues address = new ContentValues();
        address.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
        address.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, contactInfo.getAddress().getStreet());
        address.put(ContactsContract.CommonDataKinds.StructuredPostal.CITY, contactInfo.getAddress().getCity());
        address.put(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, contactInfo.getAddress().getPostcode());
        address.put(ContactsContract.CommonDataKinds.StructuredPostal.REGION, contactInfo.getAddress().getState());
        address.put(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, contactInfo.getAddress().getCountry());
        address.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK);
        data.add(address);

        // Address
        ContentValues website = new ContentValues();
        website.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
        website.put(ContactsContract.CommonDataKinds.Website.URL, contactInfo.getWebsite());
        data.add(website);

        // Put data to intent
        intent.putExtra(ContactsContract.Intents.Insert.NAME,
                contactInfo.getFullName());
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, contactInfo.getMobileNumber());
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, contactInfo.getMailAddress());
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, contactInfo.getCompany());
        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, contactInfo.getJob());
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, contactInfo.getPhoneNumber());
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, contactInfo.getFaxNumber());
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data);

        // Set type for data
        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);

        startActivityWithRequestCode(intent, REQUEST_ADD_CONTACT);
    }

    public void saveCaptureImageCodeToExternalStorage() {
        String fileName = "code_" + scanResult.getDate().getTime() + ".png";
        try (FileOutputStream out = openFileOutput(fileName, MODE_PRIVATE)) {
            scanResult.getImage().compress(Bitmap.CompressFormat.PNG, 80, out);
        } catch (IOException ex) {
            Toast.makeText(this, getResources().getString(R.string.can_not_save_image), Toast.LENGTH_SHORT).show();
        }
    }

    private void startWebBrowser() {
        Intent intent = IntentProviderHelper.provideIntent(Intent.ACTION_VIEW, Uri.parse(scanResult.getResult()));
        startActivityWithRequestCode(intent, REQUEST_WEB_BROWSER);
    }

    private void startCallPhoneNumber() {
        Intent intent = IntentProviderHelper.provideIntent(Intent.ACTION_CALL, Uri.parse(scanResult.getResult()));
        startActivityWithRequestCode(intent, REQUEST_CALL_PHONE);
    }

    private void showNoticeOpenWebBrowser() {

        String webBrowser = "<b>" + getResources().getString(R.string.dialog_open_web_web_browser) + "</b>";
        String result = "<b><u>" + scanResult.getResult() + "</u></b>";

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
                        startWebBrowser();
                    }

                    @Override
                    public void onAction2() {
                        finishHandleResult();
                    }

                    @Override
                    public void onAction3() {

                    }

                    @Override
                    public void onActionCancel() {
                        finishHandleResult();
                    }
                }, true, view , btnOpen, btnCancel);
    }

    private void showNoticeCallToPhoneNumber() {
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
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                        } else {
                            startCallPhoneNumber();
                        }
                    }

                    @Override
                    public void onAction2() {
                        finishHandleResult();
                    }

                    @Override
                    public void onAction3() {

                    }

                    @Override
                    public void onActionCancel() {
                        finishHandleResult();
                    }
                }, true, null , btnCall, btnCancel);
    }
}