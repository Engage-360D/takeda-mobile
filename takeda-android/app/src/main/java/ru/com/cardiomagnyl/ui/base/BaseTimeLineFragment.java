package ru.com.cardiomagnyl.ui.base;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppSharedPreferences;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants.ArterialPressure;
import ru.com.cardiomagnyl.application.Constants.CholesterolLevel;
import ru.com.cardiomagnyl.application.Constants.PhysicalActivity;
import ru.com.cardiomagnyl.application.Constants.Weight;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.task.TaskDao;
import ru.com.cardiomagnyl.model.timeline.Timeline;
import ru.com.cardiomagnyl.model.timeline.TimelineDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.user.Isr;
import ru.com.cardiomagnyl.model.user.UserDao;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.Callback;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.ProfileHelper;
import ru.com.cardiomagnyl.util.TimelineComparator;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.util.schedule.PillsScheduler;
import ru.com.cardiomagnyl.widget.CustomDialogLayout;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public abstract class BaseTimeLineFragment extends BaseItemFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    protected final List<Timeline> mFullTimelineList = new ArrayList<>();
    protected final List<Timeline> mNewTimelineList = new ArrayList<>();
    protected final List<Timeline> mFilledTimelineList = new ArrayList<>();
    protected final List<Timeline> mCurrentTimelineList = new ArrayList<>();
    protected final Map<String, Pill> mPillsMap = new HashMap<>();
    protected ViewGroup mViewGroupTopBar;

    protected abstract void initFragmentFinishHelper(final View fragmentView);

    protected abstract void onTaskUpdated(final Task updatedTask);

    protected abstract void onTimelineUpdated(final View fragmentVie);

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        mViewGroupTopBar = viewGroupTopBar;
    }

    @Override
    public void onRefresh() {
        View fragmentView = getView();

        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        TimelineDao.clearTable();
        getTimeline(fragmentView);

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipeRefreshLayoutTimeline);
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        // TODO: replace mPillsMap
        if (tag != null || tag instanceof Task)
            tryShowTaskDialog((Task) tag, mPillsMap);
    }

    protected void initFragmentStart(final View fragmentView) {
        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        getTimeline(fragmentView);
    }

    private void getTimeline(final View fragmentView) {
        Token token = AppState.getInsnatce().getToken();
        TimelineDao.getAll(
                token,
                new CallbackOne<List<Timeline>>() {
                    @Override
                    public void execute(List<Timeline> timeline) {
                        Collections.sort(timeline, new TimelineComparator());
                        AppState.getInsnatce().setTimelineEvents(Timeline.calculateMissedTasks(timeline));
                        getPillDatabase(fragmentView, timeline);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, null, null, null);
                    }
                }
        );
    }

    private void getPillDatabase(final View fragmentView, final List<Timeline> timeline) {
        Token token = AppState.getInsnatce().getToken();
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        PillsScheduler.setAll(pillsList);

                        Map<String, Pill> pillsMap = Pill.listToMap(pillsList);
                        getIsr(fragmentView, timeline, pillsMap);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        getIsr(fragmentView, timeline, null);
                    }
                },
                PillDao.Source.database
        );
    }

    private void getIsr(final View fragmentView, final List<Timeline> timeline, final Map<String, Pill> pillsMap) {
        Token token = AppState.getInsnatce().getToken();
        UserDao.getIsr(
                token,
                new CallbackOne<Isr>() {
                    @Override
                    public void execute(Isr isr) {
                        initFragmentFinish(fragmentView, timeline, pillsMap, isr);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        String isrId = (String) AppSharedPreferences.get(AppSharedPreferences.Preference.isr);
                        Isr isr = new Isr();
                        isr.setId(TextUtils.isEmpty(isrId) ? "0" : isrId);
                        initFragmentFinish(fragmentView, timeline, pillsMap, isr);
                    }
                }
        );
    }

    private void initFragmentFinish(final View fragmentView, final List<Timeline> timeline, final Map<String, Pill> pillsMap, final Isr isr) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (timeline == null || timeline.isEmpty()) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishSubHelper(fragmentView, timeline, pillsMap, isr);
        }
    }

    private void initFragmentFinishSubHelper(final View fragmentView, final List<Timeline> timeline, final Map<String, Pill> pillsMap, final Isr isr) {
        String isrId = (isr == null || TextUtils.isEmpty(isr.getId())) ? "0" : isr.getId();
        AppSharedPreferences.put(AppSharedPreferences.Preference.isr, isrId);
        AppState.getInsnatce().setIsr(isr);

        if (pillsMap != null && !pillsMap.isEmpty()) {
            mPillsMap.clear();
            mPillsMap.putAll(pillsMap);
        }

        mFullTimelineList.clear();
        mFullTimelineList.addAll(timeline);
        separateFullTimeline();

        // TODO: tagView is a hack, solve this problem
        View tagView = fragmentView.findViewById(R.id.textViewFooter);
        if (tagView == null || tagView.getTag() == null) {
            initFragmentFinishHelper(fragmentView);
            tagView.setTag(true);
        } else {
            onTimelineUpdated(fragmentView);
        }

        updateMissedEvents();
    }

    private void initDialogBody(final RadioGroup RadioGroup, final Button buttonSave) {
        RadioGroup.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup.setOnCheckedChangeListener(null);
                buttonSave.setEnabled(true);
            }
        });
    }

    private void tryToSave(final View dialogBodyView, final Task task, final Map<String, Pill> pillsMap) {
        Token token = AppState.getInsnatce().getToken();

        switch (task.getEnumType()) {
            case diet:
                RadioButton radioButtonFollowDiet = (RadioButton) dialogBodyView.findViewById(R.id.radioButtonFollowDiet);
                tryToSaveHelper(task, Task.createResult(Task.Type.diet, radioButtonFollowDiet.isChecked()), token);
                break;
            case exercise:
                TextView textViewPhysicalActivity = (TextView) dialogBodyView.findViewById(R.id.textViewPhysicalActivity);
                tryToSaveHelper(task, Task.createResult(Task.Type.exercise, (Integer) textViewPhysicalActivity.getTag()), token);
                break;
            case pill:
                RadioButton radioButtonPillsTaken = (RadioButton) dialogBodyView.findViewById(R.id.radioButtonPillsTaken);
                tryToSaveHelper(task, Task.createResult(Task.Type.pill, radioButtonPillsTaken.isChecked()), token);
                break;
            case smoking:
                RadioButton radioButtonSmoke = (RadioButton) dialogBodyView.findViewById(R.id.radioButtonSmoke);
                tryToSaveHelper(task, Task.createResult(Task.Type.smoking, radioButtonSmoke.isChecked()), token);
                break;
            case weight:
                TextView textViewWeight = (TextView) dialogBodyView.findViewById(R.id.textViewWeight);
                tryToSaveHelper(task, Task.createResult(Task.Type.weight, (Integer) textViewWeight.getTag()), token);
                break;
            case arterialPressure:
                TextView textViewPressure = (TextView) dialogBodyView.findViewById(R.id.textViewPressure);
                tryToSaveHelper(task, Task.createResult(Task.Type.arterialPressure, (Integer) textViewPressure.getTag()), token);
                break;
            case cholesterol:
                TextView textViewCholesterol = (TextView) dialogBodyView.findViewById(R.id.textViewCholesterol);
                tryToSaveHelper(task, Task.createResult(Task.Type.cholesterol, (Integer) textViewCholesterol.getTag()), token);
                break;
        }
    }

    protected void udateFullTimelineList(Task updatedTask) {
        for (Timeline timeline : mFullTimelineList) {
            Task oldTask = null;
            for (Task task : timeline.getTasks()) {
                if (task.getId().equals(updatedTask.getId())) {
                    oldTask = task;
                    break;
                }
            }
            if (oldTask != null) {
                timeline.getTasks().remove(oldTask);
                timeline.getTasks().add(updatedTask);
                return;
            }
        }
    }

    private void tryToSaveHelper(final Task task, final ObjectNode taskResult, final Token token) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        TaskDao.update(
                task,
                taskResult,
                token,
                new CallbackOne<Task>() {
                    @Override
                    public void execute(Task task) {
                        tryToGetIsr(task);
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

    private void tryToGetIsr(final Task task) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();

        Token token = AppState.getInsnatce().getToken();
        UserDao.getIsr(
                token,
                new CallbackOne<Isr>() {
                    @Override
                    public void execute(Isr isr) {
                        slidingMenuActivity.hideProgressDialog();

                        String isrId = (isr == null || TextUtils.isEmpty(isr.getId())) ? "0" : isr.getId();
                        AppSharedPreferences.put(AppSharedPreferences.Preference.isr, isrId);
                        AppState.getInsnatce().setIsr(isr);

                        AppState.getInsnatce().setTimelineEvents(AppState.getInsnatce().getTimelineEvents() - 1);
                        updateMissedEvents();
                        onTaskUpdated(task);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();

                        AppState.getInsnatce().setTimelineEvents(AppState.getInsnatce().getTimelineEvents() - 1);
                        updateMissedEvents();
                        onTaskUpdated(task);
                    }
                }
        );
    }

    protected void tryShowTaskDialog(final Task task, final Map<String, Pill> pillsMap) {
        if (task.getEnumType().equals(Task.Type.pill) && (pillsMap == null || pillsMap.get(task.getPill()) == null))
            CustomDialogs.showAlertDialog(getActivity(), R.string.refresh_pills);
        else
            showTaskDialog(task, pillsMap);
    }

    private void showTaskDialog(final Task task, final Map<String, Pill> pillsMap) {
        final Context context = this.getActivity();

        final Button buttonSave = createButtonSave(context);
        final View dialogBodyView = createDialogBody(context, task, buttonSave);

        if (dialogBodyView == null) return;

        customizeDialogIfPills(context, dialogBodyView, task, pillsMap);

        View.OnClickListener buttonSaveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToSave(dialogBodyView, task, pillsMap);
            }
        };

        final View dialogBodyViewHelper = dialogBodyView;
        Callback onDismissListener = new Callback() {
            @Override
            public void execute() {
                Tools.hideKeyboard(dialogBodyViewHelper);
            }
        };

        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(getActivity())
                .setBody(dialogBodyView)
                .addButton(R.string.cancel, CustomDialogLayout.DialogStandardAction.dismiss)
                .addButton(buttonSave, buttonSaveClickListener)
                .setOnDismissListener(onDismissListener)
                .create();

        AlertDialog alertDialog = new AlertDialog
                .Builder(getActivity())
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);

        alertDialog.show();
    }

    private void customizeDialogIfPills(Context context, View dialogBodyView, Task task, Map<String, Pill> pillsMap) {
        if (task.getEnumType().equals(Task.Type.pill)) {
            TextView textViewPillsTaken = (TextView) dialogBodyView.findViewById(R.id.textViewPillsTaken);
            String taken = context.getString(R.string.taken, pillsMap.get(task.getPill()).getName());
            textViewPillsTaken.setText(taken);
        }
    }

    private Button createButtonSave(Context context) {
        Button buttonSave = (Button) View.inflate(context, R.layout.layout_button_main, null);
        buttonSave.setEnabled(false);
        buttonSave.setText(R.string.save);
        return buttonSave;
    }

    private View createDialogBody(Context context, Task task, Button buttonSave) {
        View dialogBodyView = null;
        switch (task.getEnumType()) {
            case diet:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_diet, null);
                RadioGroup radioGroupFollowDiet = (RadioGroup) dialogBodyView.findViewById(R.id.radioGroupFollowDiet);
                initDialogBody(radioGroupFollowDiet, buttonSave);
                break;
            case exercise:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_physical_activity, null);
                TextView textViewPhysicalActivity = (TextView) dialogBodyView.findViewById(R.id.textViewPhysicalActivity);
                initDialogBody(textViewPhysicalActivity, buttonSave, PhysicalActivity.MIN, PhysicalActivity.MAX, PhysicalActivity.INIT, context.getString(R.string.physical_activities_hint));
                break;
            case pill:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_pills_named, null);
                RadioGroup radioGroupPillsTaken = (RadioGroup) dialogBodyView.findViewById(R.id.radioGroupPillsTaken);
                initDialogBody(radioGroupPillsTaken, buttonSave);
                break;
            case smoking:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_still_smoke, null);
                RadioGroup radioGroupSmoke = (RadioGroup) dialogBodyView.findViewById(R.id.radioGroupSmoke);
                initDialogBody(radioGroupSmoke, buttonSave);
                break;
            case weight:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_weight, null);
                TextView textViewWeight = (TextView) dialogBodyView.findViewById(R.id.textViewWeight);
                initDialogBody(textViewWeight, buttonSave, Weight.MIN, Weight.MAX, Weight.INIT, context.getString(R.string.data_weight_hint));
                break;
            case arterialPressure:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_pressure, null);
                TextView textViewPressure = (TextView) dialogBodyView.findViewById(R.id.textViewPressure);
                initDialogBody(textViewPressure, buttonSave, ArterialPressure.MIN, ArterialPressure.MAX, ArterialPressure.INIT, context.getString(R.string.arterial_pressure_hint));
                break;
            case cholesterol:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_cholesterol, null);
                TextView textViewCholesterol = (TextView) dialogBodyView.findViewById(R.id.textViewCholesterol);
                initDialogBody(textViewCholesterol, buttonSave, CholesterolLevel.MIN, CholesterolLevel.MAX, CholesterolLevel.INIT, context.getString(R.string.data_cholesterol_hint));
                break;
        }

        if (dialogBodyView != null) {
            int spaceMedium = (int) context.getResources().getDimension(R.dimen.space_medium);
            dialogBodyView.setPadding(spaceMedium, spaceMedium, spaceMedium, spaceMedium);
        }

        return dialogBodyView;
    }

    private void initDialogBody(final TextView textView,
                                final Button buttonSave,
                                final int minValue,
                                final int maxValue,
                                final int startValue,
                                final String description) {
        ProfileHelper.initTextViewWithNumberPicker(textView, minValue, maxValue, startValue, description);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonSave.setEnabled(s.length() > 0);
            }
        });
    }

    private void initDialogBody(final EditText editText, final Button buttonSave) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                buttonSave.setEnabled(s.length() > 0);
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editText.setOnFocusChangeListener(null);
                InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
    }

    protected void separateFullTimeline() {
        mNewTimelineList.clear();
        mFilledTimelineList.clear();

        for (Timeline timeline : mFullTimelineList) {
            List<Task> newTasks = new ArrayList<>();
            List<Task> filledTasks = new ArrayList<>();

            for (Task task : timeline.getTasks()) {
                if (!task.getIsCompleted()) newTasks.add(task);
                else filledTasks.add(task);
            }

            if (!newTasks.isEmpty()) {
                Timeline newTimeline = new Timeline();
                newTimeline.setDate(timeline.getDate());
                newTimeline.setTasks(newTasks);
                mNewTimelineList.add(newTimeline);
            }

            if (!filledTasks.isEmpty()) {
                Timeline filledTimeline = new Timeline();
                filledTimeline.setDate(timeline.getDate());
                filledTimeline.setTasks(filledTasks);
                mFilledTimelineList.add(filledTimeline);
            }
        }
    }

}
