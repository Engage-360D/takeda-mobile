package ru.com.cardiomagnyl.model.incident;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "comment"
})
public class Incident extends BaseModel {
    public enum Name {infarction, apoplexy, shunting}

    @JsonProperty("name")
    private String name;
    @JsonProperty("comment")
    private String comment;

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The comment
     */
    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    /**
     * @param comment The comment
     */
    @JsonProperty("comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

}