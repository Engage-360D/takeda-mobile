package ru.com.cardiomagnyl.ui.slidingmenu.content.institution;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.institution.Institution;
import ru.com.cardiomagnyl.model.institution.InstitutionDao;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;

public class InstitutionDetailsFragment extends BaseItemFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_institution_details, null);
        initFragmentStart(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    private void initFragmentStart(final View fragmentView) {
        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        getInstitutionById(fragmentView);
    }

    private void getInstitutionById(final View fragmentView) {
        Bundle bundle = this.getArguments();
        String institutionId = bundle.getString(Constants.INSTITUTION_ID);
        InstitutionDao.getById(
                institutionId,
                new CallbackOne<Institution>() {
                    @Override
                    public void execute(Institution institution) {
                        initFragmentFinish(fragmentView, institution, null);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, null, responseError);
                    }
                }
        );
    }

    private void initFragmentFinish(final View fragmentView, final Institution institution, final Response responseError) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (institution == null) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishHelper(fragmentView, institution);
        }
    }

    private void initFragmentFinishHelper(final View fragmentView, final Institution institution) {
        TextView textViewInstitutionName = (TextView) fragmentView.findViewById(R.id.textViewInstitutionName);
        TextView textViewInstitutionAddress = (TextView) fragmentView.findViewById(R.id.textViewInstitutionAddress);
        TextView textViewBestInstitution = (TextView) fragmentView.findViewById(R.id.textViewBestInstitution);
        LinearLayout linearLayoutContentHolder = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutContentHolder);

        textViewInstitutionName.setText(institution.getName());
        textViewInstitutionAddress.setText(institution.getAddress());
        textViewBestInstitution.setVisibility(institution.getPriority() == 1 ? View.VISIBLE : View.GONE);

        Context context = fragmentView.getContext();

        linearLayoutContentHolder.addView(createInstitutionItem(fragmentView,
                context.getString(R.string.phone), context.getString(R.string.no_data)));
        linearLayoutContentHolder.addView(createInstitutionItem(fragmentView,
                context.getString(R.string.registry), context.getString(R.string.no_data)));
        linearLayoutContentHolder.addView(createInstitutionItem(fragmentView,
                context.getString(R.string.cardiovascular_department), context.getString(R.string.no_data)));
        linearLayoutContentHolder.addView(createInstitutionItem(fragmentView,
                context.getString(R.string.expert), context.getString(R.string.no_data)));
        linearLayoutContentHolder.addView(createInstitutionItem(fragmentView,
                context.getString(R.string.opening_hours), context.getString(R.string.no_data)));
    }

    private LinearLayout createInstitutionItem(final View fragmentView, final String name, final String value) {
        LinearLayout institutionItem = (LinearLayout) fragmentView.inflate(fragmentView.getContext(), R.layout.layout_institution_item, null);
        TextView textViewName = (TextView) institutionItem.findViewById(R.id.textViewName);
        TextView textViewValue = (TextView) institutionItem.findViewById(R.id.textViewValue);

        textViewName.setText(name);
        textViewValue.setText(value);

        return institutionItem;
    }

}
