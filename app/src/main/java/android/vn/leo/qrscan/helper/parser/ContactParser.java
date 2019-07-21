package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookAUResultParser;
import com.google.zxing.client.result.AddressBookDoCoMoResultParser;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.VCardResultParser;

public class ContactParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        AddressBookAUResultParser parser1 = new AddressBookAUResultParser();
        AddressBookDoCoMoResultParser parser2 = new AddressBookDoCoMoResultParser();
        VCardResultParser parser3 = new VCardResultParser();

        ParsedResult parsedResult = parser1.parse(result);

        if (parsedResult == null) {
            parsedResult = parser2.parse(result);
        }

        if (parsedResult == null) {
            parsedResult = parser3.parse(result);
        }

        if (parsedResult == null) {
            callback.copyText(ResultParser.parseResult(result));
            return;
        }

        callback.addNewContact(parsedResult);
    }
}
