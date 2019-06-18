package android.vn.leo.qrscan.interfaces;

import android.vn.leo.qrscan.data.qrcode.QRCodeBaseData;

public interface IQRCodeParser {

    QRCodeBaseData parse(String code);
}
