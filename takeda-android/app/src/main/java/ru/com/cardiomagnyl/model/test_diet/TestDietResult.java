package ru.com.cardiomagnyl.model.test_diet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "messages",
        "red",
        "blue"
})
public class TestDietResult extends BaseModel {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "db_id")
    private String dbId;
    @JsonProperty("messages")
    private List<String> messages = new ArrayList<>();
    @JsonProperty("red")
    private List<TestDietRecomendation> red = new ArrayList<>();
    @JsonProperty("blue")
    private List<TestDietRecomendation> blue = new ArrayList<>();

    /**
     * @return The messages
     */
    @JsonProperty("messages")
    public List<String> getMessages() {
        return messages;
    }

    /**
     * @param messages The messages
     */
    @JsonProperty("messages")
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * @return The red
     */
    @JsonProperty("red")
    public List<TestDietRecomendation> getRed() {
        return red;
    }

    /**
     * @param red The red
     */
    @JsonProperty("red")
    public void setRed(List<TestDietRecomendation> red) {
        this.red = red;
    }

    /**
     * @return The blue
     */
    @JsonProperty("blue")
    public List<TestDietRecomendation> getBlue() {
        return blue;
    }

    /**
     * @param blue The blue
     */
    @JsonProperty("blue")
    public void setBlue(List<TestDietRecomendation> blue) {
        this.blue = blue;
    }

}