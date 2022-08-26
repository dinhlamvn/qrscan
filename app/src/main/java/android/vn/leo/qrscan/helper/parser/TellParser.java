package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.CodeResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.TelResultParser;

public class TellParser implements CodeResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        TelResultParser parser = new TelResultParser();

        ParsedResult parsedResult = parser.parse(result);

        if (parsedResult == null) {
            callback.copyText(ResultParser.parseResult(result));
            return;
        }
        callback.callPhone(parsedResult);
    }
}
