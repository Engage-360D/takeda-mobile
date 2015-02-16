package ru.com.cardiomagnyl.model.test;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "state",
        "text"
})
public class TestNote extends BaseModel {

    @JsonProperty("state")
    private String state;
    @JsonProperty("text")
    private String text;

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

    public static boolean isEmpty(TestNote testNote) {
        return testNote == null || (TextUtils.isEmpty(testNote.getState()) && TextUtils.isEmpty(testNote.getText()));
    }

}