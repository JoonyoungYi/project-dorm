package kr.clude.yearnning.smartdorm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.timroes.android.listview.EnhancedListView;


public class MainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     *
     */
    private ScheduleManager mScheduleManager;

    /**
     *
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mErrorView;
    private TextView mErrorTv;
    private ProgressBar mErrorPb;
    private EnhancedListView mLv;
    private LvAdapter mLvAdapter;


    /**
     *
     */
    MainListApiTask mTashuApiTask = null;


    /**
     *
     */
    private MainActivity mMainActivity = null;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    /**
     * ListView Apdater Setting
     */

    private class LvAdapter extends ArrayAdapter<MainListItem> {

        private static final String TAG = "MainFragmentLvAdapter";
        public ArrayList<MainListItem> mMainListItems;
        private ViewHolder viewHolder = null;
        private int textViewResourceId;
        private Activity context;

        public LvAdapter(Activity context, int textViewResourceId,
                         ArrayList<MainListItem> mMainListItems) {
            super(context, textViewResourceId, mMainListItems);

            this.context = context;
            this.textViewResourceId = textViewResourceId;
            this.mMainListItems = mMainListItems;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getCount() {
            return mMainListItems.size();
        }

        @Override
        public MainListItem getItem(int position) {
            return mMainListItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void removeItem(int position) {
            mMainListItems.remove(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

			/*
             * UI Initiailizing : View Holder
			 */

           if (convertView == null) {
                convertView = context.getLayoutInflater()
                        .inflate(textViewResourceId, null);

                viewHolder = new ViewHolder();

                viewHolder.cardViewCode1 = convertView.findViewById(R.id.card_view_code_1);
                viewHolder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
                viewHolder.alarmBtn = (ImageButton) convertView.findViewById(R.id.alarm_btn);
                viewHolder.emptyView = convertView.findViewById(R.id.empty_view);
                viewHolder.occupiedTv = (TextView) convertView.findViewById(R.id.occupied_tv);
                viewHolder.occupiedView = convertView.findViewById(R.id.occupied_view);
                viewHolder.emptyTv = (TextView) convertView.findViewById(R.id.empty_tv);
                viewHolder.whenTv = (TextView) convertView.findViewById(R.id.when_tv);
                viewHolder.recommendTv = (TextView) convertView.findViewById(R.id.recommend_tv);

                viewHolder.cardViewCode2 = convertView.findViewById(R.id.card_view_code_2);
                viewHolder.messageTv = (TextView) convertView.findViewById(R.id.message_tv);
                viewHolder.mIconIv = (ImageView) convertView.findViewById(R.id.icon_iv);
                viewHolder.confirmBtn = convertView.findViewById(R.id.confirm_btn);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final MainListItem mainListItem = this.getItem(position);

			/*
             * Data Import and export
			 */

            if (mainListItem.code == 1) {

                viewHolder.cardViewCode1.setVisibility(View.VISIBLE);
                viewHolder.cardViewCode2.setVisibility(View.GONE);

                viewHolder.titleTv.setText(mainListItem.title);

                if (mainListItem.occupied_cnt != 0) {
                    viewHolder.occupiedTv.setText(mainListItem.occupied_cnt + "");
                    viewHolder.occupiedView.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.occupiedView.setVisibility(View.GONE);
                }

                if (mainListItem.empty_cnt != 0) {
                    viewHolder.emptyTv.setText(mainListItem.empty_cnt + "");
                    viewHolder.emptyView.setVisibility(View.VISIBLE);
                    viewHolder.alarmBtn.setVisibility(View.GONE);
                } else {
                    viewHolder.emptyView.setVisibility(View.GONE);
                    viewHolder.alarmBtn.setVisibility(View.VISIBLE);
                }

                viewHolder.whenTv.setText(mainListItem.when_str);
                viewHolder.recommendTv.setText(mainListItem.recommend_str);

                final ImageButton alarmBtn = viewHolder.alarmBtn;

                alarmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleAlarm(alarmBtn, position);

                    }
                });

            } else {
                viewHolder.cardViewCode1.setVisibility(View.GONE);
                viewHolder.cardViewCode2.setVisibility(View.VISIBLE);

                viewHolder.mIconIv.setImageResource(mainListItem.icon_resource_id);
                viewHolder.messageTv.setText(mainListItem.message);

                viewHolder.confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mainListItem.message.equals("룸메이트가 방에 있습니다.\n같이 식사할까 물어보시는 건 어떤가요?")) {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "밥?");
                            sendIntent.setType("text/plain");
                            sendIntent.setPackage("com.kakao.talk");
                            startActivity(sendIntent);

                        }
                        mLv.delete(position);


                    }
                });
            }


            return convertView;
        }


        public void toggleAlarm(ImageButton alarmBtn, int position){
            MainListItem mainListItem = this.getItem(position);

            if (mainListItem.isAlarmed) {
                makeAlarmOff(mainListItem.type);
                alarmBtn.setImageResource(R.drawable.ic_notification_none);
                Toast.makeText(getActivity(), "알람이 해제되었습니다", Toast.LENGTH_SHORT).show();
                mainListItem.isAlarmed = false;
            } else {
                makeAlarmOn(mainListItem.type, mainListItem.title, mainListItem.when);
                alarmBtn.setImageResource(R.drawable.ic_notification);
                Toast.makeText(getActivity(), "알람이 설정되었습니다", Toast.LENGTH_SHORT).show();
                mainListItem.isAlarmed = true;
            }
        }

        @Override
        public boolean isEnabled(int position) {

            return false;
        }

        private class ViewHolder {

            public View cardViewCode1;
            public TextView titleTv;
            public ImageButton alarmBtn;
            public TextView occupiedTv;
            public View occupiedView;
            public TextView emptyTv;
            public View emptyView;
            public TextView whenTv;
            public TextView recommendTv;

            public View cardViewCode2;
            public View confirmBtn;
            public ImageView mIconIv;
            public TextView messageTv;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);

        mMainActivity = (MainActivity) getActivity();

        mScheduleManager = new ScheduleManager();
        /**
         *
         */
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl);
        mLv = (EnhancedListView) rootView.findViewById(R.id.lv);
        mErrorView = rootView.findViewById(R.id.error_view);
        mErrorTv = (TextView) mErrorView.findViewById(R.id.error_tv);
        mErrorPb = (ProgressBar) mErrorView.findViewById(R.id.error_pb);

        /**
         *
         */

        mLv.setDismissCallback(new EnhancedListView.OnDismissCallback() {

            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

                // Store the item for later undo
                final MainListItem item = mLvAdapter.getItem(position);
                // Remove the item from the adapter
                mLvAdapter.removeItem(position);
                // return an Undoable

                mLvAdapter.notifyDataSetChanged();

                return new EnhancedListView.Undoable() {
                    // Reinsert the item to the adapter
                    @Override
                    public void undo() {
                        mLvAdapter.mMainListItems.add(position, item);
                        mLvAdapter.notifyDataSetChanged();
                    }

                    // Return a string for your item
                    @Override
                    public String getTitle() {
                        return "카드가 제거되었습니다";
                    }

                    // Delete item completely from your persistent storage
                    @Override
                    public void discard() {

                    }
                };

            }

        });

        mLv.enableSwipeToDismiss();


        /**
         *
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRefresh();

            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(R.color.actionbar);

        /*
         * ListView Setting
		 */
        mLv.addHeaderView(inflater.inflate(R.layout.main_fragment_lv_header, null));
        mLv.addFooterView(new View(getActivity()));
        ArrayList<MainListItem> mainListItems = new ArrayList<>();
        mLvAdapter = new LvAdapter(getActivity(), R.layout.main_fragment_lv,
                mainListItems);
        mLv.setAdapter(mLvAdapter);

        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            mSwipeRefreshLayout.setProgressViewOffset(false, actionBarHeight, actionBarHeight * 3 / 2);
        }


        /**
         *
         */
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    mMainActivity.showActionbar();
                } else {
                    mMainActivity.hideActionbar();
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });


        /**
         *
         */
        requestRefresh();

        return rootView;
    }

    public void requestRefresh() {
        if (mTashuApiTask == null) {
            mTashuApiTask = new MainListApiTask();
            mTashuApiTask.execute();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    /**
     *
     */
    public class MainListApiTask extends AsyncTask<Void, Void, ArrayList<MainListItem>> {

        @Override
        protected void onPreExecute() {

            if (mLvAdapter.mMainListItems.size() == 0) {
                showErrorView(true, "");
            } else {
                mSwipeRefreshLayout.setRefreshing(true);
            }

        }

        /**
         * @param params
         * @return
         */
        @Override
        protected ArrayList<MainListItem> doInBackground(Void... params) {

            ArrayList<MainListItem> tashuStations = new ArrayList<MainListItem>();

            try {

                Thread.sleep(1000);
                MainListApi tashuApi = new MainListApi();
                tashuStations = tashuApi.getResult();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return tashuStations;
        }

        @Override
        protected void onPostExecute(ArrayList<MainListItem> tashuStations) {


            if (tashuStations != null) {
                Log.d("MainFragment", tashuStations.size() + " <- size ");
                mLvAdapter.mMainListItems.clear();
                mLvAdapter.mMainListItems.addAll(tashuStations);
                mLvAdapter.notifyDataSetChanged();
                showErrorView(false, "");

            } else {
                showErrorView(true, "오류가 발생해 \"돔\"정보를 불러오지 못했습니다");
            }

            mTashuApiTask = null;
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mTashuApiTask = null;
        }
    }


    @Override
    public void onDestroy() {
        if (mTashuApiTask != null) {
            mTashuApiTask.cancel(true);
        }

        super.onDestroy();
    }


    /**
     * @param show
     */
    private void showErrorView(final boolean show, String msg) {

        if (show) {
            mLv.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
            mErrorTv.setText(msg);

            if (msg.equals("")) {
                mErrorPb.setVisibility(View.VISIBLE);
            } else {
                mErrorPb.setVisibility(View.GONE);
            }

        } else {
            mLv.setVisibility(View.VISIBLE);
            mErrorPb.setVisibility(View.VISIBLE);
            mErrorView.setVisibility(View.GONE);
        }
    }

    public void makeAlarmOn(int id, String title, int time_offset) {
        Context context = getActivity().getApplicationContext();
        if (mScheduleManager != null) {
            mScheduleManager.SetAlarm(context, id, title, time_offset);
        }
    }

    public void makeAlarmOff(int id) {
        Context context = getActivity().getApplicationContext();
        if (mScheduleManager != null) {
            mScheduleManager.CancelAlarm(context, id);
        }
    }

}
