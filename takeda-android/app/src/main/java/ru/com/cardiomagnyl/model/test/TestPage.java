package ru.com.cardiomagnyl.model.test;

import android.util.Pair;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "state",
        "title",
        "subtitle",
        "text"
})
@DatabaseTable(tableName = "page")
public class TestPage {
    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "state")
    @JsonProperty("state")
    private String state;

    @DatabaseField(dataType = DataType.STRING, columnName = "title")
    @JsonProperty("title")
    private String title;

    @DatabaseField(dataType = DataType.STRING, columnName = "subtitle")
    @JsonProperty("subtitle")
    private String subtitle;

    @DatabaseField(dataType = DataType.STRING, columnName = "text")
    @JsonProperty("text")
    private String text;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public static String generatePageId(String link) {
        Pair<String, String> extractIdAndName = extractIdAndName(link);
        return extractIdAndName.first + "_" + extractIdAndName.second;
    }

    private static Pair<String, String> extractIdAndName(String link) {
        String[] separated = link.split("/");
        String testId = separated[separated.length - 3];
        String recomendationName = separated[separated.length - 1];

        return new Pair(testId, recomendationName);
    }

}