package android.vn.leo.qrscan.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.vn.leo.qrscan.data.QRCodeType;
import android.vn.leo.qrscan.interfaces.OnUseCallback;
import android.widget.Toast;

public final class CommonMethod {

    /**
     * Copy result to clip board, user can paste it in other place
     * @param context The context
     * @param result The result text
     */
    public static void copyResultToClipboard(Context context, String result) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("result", result);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "Copied to clipboard...!!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check the object is null
     * @param target the object will be check
     */
    public static boolean isNull(Object target) {
        return target == null;
    }
}
