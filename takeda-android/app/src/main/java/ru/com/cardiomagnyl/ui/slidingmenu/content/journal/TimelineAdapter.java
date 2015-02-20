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

    public TimelineAdapter(Context context, List<Timeline> timeline, Map<String, Pill> pillsMap) {
        mContext = context;
        mTimeline = timeline;
        mPpillsMap = pillsMap;
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
        setTasksHolder(linearLayoutTasksHolder, mTimeline.get(position));

        return view;
    }

    private void setTasksHolder(ViewGroup viewGroup, Timeline timeline) {
        viewGroup.removeAllViews();
        for (Task task : timeline.getTasks()) {
            View taskView = getTaskView(task);
            viewGroup.addView(taskView);
        }
    }

    private View getTaskView(final Task task) {
        View taskView = View.inflate(mContext, R.layout.list_item_task, null);

        taskView.setTag(task);
        setTextTaskView(taskView, task);

        return taskView;
    }

    private void setTextTaskView(final View parentView, final Task task) {
        TextView textViewTaskName = (TextView) parentView.findViewById(R.id.textViewTaskName);
        TextView textViewTaskSubname = (TextView) parentView.findViewById(R.id.textViewTaskSubname);

        Task.Type type = Task.Type.undefined;
        try {
            type = Task.Type.valueOf(task.getType().toLowerCase());
        } catch (Exception ex) {
            // do nothing
        }

        int taskNameText = -1;
        String taskSubnameText = "";
        switch (type) {
            case diet:
                taskNameText = R.string.what_eaten;
                break;
            case exercise:
                taskNameText = R.string.exercise_stress;
                break;
            case pill:
                taskNameText = R.string.take_pills;
                taskSubnameText = mPpillsMap.get(task.getPill()).getName();
                break;
            case smoking:
                taskNameText = R.string.still_smoke;
                break;
            case weight:
                taskNameText = R.string.enter_weight;
                break;
            case pressure:
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
}
