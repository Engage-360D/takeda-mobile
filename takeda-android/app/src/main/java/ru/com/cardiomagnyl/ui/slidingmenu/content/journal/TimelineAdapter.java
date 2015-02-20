package ru.com.cardiomagnyl.ui.slidingmenu.content.journal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.timeline.Timeline;

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
        final LinearLayout linearLayoutTasksHolder = (LinearLayout) view.findViewById(R.id.linearLayoutTasksHolder);

        linearLayoutTasksHolder.removeAllViews();

        for (Task task : mTimeline.get(position).getTasks()) {
            View taskView = getTaskView(task);
            linearLayoutTasksHolder.addView(taskView);
        }

        return view;
    }

    private View getTaskView(final Task task) {
        View taskView = View.inflate(mContext, R.layout.list_item_task, null);
        TextView textViewTaskName = (TextView) taskView.findViewById(R.id.textViewTaskName);

        taskView.setTag(task);
        setTextView(taskView, textViewTaskName, task.getType());

        return taskView;
    }

    private void setTextView(final View parentView, final TextView textView, final String taskType) {
        Task.Type type = Task.Type.undefined;

        try {
            type = Task.Type.valueOf(taskType.toLowerCase());
        } catch (Exception ex) {
            // do nothing
        }

        int taskText = -1;
        switch (type) {
            case diet:
                taskText = R.string.what_eaten;
                break;
            case exercise:
                taskText = R.string.exercise_stress;
                break;
            case pill:
                taskText = R.string.take_pills;
                break;
            case smoking:
                taskText = R.string.still_smoke;
                break;
            case weight:
                taskText = R.string.enter_weight;
                break;
            case pressure:
                taskText = R.string.enter_pressure;
                break;
            case cholesterol:
                taskText = R.string.enter_cholesterol;
                break;
        }

        if (taskText < 0) {
            parentView.setVisibility(View.GONE);
        } else {
            textView.setText(taskText);
        }
    }
}
