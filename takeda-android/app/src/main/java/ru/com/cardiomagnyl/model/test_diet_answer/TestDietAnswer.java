package ru.com.cardiomagnyl.model.test_diet_answer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.test_diet.TestDiet;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "answer",
        "weight"
})
@DatabaseTable(tableName = "test_diet_answer")
public class TestDietAnswer extends BaseModel {

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "db_id")
    private String dbId;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "id")
    @JsonProperty("id")
    private String id;

    @DatabaseField(dataType = DataType.STRING, columnName = "answer")
    @JsonProperty("answer")
    private String answer;

    @DatabaseField(dataType = DataType.INTEGER, columnName = "weight")
    @JsonProperty("weight")
    private int weight;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "test_diet")
    private TestDiet testDiet;

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
     * @return The answer
     */
    @JsonProperty("answer")
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer The answer
     */
    @JsonProperty("answer")
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * @return The weight
     */
    @JsonProperty("weight")
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    @JsonProperty("weight")
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return The testDiet
     */
    public TestDiet getTestDiet() {
        return testDiet;
    }

    /**
     * @param testDiet The testDiet
     */
    public void setTestDiet(TestDiet testDiet) {
        this.testDiet = testDiet;
        this.dbId = testDiet.getId() + "_" + id;
    }

    public static Map<String, TestDietAnswer> listToMap(List<TestDietAnswer> testDietAnswerList) {
        if (testDietAnswerList == null) return null;

        Map<String, TestDietAnswer> testDietAnswerMap = new HashMap<>();
        for (TestDietAnswer testDietAnswer : testDietAnswerList) {
            testDietAnswerMap.put(testDietAnswer.getId(), testDietAnswer);
        }

        return testDietAnswerMap;
    }

}
