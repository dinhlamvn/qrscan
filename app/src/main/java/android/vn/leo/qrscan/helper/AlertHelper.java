package android.vn.leo.qrscan.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.vn.leo.qrscan.interfaces.IDialogCallback;
import android.vn.leo.qrscan.utils.CommonMethod;

public final class AlertHelper {

    /**
     * Show confirm dialog without dismiss when user click the outside,
     * see {@link #showConfirmDialog(Context, String, String, IDialogCallback, boolean, View, String...)}
     * @param context The context
     * @param title The title
     * @param message The message
     * @param callback The callback when user interactive with dialog
     * @param buttons List string of button, max value is 3 for Positive, Negative and Neutral
     */
    public static void showConfirmDialog(@NonNull Context context,
                                         String title, String message,
                                         final IDialogCallback callback,
                                         String... buttons) {
        showConfirmDialog(context, title, message, callback, false, null, buttons);
    }

    public static void showConfirmDialog(@NonNull Context context,
                                         int titleRes, int messageRes,
                                         final IDialogCallback callback,
                                         final boolean isCancelable,
                                         final View view,
                                         Integer... buttonRes) {
        String[] strings = {
                context.getResources().getString(buttonRes[0]),
                context.getResources().getString(buttonRes[1]),
                context.getResources().getString(buttonRes[2])
        };
        showConfirmDialog(context,
                context.getResources().getString(titleRes),
                context.getResources().getString(messageRes),
                callback, isCancelable, view, strings[0], strings[1], strings[2]);
    }


    /**
     * Show confirm alert dialog to user
     * @param context The context
     * @param title The title
     * @param message The message
     * @param callback The callback when user interactive with dialog
     * @param customView The custom view of dialog
     * @param isCancelable User can dismiss dialog when click outside
     * @param buttons List string of button, max value is 3 for Positive, Negative and Neutral
     */
    public static void showConfirmDialog(@NonNull Context context,
                                         String title, String message,
                                         final IDialogCallback callback,
                                         final boolean isCancelable,
                                         final View customView,
                                         String... buttons) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        if (message != null) {
            builder.setMessage(Html.fromHtml(message));
        }
        builder.setCancelable(isCancelable);

        if (customView != null) {
            builder.setView(customView);
        }

        if (buttons.length >= 1) {
            builder.setPositiveButton(buttons[0], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (CommonMethod.isNotNull(callback)) {
                        callback.onAction1();
                    }
                }
            });
        }

        if (buttons.length >= 2) {
            builder.setNegativeButton(buttons[1], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (CommonMethod.isNotNull(callback)) {
                        callback.onAction2();
                    }
                }
            });
        }

        if (buttons.length >= 3) {
            builder.setNeutralButton(buttons[0], new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (CommonMethod.isNotNull(callback)) {
                        callback.onAction3();
                    }
                }
            });
        }

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (CommonMethod.isNotNull(callback)) {
                    callback.onActionCancel();
                }
            }
        });

        final AlertDialog alert = builder.create();
        if (isCancelable) {
            alert.setCanceledOnTouchOutside(true);
        }
        alert.show();
    }
}
