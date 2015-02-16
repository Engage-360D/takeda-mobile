package ru.com.cardiomagnyl.model.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pageUrl",
        "state",
        "title",
        "subtitle",
        "note"
})
public class TestBanner extends BaseModel {

    @JsonProperty("pageUrl")
    private String pageUrl;
    @JsonProperty("state")
    private String state;
    @JsonProperty("title")
    private String title;
    @JsonProperty("subtitle")
    private String subtitle;
    @JsonProperty("note")
    private String note;

    /**
     * @return The pageUrl
     */
    @JsonProperty("pageUrl")
    public String getPageUrl() {
        return pageUrl;
    }

    /**
     * @param pageUrl The pageUrl
     */
    @JsonProperty("pageUrl")
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * @return The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     * @param state The state
     */
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

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
     * @return The subtitle
     */
    @JsonProperty("subtitle")
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle The subtitle
     */
    @JsonProperty("subtitle")
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
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

}