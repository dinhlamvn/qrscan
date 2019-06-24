package android.vn.leo.qrscan.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.vn.leo.qrscan.fragments.HistoryFragment;
import android.vn.leo.qrscan.fragments.ScanFragment;

import java.util.List;

public class MainScreenAdapter extends FragmentPagerAdapter {

    private static final int MAX_SCREEN_SIZE = 2;

    private List<String> titles = null;

    private Fragment fragment1, fragment2;

    public MainScreenAdapter(FragmentManager fm) {
        super(fm);
        fragment1 = new ScanFragment();
        fragment2 = new HistoryFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: return fragment1;
            case 1: return fragment2;
        }
        return null;
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
