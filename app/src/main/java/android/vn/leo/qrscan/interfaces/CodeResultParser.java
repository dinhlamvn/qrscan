package android.vn.leo.qrscan.interfaces;

import com.google.zxing.Result;

public interface CodeResultParser {

    void parse(Result result, ResultWorker callback);
}
