package android.vn.leo.qrscan.data;

import android.vn.leo.qrscan.utils.CommonMethod;

import java.security.PublicKey;

public enum QRCodeType {

    NONE(0),
    TEXT(1),
    URL(2),
    EMAIL(3),
    SMS(4),
    CALL(5),
    VCARD(6),
    BARCODE(7);

    private int value;

    QRCodeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static final String URL_PREFIX_1 = "https://";
    public static final String URL_PREFIX_2 = "http://";
    public static final String EMAIL_PREFIX_1 = "MAILTO:";
    public static final String EMAIL_PREFIX_2 = "MATMSG:";
    public static final String SMS_PREFIX = "SMSTO:";
    public static final String CALL_PREFIX = "tel:";
    public static final String VCARD_PREFIX_1 = "BEGIN:VCARD";
    public static final String VCARD_PREFIX_2 = "END:VCARD\n";
    public static final String VCARD_PREFIX_3 = "END:VCARD";

    public static QRCodeType getType(String text) {

        if (CommonMethod.isNull(text) || text.isEmpty()) {
            return NONE;
        }

        if (text.startsWith(URL_PREFIX_1) || text.startsWith(URL_PREFIX_2)) {
            return URL;
        }

        if (text.startsWith(EMAIL_PREFIX_1)) {
            return EMAIL;
        }

        if (text.startsWith(EMAIL_PREFIX_2)) {
            String s = text.substring(EMAIL_PREFIX_2.length(), text.length());
            String[] arr = s.split("[;]");

            if (arr.length == 3 && arr[0].startsWith("TO:")
                    && arr[1].startsWith("SUB:") && arr[2].startsWith("BODY:")) {
                return EMAIL;
            }
        }

        if (text.startsWith(SMS_PREFIX)) {
            return SMS;
        }

        if (text.startsWith(CALL_PREFIX)) {
            return CALL;
        }

        if (text.startsWith(VCARD_PREFIX_1) && (text.endsWith(VCARD_PREFIX_2) || text.endsWith(VCARD_PREFIX_3))) {
            return VCARD;
        }

        return TEXT;
    }
}
