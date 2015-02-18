package ru.com.cardiomagnyl.model.timeline;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.task.Task;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "tasks"
})
@DatabaseTable(tableName = "timeline")
public class Timeline extends BaseModel {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "date")
    @JsonProperty("date")
    private String date;

    @ForeignCollectionField(eager = true, columnName = "tasks")
    @JsonProperty("tasks")
    @JsonDeserialize(as = ArrayList.class)
    private Collection<Task> tasks = new ArrayList<>();

    @DatabaseField(dataType = DataType.STRING, columnName = "user_id")
    private String userId;

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
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
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
    }

}