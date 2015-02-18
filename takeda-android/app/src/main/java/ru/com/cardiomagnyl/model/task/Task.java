package ru.com.cardiomagnyl.model.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.timeline.Timeline;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "type",
        "exerciseMins",
        "isCompleted"
})
@DatabaseTable(tableName = "task")
public class Task extends BaseModel {

    public enum Type {diet, exercise, pill}

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    @JsonProperty("id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "type")
    @JsonProperty("type")
    private String type;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "exercise_mins")
    @JsonProperty("exerciseMins")
    private int exerciseMins;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = "is_completed")
    @JsonProperty("isCompleted")
    private boolean isCompleted;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "timeline")
    private Timeline timeline;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The exerciseMins
     */
    @JsonProperty("exerciseMins")
    public int getExerciseMins() {
        return exerciseMins;
    }

    /**
     * @param exerciseMins The exerciseMins
     */
    @JsonProperty("exerciseMins")
    public void setExerciseMins(int exerciseMins) {
        this.exerciseMins = exerciseMins;
    }

    /**
     * @return The isCompleted
     */
    @JsonProperty("isCompleted")
    public boolean isIsCompleted() {
        return isCompleted;
    }

    /**
     * @param isCompleted The isCompleted
     */
    @JsonProperty("isCompleted")
    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * @return The timeline
     */
    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * @param timeline The timeline
     */
    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public static Map<String, Task> listToMap(List<Task> listTasks) {
        if (listTasks == null) return null;

        Map<String, Task> mapTasks = new HashMap<>();
        for (Task task : listTasks) mapTasks.put(task.getId(), task);

        return mapTasks;
    }

}
