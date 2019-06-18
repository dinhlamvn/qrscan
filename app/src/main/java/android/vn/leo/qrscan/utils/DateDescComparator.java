package android.vn.leo.qrscan.utils;

import android.vn.leo.qrscan.data.ScanResult;

import java.util.Comparator;

public class DateDescComparator implements Comparator<ScanResult> {

    @Override
    public int compare(ScanResult o1, ScanResult o2) {
        long t1 = o1.getDate().getTime();
        long t2 = o2.getDate().getTime();
        return (int)(t1 - t2) * -1;
    }
}
