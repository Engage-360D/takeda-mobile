package ru.com.cardiomagnyl.model.test_diet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.test_diet_answer.TestDietAnswer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "question",
        "answers"
})
@DatabaseTable(tableName = "test_diet")
public class TestDiet extends BaseModel {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "db_id")
    private String dbId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "id")
    @JsonProperty("id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "question")
    @JsonProperty("question")
    private String question;

    @ForeignCollectionField(eager = true, columnName = "answers")
    @JsonProperty("answers")
    @JsonDeserialize(as = ArrayList.class)
    private Collection<TestDietAnswer> answers = new ArrayList<>();

    @DatabaseField(dataType = DataType.STRING, columnName = "test_id")
    private String testId;

    /**
     * @return The dbId
     */
    public String getDbId() {
        return dbId;
    }

    /**
     * @param dbId The dbId
     */
    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

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
     * @return The answers
     */
    @JsonProperty("answers")
    public Collection<TestDietAnswer> getAnswers() {
        return answers;
    }

    /**
     * @param answers The answers
     */
    @JsonProperty("answers")
    public void setAnswers(List<TestDietAnswer> answers) {
        this.answers.clear();
        this.answers.addAll(answers);
    }

    /**
     * @return The testId
     */
    public String setTestId() {
        return testId;
    }

    /**
     * @param testId The testId
     */
    public void setTestId(String testId) {
        this.testId = testId;
        this.dbId = testId + "_" + id;
    }

}
