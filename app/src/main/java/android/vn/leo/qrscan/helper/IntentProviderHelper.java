package android.vn.leo.qrscan.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

public final class IntentProviderHelper {

    public static Intent provideIntent(@NonNull Activity activity, @NonNull Class<?> clazz) {
        return new Intent(activity, clazz);
    }

    public static Intent provideIntent(@NonNull Activity activity, @NonNull Class<?> clazz, Bundle args) {
        if (args == null) return provideIntent(activity, clazz);
        return new Intent(activity, clazz).putExtras(args);
    }

    public static Intent provideIntent(@NonNull String action, Uri data) {
        return new Intent(action, data);
    }
}
