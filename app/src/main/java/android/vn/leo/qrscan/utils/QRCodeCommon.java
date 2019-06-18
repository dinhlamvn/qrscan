package android.vn.leo.qrscan.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.vn.leo.qrscan.data.QRCodeType;
import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.interfaces.OnUseCallback;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Date;

public class QRCodeCommon {

    /**
     * Get type of QRCode and call back to do something
     * @param result The result text
     * @param callback The callback after get type
     */
    public static void  getTypeQRCodeToUse(ScanResult result, OnUseCallback callback) {
        QRCodeType type = getType(result.getResult());

        switch (type) {
            case TEXT: {
                callback.useWithText(result);
                return;
            }
            case URL: {
                callback.useWithUrl(result);
                return;
            }
            case SMS: {
                callback.useWithSms(result);
                return;
            }
            case CALL: {
                callback.useWithCall(result);
                return;
            }
            case EMAIL: {
                callback.useWithEmail(result);
                return;
            }
            case VCARD: {
                callback.useWithVCard(result);
                return;
            }
        }

        callback.useWithNone(result);
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

    public static String getNameOfImage(Date date) {
        return "code_" + date.getTime() + ".png";
    }

    /*public static Bitmap generateQRCodeImage(String text) {
        final int BLACK = 0xFF000000;
        final int WHITE = 0xFFFFFFFF;
        final int width = 150;
        final int height = 150;
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, null);
        } catch (WriterException e) {
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
        return bitmap;
    }*/
}
