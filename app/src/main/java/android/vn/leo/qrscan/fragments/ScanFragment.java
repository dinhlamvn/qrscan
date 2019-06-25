package android.vn.leo.qrscan.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.interfaces.OnResult;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class ScanFragment extends Fragment implements DecoratedBarcodeView.TorchListener,
        BarcodeCallback, View.OnKeyListener {

    private DecoratedBarcodeView mDecoratedBarcodeView;
    private boolean isFlashOn = false;

    private OnResult resultCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        mDecoratedBarcodeView = view.findViewById(R.id.zxing_barcode_scanner);

        mDecoratedBarcodeView.decodeContinuous(this);
        mDecoratedBarcodeView.setTorchListener(this);
        mDecoratedBarcodeView.setOnKeyListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.resultCallback = (OnResult) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_flash: {
                onFlash();
                if (isFlashOn) {
                    item.setIcon(R.drawable.ic_flash_on_24dp);
                } else {
                    item.setIcon(R.drawable.ic_flash_off_24dp);
                }
                return true;
            }
        }
        return false;
    }

    public void onFlash() {
        isFlashOn = !isFlashOn;
        if (isFlashOn) {
            mDecoratedBarcodeView.setTorchOn();
        } else {
            mDecoratedBarcodeView.setTorchOff();
        }
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        if (this.resultCallback == null) {
            return;
        }
        if (this.resultCallback.isOnResult()) {
            return;
        }
        this.resultCallback.onResult(result);
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {

    }

    @Override
    public void onResume() {
        if (mDecoratedBarcodeView != null &&
                this.resultCallback != null && !this.resultCallback.isDisabled()) {
            mDecoratedBarcodeView.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDecoratedBarcodeView != null) {
            mDecoratedBarcodeView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDecoratedBarcodeView != null) {
            mDecoratedBarcodeView.decodeContinuous(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK;
    }

    @Override
    public void onTorchOn() {
        Toast.makeText(getContext(), getResources().getString(R.string.light_flash_on), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTorchOff() {
        Toast.makeText(getContext(), getResources().getString(R.string.light_flash_off), Toast.LENGTH_SHORT).show();
    }
}
