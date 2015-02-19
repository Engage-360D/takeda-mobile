
package ru.com.cardiomagnyl.model.task_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "pill"
})
public class TaskLinksProxy {

    @JsonProperty("pill")
    private String pill;

    /**
     * @return The pill
     */
    @JsonProperty("pill")
    public String getPills() {
        return pill;
    }

    /**
     * @param pill The pill
     */
    @JsonProperty("pill")
    public void setPills(String pill) {
        this.pill = pill;
    }

}