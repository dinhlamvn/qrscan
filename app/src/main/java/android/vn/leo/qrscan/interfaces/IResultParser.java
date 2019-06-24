package android.vn.leo.qrscan.interfaces;

import com.google.zxing.Result;

public interface IResultParser {

    void parse(Result result, ResultWorker callback);
}
