package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;

public class ContentParser {

    private ResultWorker resultWorker;

    public ContentParser(ResultWorker resultWorker) {
        this.resultWorker = resultWorker;
    }

    public void startParse(Result result) {
        ParsedResultType type = ResultParser.parseResult(result).getType();
        IResultParser parser = null;
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
            parser.parse(result, this.resultWorker);
        }
    }
}
