package android.vn.leo.qrscan.interfaces;

import android.vn.leo.qrscan.data.QRCodeType;

public interface OnUseCallback {

    void useWithNone();

    void useWithText();

    void useWithUrl();

    void useWithSms();

    void useWithCall();

    void useWithEmail();

    void useWithVCard();
}
