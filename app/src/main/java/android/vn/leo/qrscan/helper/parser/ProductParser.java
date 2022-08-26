package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.CodeResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductResultParser;
import com.google.zxing.client.result.ResultParser;

public class ProductParser implements CodeResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        ProductResultParser parser = new ProductResultParser();
        ParsedResult parsedResult = parser.parse(result);

        if (parsedResult == null) {
            callback.researchProduct(ResultParser.parseResult(result));
            return;
        }
        callback.researchProduct(parsedResult);
    }
}
