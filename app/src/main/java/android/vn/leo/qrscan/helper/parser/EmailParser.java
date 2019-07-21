package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.EmailAddressResultParser;
import com.google.zxing.client.result.EmailDoCoMoResultParser;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

public class EmailParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        EmailAddressResultParser parser = new EmailAddressResultParser();
        EmailDoCoMoResultParser parser1 = new EmailDoCoMoResultParser();

        ParsedResult parsedResult = parser.parse(result);

        if (parsedResult == null) {
            parsedResult = parser1.parse(result);
        }

        if (parsedResult == null) {
            callback.copyText(ResultParser.parseResult(result));
            return;
        }

        callback.sendEmail(parsedResult);
    }
}
