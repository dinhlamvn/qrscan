package android.vn.leo.qrscan.interfaces;

import com.google.zxing.client.result.ParsedResult;

public interface ResultWorker {

    void copyText(ParsedResult result);

    void sendSMS(ParsedResult result);

    void callPhone(ParsedResult result);

    void sendEmail(ParsedResult result);

    void addNewContact(ParsedResult result);

    void accessUri(ParsedResult result);

    void accessWifi(ParsedResult result);

    void researchProduct(ParsedResult result);
}
