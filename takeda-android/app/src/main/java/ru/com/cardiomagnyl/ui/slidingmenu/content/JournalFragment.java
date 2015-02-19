package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.timeline.Timeline;
import ru.com.cardiomagnyl.model.timeline.TimelineDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class JournalFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, null);
        initFragmentStart(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarMenuBellCabinet(viewGroupTopBar, true, true, true);
    }

    public void initFragmentStart(final View fragmentView) {
        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Token token = AppState.getInsnatce().getToken();
        getTimeline(fragmentView, token);
    }

    public void getTimeline(final View fragmentView, final Token token) {
        TimelineDao.getAll(
                token,
                new CallbackOne<List<Timeline>>() {
                    @Override
                    public void execute(List<Timeline> timeline) {
                        getPillDatabase(fragmentView, token, timeline);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, null, null);
                    }
                }
        );
    }

    public void getPillDatabase(final View fragmentView, final Token token, final List<Timeline> timeline) {
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        Map<String, Pill> pillsMap = Pill.listToMap(pillsList);
                        if (Timeline.checkPillsInTasks(timeline, pillsMap)) {
                            initFragmentFinish(fragmentView, timeline, pillsMap);
                        }else {
                            getPillHttp(fragmentView, token, timeline);
                        }
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        getPillHttp(fragmentView, token, timeline);
                    }
                },
                PillDao.Source.database
        );
    }

    public void getPillHttp(final View fragmentView, final Token token, final List<Timeline> timeline) {
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        Map<String, Pill> pillsMap = Pill.listToMap(pillsList);
                        if (Timeline.checkPillsInTasks(timeline, pillsMap)) {
                            initFragmentFinish(fragmentView, timeline, pillsMap);
                        }else {
                            initFragmentFinish(fragmentView, timeline, null);
                        }
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, timeline, null);
                    }
                },
                PillDao.Source.http
        );
    }

    private void initFragmentFinish(final View fragmentView, final List<Timeline> timeline, final Map<String, Pill> pillsMap) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (timeline == null || timeline.isEmpty() || pillsMap == null) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
            CustomDialogs.showAlertDialog(slidingMenuActivity, R.string.data_not_found);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
        }

    }

    private void initFragment(View view) {
        View item1 = view.findViewById(R.id.item1);
        View item2 = view.findViewById(R.id.item2);
        View item3 = view.findViewById(R.id.item3);

        TextView textViewDate1 = (TextView) item1.findViewById(R.id.textViewDate);
        TextView textViewDate2 = (TextView) item2.findViewById(R.id.textViewDate);
        TextView textViewDate3 = (TextView) item3.findViewById(R.id.textViewDate);

        textViewDate1.setText(Tools.getDayOfWeek(0));
        textViewDate2.setText(Tools.getDayOfWeek(-1));
        textViewDate3.setText(Tools.getDayOfWeek(-2));


//        View linearLayoutTime1 = view.findViewById(R.id.linearLayoutTime1);
//        View linearLayoutTime2 = view.findViewById(R.id.linearLayoutTime1);
//        View linearLayoutTime3 = view.findViewById(R.id.linearLayoutTime1);
//
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
//                    SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
//                    Fragment fragment = new AddPillsFragment();
//                    mainActivity.switchContent(fragment);
//                }
//            }
//        };
//
//        linearLayoutTime1.setOnClickListener(onClickListener);
//        linearLayoutTime2.setOnClickListener(onClickListener);
//        linearLayoutTime3.setOnClickListener(onClickListener);
    }
}
