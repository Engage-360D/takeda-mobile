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
import ru.com.cardiomagnyl.model.task_proxy.TaskProxy;
import ru.com.cardiomagnyl.model.timeline.Timeline;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timeline_proxy",
        "tasks_proxy"
})
public class TimelineMergedProxy extends BaseModel {

    @JsonProperty("timeline_proxy")
    private List<TimelineRawProxy> timelineProxy = new ArrayList<TimelineRawProxy>();
    @JsonProperty("tasks_proxy")
    private List<TaskProxy> tasksProxy = new ArrayList<>();

    /**
     * @return The timelineProxy
     */
    @JsonProperty("timeline_proxy")
    public List<TimelineRawProxy> getTimelineProxy() {
        return timelineProxy;
    }

    /**
     * @param timelineProxy The timelineProxy
     */
    @JsonProperty("timeline_proxy")
    public void setTimelineProxy(List<TimelineRawProxy> timelineProxy) {
        this.timelineProxy = timelineProxy;
    }

    /**
     * @return The tasksProxy
     */
    @JsonProperty("tasks_proxy")
    public List<TaskProxy> getTasksProxy() {
        return tasksProxy;
    }

    /**
     * @param tasksProxy The tasksProxy
     */
    @JsonProperty("tasks_proxy")
    public void setTaskProxy(List<TaskProxy> tasksProxy) {
        this.tasksProxy = tasksProxy;
    }

    public List<Timeline> extractAllTimeline() {
        List<Task> tasks = TaskProxy.extractAllTasks(tasksProxy);
        Map<String, Task> mapTasks = Task.listToMap(tasks);

        List<Timeline> extractedTimeline = new ArrayList<>();
        for (TimelineRawProxy currentTimelineRawProxy : timelineProxy) {
            Timeline timeline = extractTimeline(currentTimelineRawProxy, mapTasks);
            extractedTimeline.add(timeline);
        }

        return extractedTimeline;
    }

    private Timeline extractTimeline(TimelineRawProxy timelineRawProxy, Map<String, Task> mapTasks) {
        Timeline timeline = new Timeline();

        List<Task> tasks = new ArrayList<>();
        for (String stringTask : timelineRawProxy.getTimelineProxyLinks().getTasks()) {
            tasks.add(mapTasks.get(stringTask));
        }
        timeline.setTasks(tasks);
        timeline.setDate(timelineRawProxy.getDate());

        return timeline;
    }

    public static void mergeResponse(Response response) {
        JsonNode jsonNodeData = response.getData();
        JsonNode jsonNodeTasks = response.getLinked().get("tasks");

        ObjectNode jsonNodeMergedData = JsonNodeFactory.instance.objectNode();
        jsonNodeMergedData.put("timeline_proxy", jsonNodeData);
        jsonNodeMergedData.put("tasks_proxy", jsonNodeTasks);

        response.setData(jsonNodeMergedData);
    }

}