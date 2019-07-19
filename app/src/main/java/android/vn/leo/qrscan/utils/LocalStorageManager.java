package android.vn.leo.qrscan.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorageManager {

    private static final String PREF_NAME = "scan_pref";

    private static SharedPreferences preferences;

    public static void init(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setAutoUseAfterScan(boolean value) {
        preferences.edit().putBoolean(KeyManager.AUTO_USE_RESULT, value).apply();
    }

    public static boolean isAutoUseAfterScan() {
        return preferences.getBoolean(KeyManager.AUTO_USE_RESULT, false);
    }

    public static void setAutoCopyAfterScan(boolean value) {
        preferences.edit().putBoolean(KeyManager.AUTO_COPY_AFTER_SCAN_KEY, value).apply();
    }

    public static boolean isAutoCopyAfterScan() {
        return preferences.getBoolean(KeyManager.AUTO_COPY_AFTER_SCAN_KEY, true);
    }

    public static void setTurnOnConfirmMoveWeb(boolean value) {
        preferences.edit().putBoolean(KeyManager.TURN_ON_CONFIRM_MOVE_WEB, value).apply();
    }

    public static boolean isTurnOnConfirmMoveWeb() {
        return preferences.getBoolean(KeyManager.TURN_ON_CONFIRM_MOVE_WEB, false);
    }

    public static void setTurnOnConfirmCallPhone(boolean value) {
        preferences.edit().putBoolean(KeyManager.TURN_ON_CONFIRM_CALL_PHONE, value).apply();
    }

    public static boolean isTurnOnConfirmCallPhone() {
        return preferences.getBoolean(KeyManager.TURN_ON_CONFIRM_CALL_PHONE, false);
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
        return preferences.getBoolean(KeyManager.ENABLE_SAVE_CODE_IMAGE, false);
    }

    public static void setDeleteCodeList(String list) {
        preferences.edit().putString(KeyManager.DELETE_CODE_LIST, list).apply();
    }

    public static String getDeleteCodeList() {
        return preferences.getString(KeyManager.DELETE_CODE_LIST, "");
    }

    class KeyManager {
        static final String AUTO_USE_RESULT = "setting_auto_use_result";
        static final String AUTO_COPY_AFTER_SCAN_KEY = "setting_auto_copy_after_scan";
        static final String TURN_ON_CONFIRM_MOVE_WEB = "setting_auto_open_web_browser";
        static final String TURN_ON_CONFIRM_CALL_PHONE = "setting_auto_call_to_phone_number";
        static final String ENABLE_SOUND = "setting_enable_sound";
        static final String ENABLE_VIBRATE = "setting_enable_vibrate";
        static final String ENABLE_SAVE_CODE_IMAGE = "setting_enable_save_code";
        static final String DELETE_CODE_LIST = "delete_code_list";
    }
}
