package android.vn.leo.qrscan.fragments;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.adapters.HistoryAdapter;
import android.vn.leo.qrscan.data.ResultManager;
import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.interfaces.OnClickHistoryItemCallback;

import com.google.zxing.Result;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView.Adapter mAdapter;
    private MutableLiveData<List<ScanResult>> observe;
    public static final String ACTION_UPDATE = "leo.vn.android.action.UPDATE_HISTORY";
    public OnClickHistoryItemCallback clickHistoryItemCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observe = new MutableLiveData<>();

        observe.setValue(ResultManager.getInstance().getResultList());

        observe.observe(new LifecycleOwner() {
            @NonNull
            @Override
            public Lifecycle getLifecycle() {
                return getActivity().getLifecycle();
            }
        }, new Observer<List<ScanResult>>() {
            @Override
            public void onChanged(@Nullable List<ScanResult> strings) {
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getStringExtra("data") != null) {
                    String data = intent.getStringExtra("data");
                    Bitmap bitmap = intent.getParcelableExtra("image");
                    ScanResult scanResult = new ScanResult(data, bitmap);
                    scanResult.setDate(new Date());
                    ResultManager.getInstance().add(scanResult);
                    observe.postValue(ResultManager.getInstance().getResultList());
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, filter);
    }

    public void addClickHistoryItemCallback(OnClickHistoryItemCallback onClickHistoryItemCallback) {
        this.clickHistoryItemCallback = onClickHistoryItemCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.history_recycler_view);
        recyclerView.setHasFixedSize(true);
        mAdapter = new HistoryAdapter();
        ((HistoryAdapter) mAdapter).addClickHistoryItemCallback(clickHistoryItemCallback);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
