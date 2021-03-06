package android.vn.leo.qrscan.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.data.ScanResult;
import android.widget.Toast;

import com.google.zxing.client.result.ParsedResultType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
            Toast.makeText(context, context.getResources().getString(R.string.toast_message_copied),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check the object is null
     * @param target the object will be check
     */
    public static boolean isNull(Object target) {
        return target == null;
    }

    /**
     * Check the object is not null
     * @param target the object
     * @return true if it not null, else return false
     */
    public static boolean isNotNull(Object target) {
        return target != null;
    }

    public static String getNameOfImage(Date date) {
        return "code_" + date.getTime() + ".png";
    }

    public static int getIndexOfType(String type) {
        ParsedResultType[] types = ParsedResultType.values();

        for (int i = 0; i < types.length; i++) {
            if (types[i].toString().equals(type)) {
                return i;
            }
        }

        return 0;
    }

    public static void share(@NonNull Context context, @NonNull List<String> strings) {
        StringBuilder sb = new StringBuilder();

        for (String str : strings) {
            sb.append(str)
                    .append("\n")
                    .append("+----------------------------------------------------+")
                    .append("\n");
        }

        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        sharedIntent.setType("text/plain");
        sharedIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        String shareMessage = sb.toString();
        sharedIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        context.startActivity(Intent.createChooser(sharedIntent, context.getResources().getString(R.string.chose_app_share)));
    }

    /**
     * The function to share a message to via other app
     * @param context The context
     * @param shareMessage The message to share
     */
    public static void share(@NonNull Context context, @NonNull String shareMessage) {
        Intent sharedIntent = new Intent(Intent.ACTION_SEND);
        sharedIntent.setType("text/plain");
        sharedIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        sharedIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        context.startActivity(Intent.createChooser(sharedIntent, context.getResources().getString(R.string.chose_app_share)));
    }

    /**
     * Put the delete list to local storage, it will be use in first run app.
     * @param deleteList The list of code will be delete
     */
    public static void putDeleteList(List<ScanResult> deleteList) {
        int length = deleteList.size();
        StringBuilder list = new StringBuilder();

        for (int i = 0; i < length; i++) {
            if (i < length - 1) {
                list.append(deleteList.get(i).getId()).append(",");
                continue;
            }
            list.append(deleteList.get(i).getId());
        }

        if (list.toString().isEmpty()) {
            return;
        }
        LocalStorageManager.setDeleteCodeList(list.toString());
    }

    /**
     * Fetch the delete list from storage
     * @return The list to delete
     */
    @Nullable
    public static List<String> fetchDeleteList() {
        String str = LocalStorageManager.getDeleteCodeList();
        if (str.isEmpty()) {
            return null;
        }
        String[] list = LocalStorageManager.getDeleteCodeList().split(",");
        clearDeleteList();
        return Arrays.asList(list);
    }

    /**
     * Clear the delete list from storage
     */
    public static void clearDeleteList() {
        LocalStorageManager.setDeleteCodeList("");
    }
}
