package android.vn.leo.qrscan.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.vn.leo.qrscan.fragments.HistoryFragment;
import android.vn.leo.qrscan.fragments.ScanFragment;
import android.vn.leo.qrscan.interfaces.OnClickHistoryItemCallback;
import android.vn.leo.qrscan.interfaces.OnResultCallback;

import java.util.List;

public class MainScreenAdapter extends FragmentPagerAdapter {

    private static final int MAX_SCREEN_SIZE = 2;

    private List<String> titles = null;

    private ScanFragment mScanFragment;

    private HistoryFragment mHistoryFragment;

    public MainScreenAdapter(FragmentManager fm) {
        super(fm);
        mScanFragment = new ScanFragment();
        mHistoryFragment = new HistoryFragment();
    }

    public void attachResultCallback(OnResultCallback onResultCallback) {
        this.mScanFragment.withResultCallback(onResultCallback);
    }

    public void attachOnClickItemCallback(OnClickHistoryItemCallback onClickHistoryItemCallback) {
        this.mHistoryFragment.addClickHistoryItemCallback(onClickHistoryItemCallback);
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0 ? mScanFragment : mHistoryFragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (this.titles != null) {
            return this.titles.get(position);
        }
        return super.getPageTitle(position);
    }

    public void withListTitle(List<String> titles) {
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return MAX_SCREEN_SIZE;
    }
}
