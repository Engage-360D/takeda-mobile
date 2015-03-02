package ru.com.cardiomagnyl.model.test_diet;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "title",
        "note",
        "text"
})
public class TestDietRecomendation extends BaseModel {

    @JsonProperty("title")
    private String title;
    @JsonProperty("note")
    private String note;
    @JsonProperty("text")
    private String text;

    /**
     * @return The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The note
     */
    @JsonProperty("note")
    public String getNote() {
        return note;
    }

    /**
     * @param note The note
     */
    @JsonProperty("note")
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return The text
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * @param text The text
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

}