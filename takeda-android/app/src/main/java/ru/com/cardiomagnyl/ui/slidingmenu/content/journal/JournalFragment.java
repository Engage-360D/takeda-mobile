package ru.com.cardiomagnyl.ui.slidingmenu.content.journal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.task.TaskDao;
import ru.com.cardiomagnyl.model.timeline.Timeline;
import ru.com.cardiomagnyl.model.timeline.TimelineDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.Callback;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.TimelineComparator;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.util.Utils;
import ru.com.cardiomagnyl.widget.CustomDialogLayout;

public class JournalFragment extends BaseItemFragment implements SwipeRefreshLayout.OnRefreshListener {
    private final List<Timeline> mFullTimelineList = new ArrayList<>();
    private final List<Timeline> mNewTimelineList = new ArrayList<>();
    private final List<Timeline> mFilledTimelineList = new ArrayList<>();
    private final List<Timeline> mCurrentTimelineList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, null);
        initFragmentStart(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarMenuBellCabinet(viewGroupTopBar, true, true, true);
    }

    @Override
    public void onRefresh() {
        View fragmentView = getView();

        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        getTimeline(fragmentView);

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipeRefreshLayoutTimeline);
        swipeLayout.setRefreshing(false);
    }

    private void initFragmentStart(final View fragmentView) {
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
                        getPillDatabase(fragmentView, timeline);
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

    private void getPillDatabase(final View fragmentView, final List<Timeline> timeline) {
        Token token = AppState.getInsnatce().getToken();
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        Map<String, Pill> pillsMap = Pill.listToMap(pillsList);
                        if (Timeline.checkPillsInTasks(timeline, pillsMap)) {
                            initFragmentFinish(fragmentView, timeline, pillsMap);
                        } else {
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
                PillDao.Source.database
        );
    }

    private void initFragmentFinish(final View fragmentView, final List<Timeline> timeline, final Map<String, Pill> pillsMap) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (pillsMap == null) ((TextView) textViewMessage).setText(R.string.refresh_pills);

        if (timeline == null || timeline.isEmpty() || pillsMap == null) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishHelper(fragmentView, timeline, pillsMap);
        }
    }

    private void initFragmentFinishHelper(final View fragmentView, final List<Timeline> timeline, final Map<String, Pill> pillsMap) {
        final RadioGroup radioGroupTabs = (RadioGroup) fragmentView.findViewById(R.id.radioGroupTabs);
        final SwipeRefreshLayout swipeRefreshLayoutTimeline = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipeRefreshLayoutTimeline);
        final ListView listViewTimeline = (ListView) fragmentView.findViewById(R.id.listViewTimeline);

        mFullTimelineList.clear();
        mFullTimelineList.addAll(timeline);
        separateFullTimeline();

        mCurrentTimelineList.clear();
        mCurrentTimelineList.addAll(mNewTimelineList);
        radioGroupTabs.check(R.id.radioButtonNew);

        swipeRefreshLayoutTimeline.setOnRefreshListener(this);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object tag = view.getTag();
                if (tag != null || tag instanceof Task)
                    showTaskDialog(fragmentView, (Task) tag, pillsMap);
            }
        };

        final TimelineAdapter timelineAdapter = new TimelineAdapter(JournalFragment.this.getActivity(), mCurrentTimelineList, pillsMap, onClickListener);
        listViewTimeline.setAdapter(timelineAdapter);

        radioGroupTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                timelineAdapter.notifyDataSetChanged();
                mCurrentTimelineList.clear();
                mCurrentTimelineList.addAll(checkedId == R.id.radioButtonNew ? mNewTimelineList : mFilledTimelineList);
                timelineAdapter.notifyDataSetInvalidated();
            }
        });
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
                EditText editTextPhysicalActivity = (EditText) dialogBodyView.findViewById(R.id.editTextPhysicalActivity);
                tryToSaveHelper(task, Task.createResult(Task.Type.exercise, editTextPhysicalActivity.getText().toString()), token);
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
                EditText editTextWeight = (EditText) dialogBodyView.findViewById(R.id.editTextWeight);
                tryToSaveHelper(task, Task.createResult(Task.Type.weight, editTextWeight.getText().toString()), token);
                break;
            case pressure:
                EditText editTextPressure = (EditText) dialogBodyView.findViewById(R.id.editTextPressure);
                tryToSaveHelper(task, Task.createResult(Task.Type.pressure, editTextPressure.getText().toString()), token);
                break;
            case cholesterol:
                EditText editTextCholesterol = (EditText) dialogBodyView.findViewById(R.id.editTextCholesterol);
                tryToSaveHelper(task, Task.createResult(Task.Type.cholesterol, editTextCholesterol.getText().toString()), token);
                break;
        }
    }

    private void updateListView(Task updatedTask) {
        Activity activity = JournalFragment.this.getActivity();
        ListView listViewTimeline = (ListView) activity.findViewById(R.id.listViewTimeline);
        RadioGroup radioGroupTabs = (RadioGroup) activity.findViewById(R.id.radioGroupTabs);
        TimelineAdapter timelineAdapter = (TimelineAdapter) listViewTimeline.getAdapter();
        timelineAdapter.notifyDataSetChanged();
        udateFullTimelineList(updatedTask);
        separateFullTimeline();
        mCurrentTimelineList.clear();
        mCurrentTimelineList.addAll(radioGroupTabs.getCheckedRadioButtonId() == R.id.radioButtonNew ? mNewTimelineList : mFilledTimelineList);
        timelineAdapter.notifyDataSetInvalidated();
    }

    private void udateFullTimelineList(Task updatedTask) {
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
                        slidingMenuActivity.hideProgressDialog();
                        updateListView(task);
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

    private void showTaskDialog(final View fragmentView, final Task task, final Map<String, Pill> pillsMap) {
        final Context context = fragmentView.getContext();

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
                Utils.hideKeyboard(dialogBodyViewHelper);
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
        Button buttonSave = new Button(context);
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
                EditText editTextPhysicalActivity = (EditText) dialogBodyView.findViewById(R.id.editTextPhysicalActivity);
                initDialogBody(editTextPhysicalActivity, buttonSave);
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
                EditText editTextWeight = (EditText) dialogBodyView.findViewById(R.id.editTextWeight);
                initDialogBody(editTextWeight, buttonSave);
                break;
            case pressure:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_pressure, null);
                EditText editTextPressure = (EditText) dialogBodyView.findViewById(R.id.editTextPressure);
                initDialogBody(editTextPressure, buttonSave);
                break;
            case cholesterol:
                dialogBodyView = View.inflate(context, R.layout.layout_ask_cholesterol, null);
                EditText editTextCholesterol = (EditText) dialogBodyView.findViewById(R.id.editTextCholesterol);
                initDialogBody(editTextCholesterol, buttonSave);
                break;
        }

        if (dialogBodyView != null) {
            int spaceMedium = (int) context.getResources().getDimension(R.dimen.space_medium);
            dialogBodyView.setPadding(spaceMedium, spaceMedium, spaceMedium, spaceMedium);
        }

        return dialogBodyView;
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
    }

    private void separateFullTimeline() {
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
