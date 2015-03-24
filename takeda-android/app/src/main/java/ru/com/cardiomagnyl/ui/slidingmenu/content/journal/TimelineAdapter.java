package ru.com.cardiomagnyl.ui.slidingmenu.content.journal;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.timeline.Timeline;
import ru.com.cardiomagnyl.util.Tools;

public class TimelineAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Timeline> mTimeline;
    private final Map<String, Pill> mPpillsMap;
    private final View.OnClickListener mOnClickListener;

    public TimelineAdapter(Context context, List<Timeline> timeline, Map<String, Pill> pillsMap, View.OnClickListener onClickListener) {
        mContext = context;
        mTimeline = timeline;
        mPpillsMap = pillsMap;
        mOnClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return mTimeline.size();
    }

    @Override
    public Object getItem(int position) {
        return mTimeline.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = (convertView != null ? convertView : View.inflate(mContext, R.layout.list_item_timeline, null));
        final TextView textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        final LinearLayout linearLayoutTasksHolder = (LinearLayout) view.findViewById(R.id.linearLayoutTasksHolder);

        Date date = Tools.dateFromShortDate(mTimeline.get(position).getDate());

        textViewDate.setText(Tools.formatShortHintedDate(date));
        initTasksHolder(linearLayoutTasksHolder, mTimeline.get(position));

        return view;
    }

    public void initTasksHolder(ViewGroup viewGroup, Timeline timeline) {
        viewGroup.removeAllViews();
        for (Task task : timeline.getTasks()) {
            View taskView = getTaskView(task);
            viewGroup.addView(taskView);
        }
    }

    private View getTaskView(final Task task) {
        View taskView;

        if (!task.getIsCompleted()) {
            taskView = View.inflate(mContext, R.layout.list_item_task_new, null);
            initNewTaskView(taskView, task);
            taskView.setTag(task);
            taskView.setEnabled(true);
            taskView.setOnClickListener(mOnClickListener);
        } else {
            taskView = View.inflate(mContext, R.layout.list_item_task_filled, null);
            initFilledTaskView(taskView, task);
            taskView.setTag(null);
            taskView.setEnabled(false);
            taskView.setClickable(false);
        }

        return taskView;
    }

    private void initNewTaskView(final View parentView, final Task task) {
        TextView textViewTaskName = (TextView) parentView.findViewById(R.id.textViewTaskName);
        TextView textViewTaskSubname = (TextView) parentView.findViewById(R.id.textViewTaskSubname);

        int taskNameText = -1;
        String taskSubnameText = "";
        switch (task.getEnumType()) {
            case diet:
                taskNameText = R.string.follow_diet;
                break;
            case exercise:
                taskNameText = R.string.exercise_stress;
                break;
            case pill:
                taskNameText = R.string.take_pills;
                boolean pillIsOk = mPpillsMap != null && mPpillsMap.get(task.getPill()) != null;
                taskSubnameText = pillIsOk ? mPpillsMap.get(task.getPill()).getName() : parentView.getContext().getString(R.string.refresh_pills);
                break;
            case smoking:
                taskNameText = R.string.still_smoke;
                break;
            case weight:
                taskNameText = R.string.enter_weight;
                break;
            case arterialPressure:
                taskNameText = R.string.enter_pressure;
                break;
            case cholesterol:
                taskNameText = R.string.enter_cholesterol;
                break;
        }

        if (taskNameText < 0) {
            parentView.setVisibility(View.GONE);
        } else {
            textViewTaskName.setText(taskNameText);
            textViewTaskSubname.setVisibility(TextUtils.isEmpty(taskSubnameText) ? View.GONE : View.VISIBLE);
            textViewTaskSubname.setText(taskSubnameText);
        }
    }

    private void initFilledTaskView(final View parentView, final Task task) {
        TextView textViewTaskName = (TextView) parentView.findViewById(R.id.textViewTaskName);
        TextView textViewTaskSubname = (TextView) parentView.findViewById(R.id.textViewTaskSubname);

        TextView textViewTaskStatus = (TextView) parentView.findViewById(R.id.textViewTaskStatus);
        TextView textViewTaskSubstatus = (TextView) parentView.findViewById(R.id.textViewTaskSubstatus);

        int taskNameText = -1;
        String taskSubnameText = "";
        int taskStatusText = -1;
        String taskSubstatusText = "";
        switch (task.getEnumType()) {
            case diet:
                taskNameText = R.string.follow_diet;
                taskStatusText = task.getIsCompletedFully() ? R.string.fulfilled : R.string.not_fulfilled;
                break;
            case exercise:
                taskNameText = R.string.exercise_stress;
                taskStatusText = task.getIsCompletedFully() ? R.string.fulfilled : R.string.not_fulfilled;
                taskSubstatusText = String.valueOf(task.getExerciseMins()) + " " + mContext.getString(R.string.min);
                break;
            case pill:
                taskNameText = R.string.take_pills;
                boolean pillIsOk = mPpillsMap != null && mPpillsMap.get(task.getPill()) != null;
                taskSubnameText = pillIsOk ? mPpillsMap.get(task.getPill()).getName() : parentView.getContext().getString(R.string.refresh_pills);
                taskStatusText = task.getIsCompletedFully() ? R.string.fulfilled : R.string.not_fulfilled;
                break;
            case smoking:
                taskNameText = R.string.still_smoke;
                taskStatusText = task.getIsCompletedFully() ? R.string.fulfilled : R.string.not_fulfilled;
                break;
            case weight:
                taskNameText = R.string.enter_weight;
                taskStatusText = task.getIsCompletedFully() ? R.string.fulfilled : R.string.not_fulfilled;
                break;
            case arterialPressure:
                taskNameText = R.string.enter_pressure;
                taskStatusText = task.getIsCompletedFully() ? R.string.fulfilled : R.string.not_fulfilled;
                break;
            case cholesterol:
                taskNameText = R.string.enter_cholesterol;
                taskStatusText = task.getIsCompletedFully() ? R.string.fulfilled : R.string.not_fulfilled;
                break;
        }

        if (taskNameText < 0) {
            parentView.setVisibility(View.GONE);
        } else {
            textViewTaskName.setText(taskNameText);
            textViewTaskSubname.setVisibility(TextUtils.isEmpty(taskSubnameText) ? View.GONE : View.VISIBLE);
            textViewTaskSubname.setText(taskSubnameText);

            textViewTaskStatus.setText(taskStatusText);
            textViewTaskSubstatus.setVisibility(TextUtils.isEmpty(taskSubstatusText) ? View.GONE : View.VISIBLE);
            textViewTaskSubstatus.setText(taskSubstatusText);
        }
    }

}
