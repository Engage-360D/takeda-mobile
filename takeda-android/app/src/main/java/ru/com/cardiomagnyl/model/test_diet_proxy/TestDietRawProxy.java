package ru.com.cardiomagnyl.model.test_diet_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "question",
        "links"
})
public class TestDietRawProxy extends BaseModel {

    @JsonProperty("id")
    private String id;
    @JsonProperty("question")
    private String question;
    @JsonProperty("links")
    private TestDietLinksProxy links;

    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The question
     */
    @JsonProperty("question")
    public String getQuestion() {
        return question;
    }

    /**
     * @param question The question
     */
    @JsonProperty("question")
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return The links
     */
    @JsonProperty("links")
    public TestDietLinksProxy getTestDietLinksProxy() {
        return links;
    }

    /**
     * @param links The links
     */
    @JsonProperty("links")
    public void setTestDietLinksProxy(TestDietLinksProxy links) {
        this.links = links;
    }

}
