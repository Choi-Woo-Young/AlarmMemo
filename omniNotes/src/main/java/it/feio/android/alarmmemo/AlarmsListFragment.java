package it.feio.android.alarmmemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.feio.android.alarmmemo.alarms.Alarm;
import it.feio.android.alarmmemo.alarms.data.AlarmCursor;
import it.feio.android.alarmmemo.alarms.data.AlarmsListCursorLoader;
import it.feio.android.alarmmemo.alarms.data.AsyncAlarmsTableUpdateHandler;
import it.feio.android.alarmmemo.alarms.dialogs.TimePickerDialogController;
import it.feio.android.alarmmemo.alarms.list.RecyclerViewFragment;
import it.feio.android.alarmmemo.alarms.misc.AlarmController;
import it.feio.android.alarmmemo.alarms.ui.AlarmsCursorAdapter;
import it.feio.android.alarmmemo.alarms.ui.BaseAlarmViewHolder;
import it.feio.android.alarmmemo.dummy.DummyContent;
import it.feio.android.alarmmemo.utils.Constants;
import it.feio.android.alarmmemo.utils.alarms.FragmentTagUtils;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AlarmsListFragment extends RecyclerViewFragment<Alarm, BaseAlarmViewHolder, AlarmCursor,
        AlarmsCursorAdapter> implements BottomSheetTimePickerDialog.OnTimeSetListener {

    private static final String TAG = "AlarmsListFragment";
    private static final String KEY_EXPANDED_POSITION = "expanded_position";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.alarm_list)
    RecyclerView mAlarmRecyclerView;

    private TimePickerDialogController mTimePickerDialogController;
    private AsyncAlarmsTableUpdateHandler mAsyncUpdateHandler;
    private AlarmController mAlarmController;
    private View mSnackbarAnchor;

    private int mExpandedPosition = RecyclerView.NO_POSITION;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlarmsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AlarmsListFragment newInstance(int columnCount) {
        AlarmsListFragment fragment = new AlarmsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        //TimePickerDialogController(FragmentManager fragmentManager, Context context, BottomSheetTimePickerDialog.OnTimeSetListener listener)
        mTimePickerDialogController = new TimePickerDialogController( getFragmentManager(), getActivity(), this);
        mTimePickerDialogController.tryRestoreCallback(makeTimePickerDialogTag());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        mSnackbarAnchor = getActivity().findViewById(R.id.main_content);
        //wychoi
        if(mAlarmController == null){
            mAlarmController = new AlarmController(getActivity(), mSnackbarAnchor);
        }
        mAsyncUpdateHandler = new AsyncAlarmsTableUpdateHandler(getActivity(), mSnackbarAnchor, this, mAlarmController);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        // Show the pending Snackbar, if any, that was prepared for us
        // by another app component.
        //DelayedSnackbarHandler.makeAndShow(mSnackbarAnchor);
    }

    @Override
    public Loader<AlarmCursor> onCreateLoader(int id, Bundle args) {
        return new AlarmsListCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<AlarmCursor> loader, AlarmCursor data) {
        super.onLoadFinished(loader, data);
        Log.d(TAG, "onLoadFinished()");
        // TODO: If this was a content change due to an update, verify that
        // we scroll to the updated alarm if its sort order changes.

        // Does nothing If there is no expanded position.
        getAdapter().expand(mExpandedPosition);
        // We shouldn't continue to keep a reference to this, so clear it.
        mExpandedPosition = RecyclerView.NO_POSITION;
    }

    //[wychoi]2. MainActivity에서 알람 생성 Fab 클릭 시 실제 수행되는 메소드
    @Override
    public void onFabClick() {
        Log.d(TAG, "[wychoi] 2. AlarmsFragment > onFabClick");
        mTimePickerDialogController.show(0, 0, makeTimePickerDialogTag());
    }


    @Override
    protected AlarmsCursorAdapter onCreateAdapter() {
        Log.d(TAG, "[wychoi] 알람 목록에 연결한 adapter 생성 AlarmsFragment > onCreateAdapter");

        //wychoi
        if(mAlarmController == null){
            mAlarmController = new AlarmController(getActivity(), mSnackbarAnchor);
        }

        return new AlarmsCursorAdapter(this, mAlarmController);
    }

    @Override
    protected int emptyMessage() {
        return R.string.empty_alarms_container;
    }

    @Override
    protected int emptyIcon() {
        //return R.drawable.ic_alarm_96dp;
        return R.drawable.ic_alarm_black_48dp;
    }

    @Override
    public void onListItemClick(Alarm item, int position) {
        boolean expanded = getAdapter().expand(position);
        if (!expanded) {
            getAdapter().collapse(position);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // TODO: Just like with TimersCursorAdapter, we could pass in the mAsyncUpdateHandler
    // to the AlarmsCursorAdapter and call these on the save and delete button click bindings.

    @Override
    public void onListItemDeleted(final Alarm item) {
        // The corresponding VH will be automatically removed from view following
        // the requery, so we don't have to do anything to it.
        mAsyncUpdateHandler.asyncDelete(item);
    }

    @Override
    public void onListItemUpdate(Alarm item, int position) {
        // Once we update the relevant row in the db table, the VH will still
        // be in view. While the requery will probably update the values displayed
        // by the VH, the VH remains in its expanded state from before we were
        // called. Tell the adapter reset its expanded position.
        mAsyncUpdateHandler.asyncUpdate(item.getId(), item);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onScrolledToStableId(long id, int position) {
        boolean expanded = getAdapter().expand(position);
        if (!expanded) {
            // Otherwise, it was due to an item update. The VH is expanded
            // at this point, so reset it.
            getAdapter().collapse(position);
        }
    }

    //[wychoi] 키패드로 알람 시각 입력 후 등록 버튼 클릭 시
    @Override
    public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {
        // When we request the Builder, default values are provided for us,
        // which is why we don't have to set the ringtone, label, etc.
        Log.d(Constants.TAG, "[wychoi] 4.AlarmsListFragment > onTimeSet");
        Alarm alarm = Alarm.builder()
                .hour(hourOfDay)
                .minutes(minute)
                .build();
        alarm.setEnabled(true);
        mAsyncUpdateHandler.asyncInsert(alarm);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
         * From Fragment#onSaveInstanceState():
         *   - This is called "at any time before onDestroy()".
         *   - "This corresponds to Activity.onSaveInstanceState(Bundle) and most of the discussion
         *     there applies here as well".
         * From Activity#onSaveInstanceState():
         *   - "If called, this method will occur before {@link #onStop}
         *     [which follows onPause() in the lifecycle].  There are
         *     no guarantees about whether it will occur before or after {@link #onPause}."
         *
         * isResumed() is true "for the duration of onResume() and onPause()".
         * From the results of a few trials, this never seemed to call through, so i'm assuming
         * isResumed() returned false every time.
         */
        if (/*isResumed() && */getAdapter() != null) {
            // Normally when we scroll far enough away from this Fragment, *its view* will be
            // destroyed, i.e. the maximum point in its lifecycle is onDestroyView(). However,
            // if the configuration changes, onDestroy() is called through, and then this Fragment
            // and all of its members will be destroyed. This is not
            // a problem if the page in which the configuration changed is this page, because
            // the Fragment will be recreated from onCreate() to onResume(), and any
            // member initialization between those points occurs as usual.
            //
            // However, when the page in which the configuration changed
            // is far enough away from this Fragment, there IS a problem. The Fragment
            // *at that page* is recreated, but this Fragment will NOT be; the ViewPager's
            // adapter will not reinstantiate this Fragment because it exceeds the
            // offscreen page limit relative to the initial page in the new configuration.
            //
            // As such, we should only save state if this Fragment's members (i.e. its RecyclerView.Adapter)
            // are not destroyed
            // because that indicates the Fragment is both registered in the adapter AND is within the offscreen
            // page limit, so its members have been initialized (recall that a Fragment in a ViewPager
            // does not actually need to be visible to the user for onCreateView() to onResume() to
            // be called through).
            outState.putInt(KEY_EXPANDED_POSITION, getAdapter().getExpandedPosition());
        }
    }

    //alarm
    private static String makeTimePickerDialogTag() {
        return FragmentTagUtils.makeTag(AlarmsListFragment.class, R.id.fab);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.DummyItem item);
    }


    //[wychoi]알람 등록 fab 클릭
    @OnClick(R.id.fab_alarms_register)
    public void fabAlarmRegClick(){
        Log.d(Constants.TAG, "fabAlarmRegClick");
        mTimePickerDialogController.show(0, 0, makeTimePickerDialogTag());
    }
}
