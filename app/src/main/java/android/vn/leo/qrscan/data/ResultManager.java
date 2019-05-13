package android.vn.leo.qrscan.data;

import java.util.ArrayList;
import java.util.List;

public class ResultManager {

    private final List<ScanResult> resultList;

    private static ResultManager instance;

    public static synchronized ResultManager getInstance() {
        if (instance == null) {
            instance = new ResultManager();
        }
        return instance;
    }

    private ResultManager() {
        resultList = new ArrayList<>();
    }

    public void add(final ScanResult result) {
        this.resultList.add(result);
    }

    public ScanResult get(final int index) {
        return this.resultList.get(index);
    }

    public List<ScanResult> getResultList() {
        return resultList;
    }

    public int size() {
        return resultList.size();
    }
}
