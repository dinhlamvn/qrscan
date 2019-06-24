package android.vn.leo.qrscan.utils;

import android.content.Context;
import android.vn.leo.qrscan.R;

import com.google.zxing.client.result.ParsedResultType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FormatUtility {

    public static String getTitleResultFromType(Context context, ParsedResultType type) {
        switch (type) {
            case TEXT: return context.getResources().getString(R.string.title_with_text);
            case SMS: return context.getResources().getString(R.string.title_with_sms);
            case TEL: return context.getResources().getString(R.string.title_with_call);
            case URI: return context.getResources().getString(R.string.title_with_url);
            case EMAIL_ADDRESS: return context.getResources().getString(R.string.title_with_email);
            case ADDRESSBOOK: return context.getResources().getString(R.string.title_with_vcard);
            case PRODUCT: return context.getResources().getString(R.string.title_with_barcode);
            case WIFI: return context.getResources().getString(R.string.title_with_wifi);
            default: return context.getResources().getString(R.string.dialog_result_title);
        }
    }


    /**
     * Get action string with type of code
     * @param context The context
     * @param type The type of code
     * @return The action string of code, if not found for type, return <b>USE</b>
     */
    public static String getUseStringFromType(Context context, ParsedResultType type) {
        switch (type) {
            case TEXT: return context.getResources().getString(R.string.use_with_text);
            case SMS: return context.getResources().getString(R.string.use_with_sms);
            case TEL: return context.getResources().getString(R.string.use_with_call);
            case URI: return context.getResources().getString(R.string.use_with_url);
            case EMAIL_ADDRESS: return context.getResources().getString(R.string.use_with_email);
            case ADDRESSBOOK: return context.getResources().getString(R.string.use_with_vcard);
            case PRODUCT: return context.getResources().getString(R.string.use_with_product);
            case WIFI: return context.getResources().getString(R.string.use_with_wifi);
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
