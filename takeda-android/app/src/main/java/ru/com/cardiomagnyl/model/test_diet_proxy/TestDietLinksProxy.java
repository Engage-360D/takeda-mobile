
package ru.com.cardiomagnyl.model.test_diet_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)

@JsonPropertyOrder({
        "answers"
})
public class TestDietLinksProxy extends BaseModel {

    @JsonProperty("answers")
    private List<String> answers = new ArrayList<>();

    /**
     * @return The answers
     */
    @JsonProperty("answers")
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * @param answers The answers
     */
    @JsonProperty("answers")
    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

}