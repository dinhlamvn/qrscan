package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.WifiParsedResult;
import com.google.zxing.client.result.WifiResultParser;

public class WifiParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        WifiResultParser parser = new WifiResultParser();
        callback.accessWifi(parser.parse(result));
    }
}
