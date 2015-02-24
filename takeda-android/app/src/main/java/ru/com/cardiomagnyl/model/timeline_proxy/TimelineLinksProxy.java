
package ru.com.cardiomagnyl.model.timeline_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "tasks"
})
public class TimelineLinksProxy extends BaseModel {

    @JsonProperty("tasks")
    private List<String> tasks = new ArrayList<>();

    /**
     * @return The tasks
     */
    @JsonProperty("tasks")
    public List<String> getTasks() {
        return tasks;
    }

    /**
     * @param tasks The tasks
     */
    @JsonProperty("tasks")
    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

}