package ru.com.cardiomagnyl.ui.slidingmenu.content.pills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class PillsFragment extends BaseItemFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pills, null);
        initFragmentStart(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellAddPill(viewGroupTopBar, true, true);
    }

    public void initFragmentStart(final View fragmentView) {
        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Token token = AppState.getInsnatce().getToken();
        getPillDatabase(fragmentView, token);
    }

    public void getPillDatabase(final View fragmentView, final Token token) {
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        initFragmentFinish(fragmentView, pillsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        getPillHttp(fragmentView, token);
                    }
                },
                PillDao.Source.database
        );
    }

    public void getPillHttp(final View fragmentView, final Token token) {
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        initFragmentFinish(fragmentView, pillsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, null);
                    }
                },
                PillDao.Source.http
        );
    }

    private void initFragmentFinish(final View fragmentView, final List<Pill> pillsList) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (pillsList == null || pillsList.isEmpty()) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
            CustomDialogs.showAlertDialog(slidingMenuActivity, R.string.data_not_found);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishHelper(fragmentView, pillsList);
        }
    }

    private void initFragmentFinishHelper(final View fragmentView, final List<Pill> pillsList) {
        final ListView listViewPills = (ListView) fragmentView.findViewById(R.id.listViewPills);

        final PillsAdapter pillsAdapter = new PillsAdapter(PillsFragment.this.getActivity(), pillsList);
        listViewPills.setAdapter(pillsAdapter);

        listViewPills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPillDetails(pillsList.get(position));
            }
        });
    }

    private void showPillDetails(final Pill pill) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.PILL, pill);

        PillDetailsFragment pillDetailsFragment = new PillDetailsFragment();
        pillDetailsFragment.setArguments(bundle);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.putContentOnTop(pillDetailsFragment, true);
    }

}
