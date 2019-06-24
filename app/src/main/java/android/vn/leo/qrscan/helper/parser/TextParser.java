package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ResultParser;

public class TextParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        callback.copyText(ResultParser.parseResult(result));
    }
}
