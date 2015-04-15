package ru.com.cardiomagnyl.model.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        "weight",
        "arterialPressure",
        "isCompleted"
})
@DatabaseTable(tableName = "task")
public class Task extends BaseModel {

    public enum Type {diet, exercise, pill, weight, arterialPressure, /*next fields are fake*/ smoking, cholesterol, undefined}

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    @JsonProperty("id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "type")
    @JsonProperty("type")
    private String type;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "exercise_mins")
    @JsonProperty("exerciseMins")
    private int exerciseMins;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "weight")
    @JsonProperty("weight")
    private int weight;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "arterial_pressure")
    @JsonProperty("arterialPressure")
    private int arterialPressure;

    @DatabaseField(dataType = DataType.BOOLEAN, columnName = "is_completed")
    @JsonProperty("isCompleted")
    private boolean isCompleted;

    // special field for recognizing "third boolean status"
    // in the field "is Completed" of the class TaskProxy
    @DatabaseField(dataType = DataType.BOOLEAN, columnName = "is_completed_fully")
    @JsonProperty("isCompletedFully")
    private boolean isCompletedFully;

    @DatabaseField(dataType = DataType.STRING, columnName = "pill")
    @JsonProperty("pill")
    private String pill;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "timeline")
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
     * @return The weight
     */
    @JsonProperty("weight")
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    @JsonProperty("weight")
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public int getArterialPressure() {
        return arterialPressure;
    }

    /**
     * @param arterialPressure The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public void setArterialPressure(int arterialPressure) {
        this.arterialPressure = arterialPressure;
    }

    /**
     * @return The isCompleted
     */
    @JsonProperty("isCompleted")
    public boolean getIsCompleted() {
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
     * @return The isCompletedFully
     */
    @JsonProperty("isCompletedFully")
    public boolean getIsCompletedFully() {
        return isCompletedFully;
    }

    /**
     * @param isCompletedFully The isCompletedFully
     */
    @JsonProperty("isCompletedFully")
    public void setIsCompletedFully(boolean isCompletedFully) {
        this.isCompletedFully = isCompletedFully;
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

    /**
     * @return The pill
     */
    @JsonProperty("pill")
    public String getPill() {
        return pill;
    }

    /**
     * @param pill The pill
     */
    @JsonProperty("pill")
    public void setPill(String pill) {
        this.pill = pill;
    }

    public Type getEnumType() {
        Task.Type enumType = Task.Type.undefined;
        try {
            enumType = Task.Type.valueOf(type);
        } catch (Exception ex) { /*does nothing*/ }
        return enumType;
    }

    public static Map<String, Task> listToMap(List<Task> tasksList) {
        if (tasksList == null) return null;

        Map<String, Task> tasksMap = new HashMap<>();
        for (Task task : tasksList) tasksMap.put(task.getId(), task);

        return tasksMap;
    }

    public static ObjectNode createResult(Type type, boolean value) {
        ObjectNode nodeResult = JsonNodeFactory.instance.objectNode();
        switch (type) {
            case diet:
            case pill:
            case smoking:
                nodeResult.put("isCompleted", value);
        }
        return nodeResult;
    }

    public static ObjectNode createResult(Type type, Integer value) {
        ObjectNode nodeResult = JsonNodeFactory.instance.objectNode();
        switch (type) {
            case exercise:
                nodeResult.put("exerciseMins", value);
                break;
            case weight:
                nodeResult.put("weight", value);
                break;
            case arterialPressure:
                nodeResult.put("arterialPressure", value);
                break;
            case cholesterol:
                break;
            case undefined:
                break;
        }
        return nodeResult;
    }

}
