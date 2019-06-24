package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.TelResultParser;

public class TellParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        TelResultParser parser = new TelResultParser();
        callback.callPhone(parser.parse(result));
    }
}
