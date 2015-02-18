package ru.com.cardiomagnyl.model.timeline_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.timeline.Timeline;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timeline_proxy",
        "tasks"
})
public class TimelineMergedProxy extends BaseModel {

    @JsonProperty("timeline_proxy")
    private List<TimelineProxy> timelineProxy = new ArrayList<TimelineProxy>();
    @JsonProperty("tasks")
    private List<Task> tasks = new ArrayList<Task>();

    /**
     * @return The timelineProxy
     */
    @JsonProperty("timeline_proxy")
    public List<TimelineProxy> getTimelineProxy() {
        return timelineProxy;
    }

    /**
     * @param timelineProxy The timelineProxy
     */
    @JsonProperty("timeline_proxy")
    public void setTimelineProxy(List<TimelineProxy> timelineProxy) {
        this.timelineProxy = timelineProxy;
    }

    /**
     * @return The tasks
     */
    @JsonProperty("tasks")
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * @param tasks The tasks
     */
    @JsonProperty("links")
    public void setTask(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Timeline> extractAllTimeline() {
        List<Timeline> extractedTimeline = new ArrayList<>();
        Map<String, Task> mapTasks = Task.listToMap(tasks);

        for (TimelineProxy currentTimelineProxy : timelineProxy) {
            Timeline timeline = extractTimeline(currentTimelineProxy, mapTasks);
            extractedTimeline.add(timeline);
        }

        return extractedTimeline;
    }

    public Timeline extractTimeline(TimelineProxy timelineProxy, Map<String, Task> mapTasks) {
        Timeline timeline = new Timeline();

        List<Task> tasks = new ArrayList<>();
        for (String stringTask : timelineProxy.getTimelineProxyLinks().getTasks()) {
            tasks.add(mapTasks.get(stringTask));
        }
        timeline.setTasks(tasks);
        timeline.setDate(timelineProxy.getDate());

        return timeline;
    }

    public static void mergeResponse(Response response) {
        JsonNode jsonNodeData = response.getData();
        JsonNode jsonNodeTasks = response.getLinked().get("tasks");

        ObjectNode jsonNodeMergedData = JsonNodeFactory.instance.objectNode();
        jsonNodeMergedData.put("timeline_proxy", jsonNodeData);
        jsonNodeMergedData.put("tasks", jsonNodeTasks);

        response.setData(jsonNodeMergedData);
    }

}
