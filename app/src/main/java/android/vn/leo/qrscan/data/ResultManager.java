package android.vn.leo.qrscan.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import androidx.annotation.Nullable;

public class ResultManager {

    private final List<ScanResult> resultList;
    private final TreeMap<Integer, ScanResult> removedList;

    private static ResultManager instance;

    public static synchronized ResultManager getInstance() {
        if (instance == null) {
            instance = new ResultManager();
        }
        return instance;
    }

    private ResultManager() {
        resultList = new ArrayList<>();
        removedList = new TreeMap<>();
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

    public boolean remove(ScanResult item) {
        return this.resultList.size() > 0 && this.resultList.contains(item) && this.resultList.remove(item);
    }

    public void addRemoveItem(int position, ScanResult it) {
        removedList.put(position, it);
    }

    public void clearItemInRemoved(int key) {
        if (this.removedList.size() > 0 && this.removedList.containsKey(key)) {
            removedList.remove(key);
        }
    }

    public int currentSizeOfRemovedList() {
        return removedList.size();
    }

    @Nullable
    public ScanResult getRemovedItem(int key) {
        if (key >= 0 && this.removedList.containsKey(key)) {
            return this.removedList.get(key);
        }
        return null;
    }

    public Set<Integer> getPositionRemovedList() {
        return this.removedList.keySet();
    }

    public Collection<ScanResult> getRemovedList() {
        return this.removedList.values();
    }

    public void releaseRemoveList() {
        this.removedList.clear();
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
