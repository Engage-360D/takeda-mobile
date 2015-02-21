package ru.com.cardiomagnyl.model.task_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.task.Task;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "type",
        "isCompleted",
        "exerciseMins",
        "links"
})
public class TaskProxy extends BaseModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    // FIXME: change type to primitive_type when problem will be fixed o the server
    @JsonProperty("isCompleted")
    private Boolean isCompleted;

    @JsonProperty("exerciseMins")
    private int exerciseMins;

    @JsonProperty("links")
    private TaskLinksProxy links;

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
    public Boolean isIsCompleted() {
        return isCompleted;
    }

    /**
     * @param isCompleted The isCompleted
     */
    @JsonProperty("isCompleted")
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * @return The links
     */
    @JsonProperty("links")
    public TaskLinksProxy getLinks() {
        return links;
    }

    /**
     * @param links The links
     */
    @JsonProperty("links")
    public void setLinks(TaskLinksProxy links) {
        this.links = links;
    }

    public static List<Task> extractAllTasks(List<TaskProxy> taskProxy) {
        if (taskProxy == null) return null;

        List<Task> allTasks = new ArrayList<>();
        for (TaskProxy currentTaskProxy : taskProxy) allTasks.add(currentTaskProxy.extractTask());

        return allTasks;
    }

    // FIXME: improve performance!!!
    public Task extractTask() {
        ObjectNode objectNode = new ObjectMapper().valueToTree(this);
        unPackLinks(objectNode);
        Task task = (Task) BaseModel.stringToObject(objectNode.toString(), new TypeReference<Task>() {
        });
        task.setIsCompleted(isCompleted != null);
        task.setIsCompletedFully(isCompleted != null && isCompleted);
        return task;
    }

    public static void unPackLinks(ObjectNode objectNodePacked) {
        if (objectNodePacked != null) {
            JsonNode jsonNode = objectNodePacked.get("links");
            objectNodePacked.remove("links");
            if (jsonNode != null) objectNodePacked.putAll((ObjectNode) jsonNode);
        }
    }

}
