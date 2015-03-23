package ru.com.cardiomagnyl.model.timeline;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.util.Tools;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "tasks"
})
@DatabaseTable(tableName = "timeline")
public class Timeline extends BaseModel implements Parcelable {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "date")
    @JsonProperty("date")
    private String date;

    @ForeignCollectionField(eager = true, columnName = "tasks")
    @JsonProperty("tasks")
    @JsonDeserialize(as = ArrayList.class)
    private Collection<Task> tasks = new ArrayList<>();

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "user")
    private String userId;

    public Timeline() {
    }

    /**
     * @return The date
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     * @param date The date
     */
    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return The tasks
     */
    @JsonProperty("tasks")
    public Collection<Task> getTasks() {
        return tasks;
    }

    /**
     * @param tasks The tasks
     */
    @JsonProperty("tasks")
    public void setTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    /**
     * @return The userId
     */
    public String setUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
        this.id = userId + "_" + date;
    }

    public static boolean checkPillsInTasks(final List<Timeline> timeline, final Map<String, Pill> pillsMap) {
        for (Timeline currentTimeline : timeline) {
            for (Task currentTask : currentTimeline.getTasks()) {
                if (Task.Type.pill.name().equals(currentTask.getType()) && pillsMap.get(currentTask.getPill()) == null)
                    return false;
            }
        }
        return true;
    }

    public static int calculateMissedTasks(List<Timeline> timeline) {
        int result = 0;

        for (Timeline currentTimelineItem : timeline)
            if (currentTimelineItem.getTasks() != null)
                for (Task task : currentTimelineItem.getTasks())
                    if (!task.getIsCompleted()) ++result;

        return result;
    }

    ///////////////////////////////////////////////////////////////////////
    // implements Parcelable
    ///////////////////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.date);
        dest.writeList(new ArrayList<Task>(this.tasks));
        dest.writeString(this.userId);
    }

    private Timeline(Parcel in) {
        this.id = in.readString();
        this.date = in.readString();
        List<Task> tasks = new ArrayList<Task>();
        in.readList(tasks, getClass().getClassLoader());
        this.setTasks(tasks);
        this.userId = in.readString();
    }

    public static final Parcelable.Creator<Timeline> CREATOR = new Parcelable.Creator<Timeline>() {
        public Timeline createFromParcel(Parcel source) {
            return new Timeline(source);
        }

        public Timeline[] newArray(int size) {
            return new Timeline[size];
        }
    };

    ///////////////////////////////////////////////////////////////////////
}