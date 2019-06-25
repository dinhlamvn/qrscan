package android.vn.leo.qrscan.helper.parser;

import android.vn.leo.qrscan.interfaces.IResultParser;
import android.vn.leo.qrscan.interfaces.ResultWorker;

import com.google.zxing.Result;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.ProductResultParser;
import com.google.zxing.client.result.ResultParser;

public class ProductParser implements IResultParser {

    @Override
    public void parse(Result result, ResultWorker callback) {
        ProductResultParser parser = new ProductResultParser();
        callback.researchProduct(ResultParser.parseResult(result));
    }
}
