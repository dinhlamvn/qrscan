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

    public static void setEnableSound(boolean value) {
        preferences.edit().putBoolean(KeyManager.ENABLE_SOUND, value).apply();
    }

    public static boolean isEnableSound() {
        return preferences.getBoolean(KeyManager.ENABLE_SOUND, false);
    }

    public static void setEnableVibrate(boolean value) {
        preferences.edit().putBoolean(KeyManager.ENABLE_VIBRATE, value).apply();
    }

    public static boolean isEnableVibrate() {
        return preferences.getBoolean(KeyManager.ENABLE_VIBRATE, false);
    }

    public static void setEnableSaveCodeImage(boolean value) {
        preferences.edit().putBoolean(KeyManager.ENABLE_SAVE_CODE_IMAGE, value).apply();
    }

    public static boolean isEnableSaveCodeImage() {
        return preferences.getBoolean(KeyManager.ENABLE_SAVE_CODE_IMAGE, true);
    }

    class KeyManager {
        static final String AUTO_COPY_AFTER_SCAN_KEY = "setting_auto_copy_after_scan";
        static final String AUTO_OPEN_WEB_BROWSER_KEY = "setting_auto_open_web_browser";
        static final String AUTO_CALL_TO_PHONE_NUMBER = "setting_auto_call_to_phone_number";
        static final String ENABLE_SOUND = "setting_enable_sound";
        static final String ENABLE_VIBRATE = "setting_enable_vibrate";
        static final String ENABLE_SAVE_CODE_IMAGE = "setting_enable_save_code";
    }
}
