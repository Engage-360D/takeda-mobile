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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;
import ru.com.cardiomagnyl.model.pill.PillFrequency;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomOnDateSetListener;
import ru.com.cardiomagnyl.widget.CustomOnTimeSetListener;
import ru.com.cardiomagnyl.widget.CustomSpinnerAdapter;

public class AddPillFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pill, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarDone(viewGroupTopBar, null);
    }

    private void initFragment(final View fragmentView) {
        initEditText(fragmentView);
        initSpinnerFrequency(fragmentView);
        initTimePicker(fragmentView);
        initDatePicker(fragmentView);

    }

    private void initEditText(final View fragmentView) {
        EditText editTextPillName = (EditText) fragmentView.findViewById(R.id.editTextPillName);
        EditText editTextPillNumber = (EditText) fragmentView.findViewById(R.id.editTextPillNumber);

        initEditTextHelper(fragmentView, editTextPillName);
        initEditTextHelper(fragmentView, editTextPillNumber);
    }

    private void initTimePicker(final View fragmentView) {
        TextView textViewPillsHoursValue = (TextView) fragmentView.findViewById(R.id.textViewPillsHoursValue);

        initTimePickerHelper(fragmentView, textViewPillsHoursValue);
    }

    private void initDatePicker(final View fragmentView) {
        TextView textViewStartDateValue = (TextView) fragmentView.findViewById(R.id.textViewStartDateValue);
        TextView textViewEndDateValue = (TextView) fragmentView.findViewById(R.id.textViewEndDateValue);

        initDatePickerHelper(fragmentView, textViewStartDateValue);
        initDatePickerHelper(fragmentView, textViewEndDateValue);
    }

    private void initSpinnerFrequency(final View fragmentView) {
        List<BaseModelHelper> frequenciesList = PillFrequency.getFrequenciesList();

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(fragmentView.getContext(), R.layout.custom_spinner_item, R.layout.spinner_item_dropdown, frequenciesList);
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerFrequency = (Spinner) fragmentView.findViewById(R.id.spinnerPillsFrequency);
        spinnerFrequency.setAdapter(customSpinnerAdapter);
        spinnerFrequency.setSelection(customSpinnerAdapter.getCount() - 1, true);
    }

    private void initEditTextHelper(final View fragmentView, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setEnabledButtonDone(validateNewPillFields(fragmentView));
            }
        });
    }

    private static void initTimePickerHelper(final View parentView, final TextView textView) {
        final Calendar calendar = Tools.resetCalendar(Calendar.getInstance());
        final CustomOnTimeSetListener customOnTimeSetListener = new CustomOnTimeSetListener(calendar);

        final TimePickerDialog timeDialog = new TimePickerDialog(
                parentView.getContext(),
                customOnTimeSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                true
        );

        textView.setOnTouchListener(new View.OnTouchListener() {
            private boolean datePickerDialogIsStarted = false;

            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                if (!datePickerDialogIsStarted) {
                    datePickerDialogIsStarted = true;

                    Calendar calendar = (Calendar) textView.getTag();
                    if (calendar != null) {
                        timeDialog.updateTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    }

                    timeDialog.show();

                    timeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            textView.setTag(customOnTimeSetListener.getCalendar());
                            textView.setText(Tools.formatShortTime(customOnTimeSetListener.getCalendar().getTime()));

                            datePickerDialogIsStarted = false;
                        }
                    });
                }

                return false;
            }
        });
    }

    private static void initDatePickerHelper(final View parentView, final TextView textView) {
        final Calendar calendar = Tools.resetCalendar(Calendar.getInstance());
        final CustomOnDateSetListener customOnDateSetListener = new CustomOnDateSetListener(calendar);

        final DatePickerDialog dateDialog = new DatePickerDialog(
                parentView.getContext(),
                customOnDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        textView.setOnTouchListener(new View.OnTouchListener() {
            private boolean datePickerDialogIsStarted = false;

            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                if (!datePickerDialogIsStarted) {
                    datePickerDialogIsStarted = true;

                    Calendar calendar = (Calendar) textView.getTag();
                    if (calendar != null) {
                        dateDialog.getDatePicker().updateDate(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));
                    }

                    dateDialog.show();

                    dateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            textView.setTag(customOnDateSetListener.getCalendar());
                            textView.setText(Tools.formatShortDate(customOnDateSetListener.getCalendar().getTime()));

                            datePickerDialogIsStarted = false;
                        }
                    });
                }

                return false;
            }
        });
    }

    private void setEnabledButtonDone(boolean enabled) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        ViewGroup layoutHeader = slidingMenuActivity.getHeaderLayout();
        View view = layoutHeader.findViewById(R.id.textViewDone);
        view.setEnabled(enabled);
    }

    private boolean validateNewPillFields(final View fragmentView) {
        // TODO: implement body
        return false;
    }

}
