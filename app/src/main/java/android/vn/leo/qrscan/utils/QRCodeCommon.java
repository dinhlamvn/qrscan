package android.vn.leo.qrscan.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.vn.leo.qrscan.data.QRCodeType;
import android.vn.leo.qrscan.interfaces.OnUseCallback;

public class QRCodeCommon {

    public enum EmailContentType {
        NONE(0),
        SEND_TO(1),
        SUBJECT(2),
        BODY(3);

        private int value;

        EmailContentType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * Get type of QRCode and call back to do something
     * @param result The result text
     * @param callback The callback after get type
     */
    public static void  getTypeQRCodeToUse(String result, OnUseCallback callback) {
        QRCodeType type = getType(result);

        switch (type) {
            case TEXT: {
                callback.useWithText();
                break;
            }
            case URL: {
                callback.useWithUrl();
                break;
            }
            case SMS: {
                callback.useWithSms();
                break;
            }
            case CALL: {
                callback.useWithCall();
                break;
            }
            case EMAIL: {
                callback.useWithEmail();
                break;
            }
            case VCARD: {
                callback.useWithVCard();
                break;
            }
        }

        callback.useWithNone();
    }

    /**
     * Get type of QRCode from result text
     * @param result The result text
     * @return The result is <b>NONE, TEXT, URL, SMS, CALL, EMAIL, VCARD</b>, see {@link QRCodeType}
     * to more info
     */
    private static QRCodeType getType(String result) {
        if (result == null) {
            return QRCodeType.NONE;
        }

        if (result.isEmpty()) {
            return QRCodeType.NONE;
        }

        return QRCodeType.getType(result);
    }

    public static EmailContentType getContentSendMail(String text) {
        if (text.startsWith("TO:")) {
            return EmailContentType.SEND_TO;
        }

        if (text.startsWith("SUB:")) {
            return EmailContentType.SUBJECT;
        }

        if (text.startsWith("BODY:")) {
            return EmailContentType.BODY;
        }

        return EmailContentType.NONE;
    }
}
