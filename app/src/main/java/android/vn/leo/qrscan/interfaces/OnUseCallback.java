package android.vn.leo.qrscan.interfaces;

import android.vn.leo.qrscan.data.ScanResult;

public interface OnUseCallback {

    void useWithNone(ScanResult scanResult);

    void useWithText(ScanResult scanResult);

    void useWithUrl(ScanResult scanResult);

    void useWithSms(ScanResult scanResult);

    void useWithCall(ScanResult scanResult);

    void useWithEmail(ScanResult scanResult);

    void useWithVCard(ScanResult scanResult);
}
