package android.vn.leo.qrscan.utils;

import android.content.Context;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.data.QRCodeType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FormatUtility {

    /**
     * Convert type of code to string
     * @param type The type of code
     * @return The value of type in string, but not found any type for code, return empty string
     */
    public static String convertTypeToString(QRCodeType type) {
        switch (type) {
            case NONE: return "";
            case TEXT: return "Text";
            case CALL: return "Call";
            case SMS: return "SMS";
            case URL: return "URL";
            case EMAIL: return "Email";
            case VCARD: return "Contact";
            case BARCODE: return "Barcode";
            default: return "";
        }
    }

    /**
     * Get action string with type of code
     * @param context The context
     * @param type The type of code
     * @return The action string of code, if not found for type, return <b>USE</b>
     */
    public static String getUseStringFromType(Context context, QRCodeType type) {
        switch (type) {
            case NONE: return "";
            case TEXT: return context.getResources().getString(R.string.use_with_text);
            case SMS: return context.getResources().getString(R.string.use_with_sms);
            case CALL: return context.getResources().getString(R.string.use_with_call);
            case URL: return context.getResources().getString(R.string.use_with_url);
            case EMAIL: return context.getResources().getString(R.string.use_with_email);
            case VCARD: return context.getResources().getString(R.string.use_with_vcard);
            case BARCODE: return context.getResources().getString(R.string.use_with_barcode);
            default: return context.getResources().getString(R.string.dialog_result_confirm_1);
        }
    }

    /**
     * Format the time from date to {@code hh:mm a}. Example for <b>2019/01/01 10:30:00</b>, it will format to
     * <b>10:30 AM</b>
     * @param date The date want to format
     * @return The string after format with <b><i>hh:mm a</i></b>
     */
    public static String formatTimeStamp(Date date) {
        if (date == null) return "";
        DateFormat dfm = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dfm.format(date);
    }

    /**
     * Format the time from date to {@code E MMM dd yyyy}. Example for <b>Monday 2019/01/01 10:30:00</b>, it will format to
     * <b>Mon Jan 01 2019</b>
     * @param date The date want to format
     * @return The string after format with <b><i>E MMM dd yyyy</i></b>
     */
    public static String formatDateGroup(Date date) {
        if (date == null) return "";
        DateFormat dfm = new SimpleDateFormat("E MMM dd yyyy", Locale.getDefault());
        return dfm.format(date);
    }
}
