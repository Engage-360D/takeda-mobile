package ru.com.cardiomagnyl.ui.slidingmenu.content.pills;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.pill.PillFrequency;
import ru.com.cardiomagnyl.model.task.TaskDao;
import ru.com.cardiomagnyl.model.test.TestPage;
import ru.com.cardiomagnyl.model.timeline.TimelineDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomOnDateSetListener;
import ru.com.cardiomagnyl.widget.CustomOnTimeSetListener;
import ru.com.cardiomagnyl.widget.CustomSpinnerAdapter;

public class PillDetailsFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pill, null);

        Bundle bundle = this.getArguments();
        Pill pill = bundle.getParcelable(Constants.PILL);

        initFragment(view, pill);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarMenuBellCabinet(viewGroupTopBar, true, true, true);
    }

    private void initFragment(final View fragmentView, final Pill pill) {
        EditText editTextPillName = (EditText) fragmentView.findViewById(R.id.editTextPillName);
        EditText editTextPillNumber = (EditText) fragmentView.findViewById(R.id.editTextPillNumber);
        Spinner spinnerFrequency = (Spinner) fragmentView.findViewById(R.id.spinnerPillsFrequency);
        TextView textViewPillsHoursValue = (TextView) fragmentView.findViewById(R.id.textViewPillsHoursValue);
        TextView textViewStartDateValue = (TextView) fragmentView.findViewById(R.id.textViewStartDateValue);
        TextView textViewEndDateValue = (TextView) fragmentView.findViewById(R.id.textViewEndDateValue);

        editTextPillName.setText(pill.getName());
        editTextPillName.setClickable(false);

        editTextPillNumber.setText(String.valueOf(pill.getQuantity()));
        editTextPillNumber.setClickable(false);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(
                fragmentView.getContext(),
                R.layout.custom_spinner_item,
                R.layout.spinner_item_dropdown,
                Arrays.asList((BaseModelHelper)pill.getEnumFrequency()));
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(customSpinnerAdapter);
        spinnerFrequency.setClickable(false);

        textViewPillsHoursValue.setText(pill.getTime());
        textViewPillsHoursValue.setClickable(false);

        textViewStartDateValue.setText(pill.getSinceDate());
        textViewStartDateValue.setClickable(false);

        textViewEndDateValue.setText(pill.getTillDate());
        textViewEndDateValue.setClickable(false);
    }

    protected void createPill(final Pill newPill, Token token) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        PillDao.create(
                newPill,
                token,
                new CallbackOne<Pill>() {
                    @Override
                    public void execute(Pill newPill) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(getActivity(), R.string.pill_created, Toast.LENGTH_LONG);

                        // must to clean tables "task" and "timeline"
                        TaskDao.clearTable();
                        TimelineDao.clearTable();

                        slidingMenuActivity.makeContentStepBack(true);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
                    }
                }
        );
    }

}
