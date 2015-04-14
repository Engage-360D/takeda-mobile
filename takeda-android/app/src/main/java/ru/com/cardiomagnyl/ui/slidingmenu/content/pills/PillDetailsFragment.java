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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.pill.PillFrequency;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.util.schedule.PillsScheduler;
import ru.com.cardiomagnyl.widget.CustomDialogs;
import ru.com.cardiomagnyl.widget.CustomOnDateSetListener;
import ru.com.cardiomagnyl.widget.CustomOnTimeSetListener;
import ru.com.cardiomagnyl.widget.CustomSpinnerAdapter;

public class PillDetailsFragment extends BaseItemFragment {
    private ViewGroup mViewGroupTopBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pill_details, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        mViewGroupTopBar = viewGroupTopBar;
    }

    private void initFragment(final View fragmentView) {
        Bundle bundle = this.getArguments();

        if (bundle == null) {
            setAddMode(fragmentView);
        } else {
            final Pill pill = bundle.getParcelable(Constants.PILL);
            if (pill == null) {
                ((SlidingMenuActivity) getActivity()).makeContentStepBack(true);
            } else {
                setDetailsMode(fragmentView, pill);
            }
        }
    }

    private void setAddMode(final View fragmentView) {
        initHeaderAdd(fragmentView);
        initButtonsAdd(fragmentView);
        initFields(fragmentView, null);
        setFieldsEnabled(fragmentView, true);
    }

    private void setEditMode(final View fragmentView, final Pill pill) {
        initHeaderEdit(fragmentView, pill);
        initButtonsEdit(fragmentView, pill);
        initFields(fragmentView, pill);
        setFieldsEnabled(fragmentView, true);
    }

    private void setDetailsMode(final View fragmentView, final Pill pill) {
        initHeaderDetails(fragmentView);
        initButtonsDetails(fragmentView, pill);
        initFields(fragmentView, pill);
        setFieldsEnabled(fragmentView, false);
    }

    private void tryToCreate(final View fragmentView) {
        // TODO: check fields here
        // TODO: show hint on empty fields
//                        if (validatePillFields(AddPillFragment.this.getView())) {
//                            Pill newPill = pickPillFields(AddPillFragment.this.getView());
//                            createPill(newPill);
//                        } else {
//                            Tools.showToast(getActivity(), R.string.complete_required_fields, Toast.LENGTH_SHORT);
//                        }

        Pill newPill = pickPillFields(PillDetailsFragment.this.getView());
        createPill(fragmentView, newPill);
    }

    private void tryToUpdate(final View fragmentView, final Pill pill) {
        // TODO: check fields here
        // TODO: show hint on empty fields
//                        if (validatePillFields(AddPillFragment.this.getView())) {
//                            Pill newPill = pickPillFields(AddPillFragment.this.getView());
//                            createPill(newPill);
//                        } else {
//                            Tools.showToast(getActivity(), R.string.complete_required_fields, Toast.LENGTH_SHORT);
//                        }

        Pill newPill = pickPillFields(PillDetailsFragment.this.getView());
        newPill.setId(pill.getId());
        updatePill(fragmentView, newPill);
    }

    private void initHeaderAdd(final View fragmentView) {
        TextView textViewHeader = (TextView) fragmentView.findViewById(R.id.textViewHeader);
        textViewHeader.setText(R.string.add);

        initTopBarDone(
                mViewGroupTopBar,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryToCreate(fragmentView);
                    }
                },
                false);
    }

    private void initButtonsAdd(final View fragmentView) {
        Button buttonEdit = (Button) fragmentView.findViewById(R.id.buttonEdit);
        Button buttonDelete = (Button) fragmentView.findViewById(R.id.buttonDelete);

        buttonEdit.setVisibility(View.GONE);
        buttonDelete.setVisibility(View.GONE);
    }

    private void initHeaderEdit(final View fragmentView, final Pill pill) {
        TextView textViewHeader = (TextView) fragmentView.findViewById(R.id.textViewHeader);
        textViewHeader.setText(R.string.detail);

        initTopBarDone(
                mViewGroupTopBar,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryToUpdate(fragmentView, pill);
                    }
                },
                false);
    }

    private void initButtonsEdit(final View fragmentView, final Pill pill) {
        Button buttonEdit = (Button) fragmentView.findViewById(R.id.buttonEdit);
        Button buttonDelete = (Button) fragmentView.findViewById(R.id.buttonDelete);

        buttonEdit.setVisibility(View.GONE);
        buttonDelete.setVisibility(View.VISIBLE);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToDeletePill(pill);
            }
        });
    }

    private void initHeaderDetails(final View fragmentView) {
        TextView textViewHeader = (TextView) fragmentView.findViewById(R.id.textViewHeader);
        textViewHeader.setText(R.string.detail);

        initTopBarMenuBellCabinet(mViewGroupTopBar, true, true, true);
    }

    private void initButtonsDetails(final View fragmentView, final Pill pill) {
        Button buttonEdit = (Button) fragmentView.findViewById(R.id.buttonEdit);
        Button buttonDelete = (Button) fragmentView.findViewById(R.id.buttonDelete);

        buttonEdit.setVisibility(View.VISIBLE);
        buttonDelete.setVisibility(View.GONE);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditMode(fragmentView, pill);
            }
        });
    }

    private void initFields(final View fragmentView, final Pill pill) {
        EditText editTextPillName = (EditText) fragmentView.findViewById(R.id.editTextPillName);
        EditText editTextPillNumber = (EditText) fragmentView.findViewById(R.id.editTextPillNumber);
        Spinner spinnerFrequency = (Spinner) fragmentView.findViewById(R.id.spinnerPillsFrequency);
        TextView textViewPillsHoursValue = (TextView) fragmentView.findViewById(R.id.textViewPillsHoursValue);
        TextView textViewStartDateValue = (TextView) fragmentView.findViewById(R.id.textViewStartDateValue);
        TextView textViewEndDateValue = (TextView) fragmentView.findViewById(R.id.textViewEndDateValue);

        initEditText(fragmentView, editTextPillName, pill == null ? null : pill.getName());
        initEditText(fragmentView, editTextPillNumber, pill == null ? null : String.valueOf(pill.getQuantity()));
        initSpinnerFrequency(fragmentView, spinnerFrequency, pill == null ? null : pill.getEnumFrequency());
        initTimePicker(fragmentView, textViewPillsHoursValue, pill == null ? null : Tools.calendarFromMediumTime(pill.getTime()));
        initDatePicker(fragmentView, textViewStartDateValue, pill == null ? null : Tools.calendarFromFullDate(pill.getSinceDate()));
        initDatePicker(fragmentView, textViewEndDateValue, pill == null ? null : Tools.calendarFromFullDate(pill.getTillDate()));
    }

    private void setFieldsEnabled(final View fragmentView, boolean enabled) {
        EditText editTextPillName = (EditText) fragmentView.findViewById(R.id.editTextPillName);
        EditText editTextPillNumber = (EditText) fragmentView.findViewById(R.id.editTextPillNumber);
        Spinner spinnerFrequency = (Spinner) fragmentView.findViewById(R.id.spinnerPillsFrequency);
        TextView textViewPillsHoursValue = (TextView) fragmentView.findViewById(R.id.textViewPillsHoursValue);
        TextView textViewStartDateValue = (TextView) fragmentView.findViewById(R.id.textViewStartDateValue);
        TextView textViewEndDateValue = (TextView) fragmentView.findViewById(R.id.textViewEndDateValue);

        editTextPillName.setEnabled(enabled);
        editTextPillNumber.setEnabled(enabled);
        spinnerFrequency.setEnabled(enabled);
        textViewPillsHoursValue.setEnabled(enabled);
        textViewStartDateValue.setEnabled(enabled);
        textViewEndDateValue.setEnabled(enabled);
    }

    private void initEditText(final View fragmentView, final EditText editText, final String text) {
        if (text != null) editText.setText(text);

        Object oldTextWatcher = editText.getTag();
        if (oldTextWatcher != null && oldTextWatcher instanceof TextWatcher) {
            editText.removeTextChangedListener((TextWatcher) oldTextWatcher);
        }

        TextWatcher newTextWatcher =
                new TextWatcher() {
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
                };

        editText.addTextChangedListener(newTextWatcher);
        editText.setTag(newTextWatcher);
    }

    private void initSpinnerFrequency(final View fragmentView, final Spinner spinnerFrequency, final PillFrequency pillFrequency) {
        List<BaseModelHelper> frequenciesList = PillFrequency.getFrequenciesList();

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(fragmentView.getContext(), R.layout.custom_spinner_item, R.layout.spinner_item_dropdown, frequenciesList);
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrequency.setAdapter(customSpinnerAdapter);

        if (pillFrequency == null) {
            spinnerFrequency.setSelection(customSpinnerAdapter.getCount() - 1, true);
        } else {
            int counter;
            for (counter = 0; counter < frequenciesList.size() - 1; ++counter) {
                PillFrequency currentPillFrequency = PillFrequency.getById((Integer) frequenciesList.get(counter).getId());
                if (currentPillFrequency.equals(pillFrequency))
                    break;
            }
            spinnerFrequency.setSelection(counter, true);
        }

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

    private void initTimePicker(final View fragmentView, final TextView textView, final Calendar calendar) {
        if (calendar != null) {
            textView.setTag(calendar);
            textView.setText(Tools.formatShortTime(calendar.getTime()));
        }

        final Calendar innerCalendar = calendar != null ? calendar : Tools.resetCalendar(Calendar.getInstance());
        final CustomOnTimeSetListener customOnTimeSetListener = new CustomOnTimeSetListener(innerCalendar);

        final TimePickerDialog timeDialog = new TimePickerDialog(
                fragmentView.getContext(),
                customOnTimeSetListener,
                innerCalendar.get(Calendar.HOUR_OF_DAY),
                innerCalendar.get(Calendar.MINUTE),
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

    private void initDatePicker(final View fragmentView, final TextView textView, final Calendar calendar) {
        if (calendar != null) {
            textView.setTag(calendar);
            textView.setText(Tools.formatShortDate(calendar.getTime()));
        }

        final Calendar innerCalendar = calendar != null ? calendar : Tools.resetCalendar(Calendar.getInstance());
        final CustomOnDateSetListener customOnDateSetListener = new CustomOnDateSetListener(innerCalendar);

        final DatePickerDialog dateDialog = new DatePickerDialog(
                fragmentView.getContext(),
                customOnDateSetListener,
                innerCalendar.get(Calendar.YEAR),
                innerCalendar.get(Calendar.MONTH),
                innerCalendar.get(Calendar.DAY_OF_MONTH));

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

    private void tryToDeletePill(final Pill pill) {
        String question = String.format(getActivity().getString(R.string.delete_pill), pill.getName());

        CustomDialogs.showConfirmationDialog(
                getActivity(),
                question,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePill(pill);
                    }
                }
        );
    }

    private void setEnabledButtonDone(boolean enabled) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        ViewGroup layoutHeader = slidingMenuActivity.getHeaderLayout();
        View view = layoutHeader.findViewById(R.id.textViewDone);
        if (view != null) view.setEnabled(enabled);
    }

    private boolean validatePillFields(final View fragmentView) {
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

    private static Pill pickPillFields(final View fragmentView) {
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

    private void createPill(final View fragmentView, final Pill newPill) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Token token = AppState.getInsnatce().getToken();
        PillDao.create(
                newPill,
                token,
                new CallbackOne<Pill>() {
                    @Override
                    public void execute(Pill pill) {
                        getPillDatabase(R.string.pill_created);
                        setDetailsMode(fragmentView, pill);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(slidingMenuActivity, R.string.error_occurred, Toast.LENGTH_LONG);
                    }
                }
        );
    }

    private void updatePill(final View fragmentView, final Pill pill) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Token token = AppState.getInsnatce().getToken();
        PillDao.update(
                pill,
                token,
                new CallbackOne<Pill>() {
                    @Override
                    public void execute(Pill newPill) {
                        getPillDatabase(R.string.pill_updated);
                        setDetailsMode(fragmentView, pill);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(slidingMenuActivity, R.string.error_occurred, Toast.LENGTH_LONG);
                    }
                }
        );
    }

    private void deletePill(final Pill pill) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Token token = AppState.getInsnatce().getToken();
        PillDao.delete(
                pill,
                token,
                new CallbackOne<Dummy>() {
                    @Override
                    public void execute(Dummy dummy) {
                        getPillDatabase(R.string.pill_deleted);
                        slidingMenuActivity.makeContentStepBack(true);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(slidingMenuActivity, R.string.error_occurred, Toast.LENGTH_LONG);
                    }
                }
        );
    }

    private void getPillDatabase(final int message) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();

        Token token = AppState.getInsnatce().getToken();
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        PillsScheduler.setAll(pillsList);

                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(slidingMenuActivity, message, Toast.LENGTH_LONG);
                        slidingMenuActivity.makeContentStepBack(true);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) { /* nothing to do*/ }
                },
                PillDao.Source.database
        );
    }

}
