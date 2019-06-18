package android.vn.leo.qrscan.fragments;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.vn.leo.qrscan.R;
import android.vn.leo.qrscan.activities.MainActivity;
import android.vn.leo.qrscan.adapters.HistoryAdapter;
import android.vn.leo.qrscan.adapters.HistoryItemTouchHelperCallback;
import android.vn.leo.qrscan.data.ResultManager;
import android.vn.leo.qrscan.data.ScanResult;
import android.vn.leo.qrscan.database.SQLiteHelper;
import android.vn.leo.qrscan.interfaces.OnClickHistoryItemCallback;
import android.vn.leo.qrscan.utils.Const;
import android.widget.Toast;

import java.util.List;

public class HistoryFragment extends Fragment implements OnClickHistoryItemCallback {

    private MainActivity mActivity;
    public HistoryAdapter mAdapter;
    private MutableLiveData<List<ScanResult>> observe;
    public static final String ACTION_UPDATE = "leo.vn.android.action.UPDATE_HISTORY";
    private Snackbar snackbar;

    // Action mode
    private ActionMode mActionMode;
    private ActionModeCallback actionModeCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        observe = new MutableLiveData<>();

        observe.setValue(ResultManager.getInstance().getResultList());

        observe.observe(new LifecycleOwner() {
            @NonNull
            @Override
            public Lifecycle getLifecycle() {
                return getActivity() != null ? getActivity().getLifecycle() : getLifecycle();
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
                if (intent != null && intent.getSerializableExtra("item") != null) {
                    final ScanResult scanResult = (ScanResult) intent.getSerializableExtra("item");
                    ResultManager.getInstance().add(scanResult);
                    observe.postValue(ResultManager.getInstance().getResultList());
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(broadcastReceiver, filter);

        actionModeCallback = new ActionModeCallback();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.history_recycler_view);
        recyclerView.setHasFixedSize(true);
        mAdapter = new HistoryAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, LinearLayoutManager.VERTICAL));
        ItemTouchHelper.Callback callback = new HistoryItemTouchHelperCallback(((HistoryAdapter) mAdapter).getItemSwipedOrMovedCallback());
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onClickHistoryItemCallback(int position) {
        if (mActionMode == null) {
            // Show alert with result and image of QRCode or Barcode
            mActivity.showResult(ResultManager.getInstance().get(position));
            return;
        }
        toggleSelection(position);
    }

    @Override
    public void onItemRemoveConfirm(final int position, final ScanResult item, final HistoryAdapter.OnChangeCallback changeCallback) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleSelection(position);
                final Handler handler = new Handler();
                final Runnable removeRunnable = new RemoveResult(item);
                handler.postDelayed(removeRunnable, Const.TIME_TO_REMOVE_RESULT);
                snackbar = Snackbar.make(mActivity.findViewById(R.id.view_pager), R.string.toast_message_removed, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.undo_text), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (item != null) {
                                    boolean isRestored = ResultManager.getInstance().add(position, item);
                                    if (isRestored) {
                                        Toast.makeText(mActivity, R.string.toast_message_restored, Toast.LENGTH_SHORT).show();
                                        changeCallback.onChange();
                                        handler.removeCallbacks(removeRunnable);
                                    }
                                }
                            }
                        }).setActionTextColor(ContextCompat.getColor(mActivity, R.color.color_text_undo));
                snackbar.show();
            }
        });
    }

    class RemoveResult implements Runnable {

        final ScanResult result;

        RemoveResult(final ScanResult result) {
            this.result = result;
        }

        @Override
        public void run() {
            String image = "code_" + result.getDate().getTime() + ".png";
            mActivity.deleteFile(image);
            boolean isRemoved = SQLiteHelper.getInstance().remove(result);
            if (isRemoved) {
                showToast(getResources().getString(R.string.removed_text));
            } else {
                showToast(getResources().getString(R.string.removed_fail_text));
            }
            hideSnackbar();
        }
    }

    public void hideSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Override
    public void onLongClickItem(int position) {
        if (mActionMode == null) {
            mActionMode = mActivity.startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    public void toggleSelection(int position) {
        if (mActionMode == null) {
            return;
        }
        mAdapter.toggleSelection(position);
        int selectedItemCount = mAdapter.getSelectedItemCount();

        if (selectedItemCount == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(String.valueOf(mAdapter.getItemSelectedCount()));
            mActionMode.invalidate();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void showToast(final String message) {
        if (mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
            mode.invalidate();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete: {
                    //mActivity.showToast("Delete");
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.resetToggleSelectedItem();
            mActionMode = null;
        }
    }

}
