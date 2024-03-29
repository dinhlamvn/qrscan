package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.CodeResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.WifiParsedResult;
import com.google.zxing.client.result.WifiResultParser;

public class WifiParser implements CodeResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        WifiResultParser parser = new WifiResultParser();

        WifiParsedResult parsedResult = parser.parse(result);

        if (parsedResult == null) {
            callback.copyText(ResultParser.parseResult(result));
            return;
        }

        callback.accessWifi(parsedResult);
    }
}
