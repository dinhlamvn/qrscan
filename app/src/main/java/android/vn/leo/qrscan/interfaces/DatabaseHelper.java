package android.vn.leo.qrscan.interfaces;

import android.vn.leo.qrscan.data.ScanResult;

import java.util.List;

public interface DatabaseHelper {

    List<ScanResult> read();

    long insert(ScanResult scanResult);

    boolean update(ScanResult scanResult);

    boolean remove(ScanResult scanResult);
}
