package android.vn.leo.qrscan.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorageManager {

    private static final String PREF_NAME = "scan_pref";

    private static SharedPreferences preferences;

    public static void init(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setAutoCopyAfterScan(boolean value) {
        preferences.edit().putBoolean(KeyManager.AUTO_COPY_AFTER_SCAN_KEY, value).apply();
    }

    public static boolean isAutoCopyAfterScan() {
        return preferences.getBoolean(KeyManager.AUTO_COPY_AFTER_SCAN_KEY, true);
    }

    public static void setAutoOpenWebBrowser(boolean value) {
        preferences.edit().putBoolean(KeyManager.AUTO_OPEN_WEB_BROWSER_KEY, value).apply();
    }

    public static boolean isAutoOpenWebBrowser() {
        return preferences.getBoolean(KeyManager.AUTO_OPEN_WEB_BROWSER_KEY, false);
    }

    public static void setAutoOpenCallToPhoneNumber(boolean value) {
        preferences.edit().putBoolean(KeyManager.AUTO_CALL_TO_PHONE_NUMBER, value).apply();
    }

    public static boolean isAutoCallToPhoneNumber() {
        return preferences.getBoolean(KeyManager.AUTO_CALL_TO_PHONE_NUMBER, false);
    }

    class KeyManager {
        final static String AUTO_COPY_AFTER_SCAN_KEY = "auto_copy_after_scan";
        final static String AUTO_OPEN_WEB_BROWSER_KEY = "auto_open_web_browser";
        final static String AUTO_CALL_TO_PHONE_NUMBER = "auto_call_to_phone_number";
    }
}
