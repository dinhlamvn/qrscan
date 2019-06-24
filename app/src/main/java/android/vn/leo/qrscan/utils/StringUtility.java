package android.vn.leo.qrscan.utils;

import java.util.Arrays;

public class StringUtility {

    public static String getStringValueIfNotNull(Object target) {
        return CommonMethod.isNotNull(target) ? target.toString() : "";
    }

    public static String getStringValueIfNotNullFromArray(String[] array, int index) {
        if (array == null) return "";
        if (index < 0 || index > array.length - 1) return "";
        return getStringValueIfNotNull(array[index]);
    }

    public static String getStringListIfNotNullFromArray(String[] array) {
        if (CommonMethod.isNull(array)) return "";
        if (array.length == 0) return "";
        return Arrays.toString(array).replaceAll("[\\[\\]]", "");
    }
}
