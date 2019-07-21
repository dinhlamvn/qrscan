package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.SMSMMSResultParser;
import com.google.zxing.client.result.SMSParsedResult;
import com.google.zxing.client.result.SMSTOMMSTOResultParser;

public class SMSParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        SMSMMSResultParser parser1 = new SMSMMSResultParser();
        SMSTOMMSTOResultParser parser2 = new SMSTOMMSTOResultParser();

        ParsedResult parsedResult = parser1.parse(result);

        if (parsedResult == null) {
            parsedResult = parser2.parse(result);
        }

        if (parsedResult == null) {
            callback.copyText(ResultParser.parseResult(result));
            return;
        }

        callback.sendSMS(parsedResult);
    }
}
