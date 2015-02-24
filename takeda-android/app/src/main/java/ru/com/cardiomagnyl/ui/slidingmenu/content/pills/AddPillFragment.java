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

import java.util.Calendar;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.pill.PillFrequency;
import ru.com.cardiomagnyl.model.task.TaskDao;
import ru.com.cardiomagnyl.model.timeline.TimelineDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomOnDateSetListener;
import ru.com.cardiomagnyl.widget.CustomOnTimeSetListener;
import ru.com.cardiomagnyl.widget.CustomSpinnerAdapter;

public class AddPillFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pill_details, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarDone(viewGroupTopBar,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: check fields here
                        // TODO: show hint on empty fields
//                        if (validatePillFields(AddPillFragment.this.getView())) {
//                            Pill newPill = pickPillFields(AddPillFragment.this.getView());
//                            Token token = AppState.getInsnatce().getToken();
//                            createPill(newPill, token);
//                        } else {
//                            Tools.showToast(getActivity(), R.string.complete_required_fields, Toast.LENGTH_SHORT);
//                        }

                        Pill newPill = pickPillFields(AddPillFragment.this.getView());
                        Token token = AppState.getInsnatce().getToken();
                        createPill(newPill, token);
                    }
                }, false);
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

        Spinner spinnerFrequency = (Spinner) fragmentView.findViewById(R.id.spinnerPillsFrequency);
        spinnerFrequency.setAdapter(customSpinnerAdapter);
        spinnerFrequency.setSelection(customSpinnerAdapter.getCount() - 1, true);

        spinnerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setEnabledButtonDone(validatePillFields(fragmentView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setEnabledButtonDone(validatePillFields(fragmentView));
            }
        });
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
                setEnabledButtonDone(validatePillFields(fragmentView));
            }
        });
    }

    private void initTimePickerHelper(final View fragmentView, final TextView textView) {
        final Calendar calendar = Tools.resetCalendar(Calendar.getInstance());
        final CustomOnTimeSetListener customOnTimeSetListener = new CustomOnTimeSetListener(calendar);

        final TimePickerDialog timeDialog = new TimePickerDialog(
                fragmentView.getContext(),
                customOnTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
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

                            setEnabledButtonDone(validatePillFields(fragmentView));

                            datePickerDialogIsStarted = false;
                        }
                    });
                }

                return false;
            }
        });
    }

    private void initDatePickerHelper(final View fragmentView, final TextView textView) {
        final Calendar calendar = Tools.resetCalendar(Calendar.getInstance());
        final CustomOnDateSetListener customOnDateSetListener = new CustomOnDateSetListener(calendar);

        final DatePickerDialog dateDialog = new DatePickerDialog(
                fragmentView.getContext(),
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

                            setEnabledButtonDone(validatePillFields(fragmentView));

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

    public boolean validatePillFields(final View fragmentView) {
        EditText editTextPillName = (EditText) fragmentView.findViewById(R.id.editTextPillName);
        EditText editTextPillNumber = (EditText) fragmentView.findViewById(R.id.editTextPillNumber);
        Spinner spinnerFrequency = (Spinner) fragmentView.findViewById(R.id.spinnerPillsFrequency);
        TextView textViewPillsHoursValue = (TextView) fragmentView.findViewById(R.id.textViewPillsHoursValue);
        TextView textViewStartDateValue = (TextView) fragmentView.findViewById(R.id.textViewStartDateValue);
        TextView textViewEndDateValue = (TextView) fragmentView.findViewById(R.id.textViewEndDateValue);

        return editTextPillName.getText().length() > 0 &&
                editTextPillNumber.getText().length() > 0 &&
                spinnerFrequency.getTag() != null &&
                textViewPillsHoursValue.getTag() != null &&
                textViewStartDateValue.getTag() != null &&
                textViewEndDateValue.getTag() != null;
    }

    public static Pill pickPillFields(final View fragmentView) {
        Pill newPill = new Pill();

        try {
            EditText editTextPillName = (EditText) fragmentView.findViewById(R.id.editTextPillName);
            EditText editTextPillNumber = (EditText) fragmentView.findViewById(R.id.editTextPillNumber);
            Spinner spinnerFrequency = (Spinner) fragmentView.findViewById(R.id.spinnerPillsFrequency);
            TextView textViewPillsHoursValue = (TextView) fragmentView.findViewById(R.id.textViewPillsHoursValue);
            TextView textViewStartDateValue = (TextView) fragmentView.findViewById(R.id.textViewStartDateValue);
            TextView textViewEndDateValue = (TextView) fragmentView.findViewById(R.id.textViewEndDateValue);

            newPill.setName(editTextPillName.getText().toString());
            newPill.setQuantity(Integer.parseInt(editTextPillNumber.getText().toString()));
            newPill.setRepeat(PillFrequency.getById((Integer) spinnerFrequency.getTag()).name().toUpperCase());
            newPill.setTime(Tools.formatMediumTime(((Calendar) textViewPillsHoursValue.getTag()).getTime()));
            newPill.setSinceDate(Tools.formatFullDate(((Calendar) textViewStartDateValue.getTag()).getTime()));
            newPill.setTillDate(Tools.formatFullDate(((Calendar) textViewEndDateValue.getTag()).getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newPill;
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
