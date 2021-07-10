package android.vn.leo.qrscan.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
import android.vn.leo.qrscan.utils.CommonMethod;
import android.vn.leo.qrscan.utils.Const;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setTag("recycler_view");
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
        toggleSelection(false, position);
    }

    @Override
    public void onItemRemoveConfirm(final int position, final ScanResult item, final HistoryAdapter.OnChangeCallback changeCallback) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final List<ScanResult> deletes = new ArrayList<>();
                deletes.add(item);
                CommonMethod.putDeleteList(deletes);
                toggleSelection(false, position);
                mAdapter.notifyItemRemoved(position);
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

    @Override
    public void onListItemRemoveConfirm(final HistoryAdapter.OnChangeCallback onChangeCallback) {
        final Handler handler = new Handler();
        final int size = ResultManager.getInstance().currentSizeOfRemovedList();
        final List<ScanResult> deletes = new ArrayList<>(ResultManager.getInstance().getRemovedList());
        CommonMethod.putDeleteList(deletes);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearListItems();
                final Runnable listRemove = new ListRemoveResult();
                handler.postDelayed(listRemove, Const.TIME_TO_REMOVE_RESULT);
                String message = getResources().getString(R.string.toast_message_removed_list);
                message = message.replace("xxx", String.valueOf(size));
                snackbar = Snackbar.make(mActivity.findViewById(R.id.view_pager), message, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.undo_text), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CommonMethod.clearDeleteList();
                                int itemCounts = restoreItems();
                                ResultManager.getInstance().releaseRemoveList();
                                if (itemCounts > 0) {
                                    String rmvMessage = getResources().getString(R.string.toast_message_restored_list);
                                    rmvMessage = rmvMessage.replace("xxx", String.valueOf(itemCounts));
                                    Toast.makeText(mActivity, rmvMessage, Toast.LENGTH_SHORT).show();
                                    onChangeCallback.onChange();
                                    handler.removeCallbacks(listRemove);
                                }
                            }
                        }).setActionTextColor(ContextCompat.getColor(mActivity, R.color.color_text_undo));
                snackbar.show();
            }
        });
    }

    public void clearListItems() {
        Set<Integer> keys = ResultManager.getInstance().getPositionRemovedList();
        for (int key : keys) {
            ScanResult result = ResultManager.getInstance().getRemovedItem(key);
            if (result != null) {
                ResultManager.getInstance().remove(result);
            }
        }
        mAdapter.notifyDataSetChanged();
        releaseActionMode();
    }

    public int restoreItems() {
        Set<Integer> keys = ResultManager.getInstance().getPositionRemovedList();
        int count = 0;
        for (int i : keys) {
            ScanResult result = ResultManager.getInstance().getRemovedItem(i);
            if (result != null) {
                ResultManager.getInstance().add(i, result);
            }
            count++;
        }
        return count;
    }

    class RemoveResult implements Runnable {

        final ScanResult result;

        RemoveResult(final ScanResult result) {
            this.result = result;
        }

        @Override
        public void run() {
            hideSnackbar();
            String image = "code_" + result.getDate().getTime() + ".png";
            mActivity.deleteFile(image);
            boolean isRemoved = SQLiteHelper.getInstance().remove(result);
            if (isRemoved) {
                showToast(getResources().getString(R.string.removed_text));
            } else {
                showToast(getResources().getString(R.string.removed_fail_text));
            }
        }
    }

    class ListRemoveResult implements Runnable {

        final List<ScanResult> results = new ArrayList<>();

        ListRemoveResult() {
            this.results.addAll(ResultManager.getInstance().getRemovedList());
        }

        @Override
        public void run() {
            hideSnackbar();
            boolean isAllRemoved = true;
            int count = 0;
            for (ScanResult result : results) {
                String image = "code_" + result.getDate().getTime() + ".png";
                mActivity.deleteFile(image);
                boolean isRemoved = SQLiteHelper.getInstance().remove(result);
                if (isRemoved) {
                    count++;
                } else {
                    isAllRemoved = false;
                    break;
                }
            }
            ResultManager.getInstance().releaseRemoveList();
            CommonMethod.clearDeleteList();
            if (isAllRemoved) {
                String s = getResources().getString(R.string.removed_text) + " " + count;
                showToast(s);
            } else {
                if (count > 0) {
                    String s = getResources().getString(R.string.removed_text) + " " + count;
                    showToast(s);
                } else {
                    showToast(getResources().getString(R.string.removed_fail_text));
                }
            }
        }
    }

    public void hideSnackbar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    public void releaseActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    @Override
    public void onLongClickItem(int position) {
        if (mActionMode == null) {
            mActionMode = mActivity.startSupportActionMode(actionModeCallback);
        }
        toggleSelection(true, position);
    }

    public void toggleSelection(boolean isLongClick, int position) {
        if (mActionMode == null) {
            return;
        }

        if (isLongClick) {
            mAdapter.toggleLongClickSelection(position);
        } else {
            mAdapter.toggleSelection(position);
        }

        int selectedItemCount = mAdapter.getSelectedItemCount();

        if (selectedItemCount == 0) {
            mActionMode.finish();
            mActionMode = null;
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

    public void onShareList() {
        List<ScanResult> selected = new ArrayList<>(ResultManager.getInstance().getRemovedList());
        List<String> strings = new ArrayList<>();

        for (ScanResult result : selected) {
            strings.add(result.getResult());
        }

        if (getContext() != null) {
            CommonMethod.share(getContext(), strings);
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
                    mAdapter.onListDeleted();
                    return true;
                }
                case R.id.action_share: {
                    onShareList();
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
