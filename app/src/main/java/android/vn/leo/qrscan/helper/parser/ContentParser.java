package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.interfaces.CodeResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResultType;

public class ContentParser {

    private final ResultWorker resultWorker;

    public ContentParser(ResultWorker resultWorker) {
        this.resultWorker = resultWorker;
    }

    public void startParse(ScanResult result) {
        ParsedResultType type = result.getType();
        CodeResultParser parser = null;
        switch (type) {
            case ADDRESSBOOK: {
                parser = new ContactParser();
                break;
            }
            case EMAIL_ADDRESS: {
                parser = new EmailParser();
                break;
            }
            case TEXT: {
                parser = new TextParser();
                break;
            }
            case PRODUCT: {
                parser = new ProductParser();
                break;
            }
            case SMS: {
                parser = new SMSParser();
                break;
            }
            case TEL: {
                parser = new TellParser();
                break;
            }
            case URI: {
                parser = new UriParser();
                break;
            }
            case WIFI: {
                parser = new WifiParser();
                break;
            }
        }
        if (parser != null) {
            Result result1 = new Result(result.getResult(), null, null, null);
            parser.parse(result1, this.resultWorker);
        }
    }
}
