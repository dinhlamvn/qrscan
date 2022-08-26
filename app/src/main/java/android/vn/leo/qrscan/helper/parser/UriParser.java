package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.CodeResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.URIParsedResult;
import com.google.zxing.client.result.URIResultParser;

public class UriParser implements CodeResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        URIResultParser parser = new URIResultParser();

        URIParsedResult parsedResult = parser.parse(result);

        if (parsedResult == null) {
            callback.copyText(ResultParser.parseResult(result));
            return;
        }

        callback.accessUri(parsedResult);
    }
}
