package ru.com.cardiomagnil.ca_model.test;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.com.cardiomagnil.ca_model.base.BaseModel;

public class Ca_Banner extends BaseModel {

    @JsonProperty("state")
    private String state;
    @JsonProperty("title")
    private String title;
    @JsonProperty("subtitle")
    private String subtitle;
    @JsonProperty("note")
    private String note;
    @JsonProperty("shouldHaveLink")
    private boolean shouldHaveLink;

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

    /**
     * @return The shouldHaveLink
     */
    @JsonProperty("shouldHaveLink")
    public boolean isShouldHaveLink() {
        return shouldHaveLink;
    }

    /**
     * @param shouldHaveLink The shouldHaveLink
     */
    @JsonProperty("shouldHaveLink")
    public void setShouldHaveLink(boolean shouldHaveLink) {
        this.shouldHaveLink = shouldHaveLink;
    }

}