package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.EmailAddressResultParser;

public class EmailParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        EmailAddressResultParser parser = new EmailAddressResultParser();
        callback.sendEmail(parser.parse(result));
    }
}
