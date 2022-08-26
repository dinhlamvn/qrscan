package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.CodeResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;

public class TextParser implements CodeResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        callback.copyText(ResultParser.parseResult(result));
    }
}
