package android.vn.leo.qrscan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.vn.leo.qrscan.adapters.MainScreenAdapter;
import android.vn.leo.qrscan.data.QRCodeType;
import android.vn.leo.qrscan.fragments.HistoryFragment;
import android.vn.leo.qrscan.fragments.ScanFragment;
import android.vn.leo.qrscan.interfaces.IHandleResult;
import android.vn.leo.qrscan.interfaces.OnClickHistoryItemCallback;
import android.vn.leo.qrscan.interfaces.OnResultCallback;
import android.vn.leo.qrscan.interfaces.OnUseCallback;
import android.vn.leo.qrscan.utils.LocalStorageManager;
import android.vn.leo.qrscan.utils.CommonMethod;
import android.vn.leo.qrscan.utils.QRCodeCommon;
import android.widget.ImageView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnResultCallback, OnClickHistoryItemCallback
        , OnUseCallback, IHandleResult {


    public static final int REQUEST_CAMERA_PERMISSION = 1000;
    public static final int REQUEST_CALL_PERMISSION = 1001;

    private ViewPager viewPager;
    private MainScreenAdapter pageAdapter;
    private String resultText;
    private Bitmap resultBitmap;
    private boolean isHandlingResult = false;
    private IHandleResult handleResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleResult = this;

        setUpViewPager();
    }

    @Override
    protected void onResume() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
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
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCallPhoneNumber();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setUpViewPager() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pageAdapter = new MainScreenAdapter(getSupportFragmentManager());
        pageAdapter.attachResultCallback(this);
        pageAdapter.attachOnClickItemCallback(this);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(1);

        final List<String> titles = Arrays.asList("Scan", "History");
        pageAdapter.withListTitle(titles);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    public boolean isHandlingResult() {
        return isHandlingResult;
    }

    @Override
    public void finishHandleResult() {
        this.resultText = null;
        this.resultBitmap = null;
        isHandlingResult = false;
    }

    @Override
    public void onResult(BarcodeResult result) {
        this.isHandlingResult = true;
        this.resultText = result.getText();
        this.resultBitmap = result.getBitmap();

        if (this.resultText == null || this.resultBitmap == null) {
            finishHandleResult();
            return;
        }

        // Show alert dialog with result
        handleResult.showResult(resultText, resultBitmap);

        // Copy result to clipboard
        handleResult.copyResult(resultText);

        // Update to history screen
        handleResult.updateResultToHistory(resultText, resultBitmap);

        // Update to database
        handleResult.saveResultToDatabase(resultText, resultBitmap);
    }

    @Override
    public void onClickHistoryItemCallback(String result, Bitmap bitmap) {
        // Show alert with result and image of QRCode or Barcode
        handleResult.showResult(result, bitmap);
    }

    @Override
    public void showResult(final String result, final Bitmap bitmap) {
        String title = getString(R.string.dialog_result_title);
        String btnUse = getString(R.string.dialog_result_confirm_1);
        String btnCopy = getString(R.string.dialog_result_confirm_2);
        String btnCancel = getString(R.string.dialog_result_confirm_3);

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(result);
        builder.setView(imageView);
        builder.setPositiveButton(btnUse, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                QRCodeCommon.getTypeQRCodeToUse(result, MainActivity.this);
            }
        });
        builder.setNeutralButton(btnCopy, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonMethod.copyResultToClipboard(MainActivity.this, result);
            }
        });
        builder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishHandleResult();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finishHandleResult();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    @Override
    public void copyResult(final String result) {
        boolean isAutoCopy = LocalStorageManager.isAutoCopyAfterScan();
        if (isAutoCopy) {
            CommonMethod.copyResultToClipboard(MainActivity.this, result);
        }
    }

    @Override
    public void updateResultToHistory(final String result, final Bitmap bitmap) {
        Intent intent = new Intent();
        intent.putExtra("data", result);
        intent.putExtra("image", bitmap);
        intent.setAction(HistoryFragment.ACTION_UPDATE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void saveResultToDatabase(final String result, final Bitmap bitmap) {

    }

    /**
     * Handel on page change when user swipe screen to left or right. It will pause scan when on history
     * screen and resume when on scan screen
     * @param position Current screen
     */
    public void onPageChange(int position) {
        ScanFragment scanFragment = (ScanFragment) pageAdapter.getItem(0);
        if (position == 1) {
            scanFragment.onPause();
            return;
        }
        scanFragment.onResume();
    }

    @Override
    public void useWithNone() {
        Toast.makeText(this, R.string.toast_result_empty, Toast.LENGTH_SHORT).show();
        finishHandleResult();
    }

    @Override
    public void useWithText() {
        CommonMethod.copyResultToClipboard(this, resultText);
        finishHandleResult();
    }

    @Override
    public void useWithUrl() {
        boolean isAutoOpenWeb = LocalStorageManager.isAutoOpenWebBrowser();

        if (isAutoOpenWeb) {
            startWebBrowser();
            return;
        }
        showNoticeOpenWebBrowser();
    }

    @Override
    public void useWithSms() {
        String text = this.resultText.replace(QRCodeType.SMS_PREFIX, "");
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
        startActivity(intent);
    }

    @Override
    public void useWithCall() {
        boolean isAutoCall = LocalStorageManager.isAutoCallToPhoneNumber();

        if (isAutoCall) {
            startCallPhoneNumber();
            return;
        }

        showNoticeCallToPhoneNumber();
    }

    @Override
    public void useWithEmail() {
        Intent intent = new Intent();
        // Subject of email
        String subject = "";
        // Receiver of email
        String receiver = "";
        // Message of email
        String message = "";

        String[] args = this.resultText.substring(7, this.resultText.length() - 1).split(";");

        if (args.length == 0) {
            receiver = this.resultText.substring(7);
        } else {
            for (String s : args) {
                if (QRCodeCommon.getContentSendMail(s) == QRCodeCommon.EmailContentType.SEND_TO) {
                    receiver = s.substring(3);
                } else if (QRCodeCommon.getContentSendMail(s) == QRCodeCommon.EmailContentType.SUBJECT) {
                    subject = s.substring(4);
                } else if (QRCodeCommon.getContentSendMail(s) == QRCodeCommon.EmailContentType.BODY) {
                    message = s.substring(5);
                }
            }
        }

        // Set action send mail to intent
        intent.setAction(Intent.ACTION_SENDTO);

        // Set type of content
        intent.setType("text/plain");

        // Set data uri for intent
        intent.setData(Uri.parse("mailto:" + receiver));

        // Put subject to email
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        // Put the message to email
        intent.putExtra(Intent.EXTRA_TEXT, message);

        // Start a chooser intent for activity send mail
        startActivity(Intent.createChooser(intent, "CHOSE APP TO SEND MAIL"));
    }

    @Override
    public void useWithVCard() {
        CommonMethod.copyResultToClipboard(this, resultText);
        finishHandleResult();
    }

    private void startWebBrowser() {
        Uri uri = Uri.parse(this.resultText);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
        finishHandleResult();
    }

    private void startCallPhoneNumber() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse(resultText));
        startActivity(intent);
        finishHandleResult();
    }

    private void showNoticeOpenWebBrowser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String title = getString(R.string.dialog_notice_title);
        String message = "Do you want to open <b>Web Browser</b> with the url " +
                "<b>" + this.resultText + "</b>";
        String btnOpen = getString(R.string.dialog_button_open);
        String btnCancel = getString(R.string.dialog_button_cancel);

        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(btnOpen, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startWebBrowser();
            }
        });
        builder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishHandleResult();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finishHandleResult();
            }
        });
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void showNoticeCallToPhoneNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String phoneNumber = this.resultText.substring(this.resultText.indexOf(":") + 1);

        String title = getString(R.string.dialog_notice_title);
        String message = "Do you want to <b>call to number " + phoneNumber + "</b>";
        String btnCall = getString(R.string.dialog_button_call);
        String btnCancel = getString(R.string.dialog_button_cancel);

        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton(btnCall, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                } else {
                    startCallPhoneNumber();
                }
            }
        });
        builder.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishHandleResult();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finishHandleResult();
            }
        });
        builder.setCancelable(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
}
