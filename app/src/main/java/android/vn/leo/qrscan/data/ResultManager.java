package android.vn.leo.qrscan.data;

import android.vn.leo.qrscan.utils.CommonMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultManager {

    private final List<ScanResult> resultList;
    private final List<ScanResult> removedList;

    private static ResultManager instance;

    public static synchronized ResultManager getInstance() {
        if (instance == null) {
            instance = new ResultManager();
        }
        return instance;
    }

    private ResultManager() {
        resultList = new ArrayList<>();
        removedList = new ArrayList<>();
    }

    public void add(final ScanResult result) {
        if (this.resultList.size() == 0) {
            this.resultList.add(result);
            return;
        }
        this.resultList.add(0, result);
    }

    public boolean add(final int pos, final ScanResult scanResult) {
        if (pos >= 0 && pos <= resultList.size()) {
            resultList.add(pos, scanResult);
            return true;
        }
        return false;
    }

    public ScanResult remove(int pos) {
        if (pos >= 0 && pos < resultList.size()) {
            return resultList.remove(pos);
        }
        return null;
    }

    public void addRemoveItem(ScanResult it) {
        removedList.add(it);
    }

    public void clearItemInRemoved(ScanResult it) {
        removedList.remove(it);
    }

    public int currentSizeOfRemovedList() {
        return removedList.size();
    }

    public List<ScanResult> getRemovedList() {
        List<ScanResult> out = new ArrayList<>(removedList);
        removedList.clear();
        return out;
    }

    public void addAll(List<ScanResult> list) {
        this.resultList.addAll(list);
    }

    public void release() {
        this.resultList.clear();
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
