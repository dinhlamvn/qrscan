package android.vn.leo.qrscan.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.interfaces.OnResultCallback;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class ScanFragment extends Fragment implements DecoratedBarcodeView.TorchListener,
        BarcodeCallback, View.OnClickListener, View.OnKeyListener {

    private DecoratedBarcodeView mDecoratedBarcodeView;
    private boolean isFlashOn = false;
    private FloatingActionButton btnFlash;

    private OnResultCallback resultCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        mDecoratedBarcodeView = (DecoratedBarcodeView) view.findViewById(R.id.zxing_barcode_scanner);

        mDecoratedBarcodeView.decodeContinuous(this);
        mDecoratedBarcodeView.setTorchListener(this);
        mDecoratedBarcodeView.setOnKeyListener(this);

        btnFlash = (FloatingActionButton) view.findViewById(R.id.flash_btn);
        btnFlash.setOnClickListener(this);

        return view;
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        if (this.resultCallback == null) {
            return;
        }
        if (this.resultCallback.isHandlingResult()) {
            return;
        }
        this.resultCallback.onResult(result);
    }

    public void withResultCallback(@NonNull OnResultCallback resultCallback) {
        this.resultCallback = resultCallback;
    }

    @Override
    public void onClick(View v) {
        isFlashOn = !isFlashOn;
        if (isFlashOn) {
            mDecoratedBarcodeView.setTorchOn();
        } else {
            mDecoratedBarcodeView.setTorchOff();
        }
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {

    }

    @Override
    public void onResume() {
        if (mDecoratedBarcodeView != null) {
            mDecoratedBarcodeView.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mDecoratedBarcodeView != null) {
            mDecoratedBarcodeView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mDecoratedBarcodeView != null) {
            mDecoratedBarcodeView.decodeContinuous(null);
        }
        super.onDestroy();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK;
    }

    @Override
    public void onTorchOn() {
        btnFlash.setBackgroundTintList(getResources().getColorStateList(R.color.flash_btn_active));
    }

    @Override
    public void onTorchOff() {
        btnFlash.setBackgroundTintList(getResources().getColorStateList(R.color.flash_btn_not_active));
    }
}
