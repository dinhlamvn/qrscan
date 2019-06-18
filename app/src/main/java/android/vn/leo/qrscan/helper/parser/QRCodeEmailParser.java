package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.data.qrcode.QRCodeBaseData;
import android.vn.leo.qrscan.data.qrcode.QRCodeEmail;
import android.vn.leo.qrscan.interfaces.IQRCodeParser;
import android.vn.leo.qrscan.utils.QRCodeCommon;

import java.util.HashMap;
import java.util.Map;

public class QRCodeEmailParser implements IQRCodeParser {

    private final Map<String, String> dataMap = new HashMap<>();

    @Override
    public QRCodeBaseData parse(String code) {
        QRCodeEmail email = new QRCodeEmail();

        // Remove MAILTO: from source
        String[] args = code.substring(7, code.length() - 1).split(";");

        if (args.length == 0) {
            email.setReceiver(code.substring(7));
            return email;
        } else {
            for (String s : args) {
                handel(s);
            }
        }

        // Set value for email object
        email.setReceiver(getData("TO"));
        email.setSubject(getData("SUB"));
        email.setContent(getData("BODY"));

        return email;
    }

    public String getData(String key) {
        return dataMap.get(key) == null ? "" : dataMap.get(key);
    }

    private void handel(String element) {
        if (element.startsWith("TO:")) {
            dataMap.put("TO", element.substring(element.indexOf(":") + 1));
        } else if (element.startsWith("SUB:")) {
            dataMap.put("SUB", element.substring(element.indexOf(":") + 1));
        } else if (element.startsWith("BODY:")) {
            dataMap.put("BODY", element.substring(element.indexOf(":") + 1));
        }
    }
}
