package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.URIResultParser;

public class UriParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        URIResultParser parser = new URIResultParser();
        callback.accessUri(parser.parse(result));
    }
}
