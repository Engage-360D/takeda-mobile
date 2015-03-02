package ru.com.cardiomagnyl.model.test_diet_proxy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test_diet.TestDiet;
import ru.com.cardiomagnyl.model.test_diet_answer.TestDietAnswer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "test_diet_proxies",
        "test_diet_answers"
})
public class TestDietMergedProxy extends BaseModel {

    @JsonProperty("test_diet_proxies")
    private List<TestDietRawProxy> testDietProxies = new ArrayList<>();
    @JsonProperty("test_diet_answers")
    private List<TestDietAnswer> testDietAnswers = new ArrayList<>();

    /**
     * @return The testDietProxies
     */
    @JsonProperty("test_diet_proxies")
    public List<TestDietRawProxy> getTestDietProxies() {
        return testDietProxies;
    }

    /**
     * @param testDietProxies The testDietProxies
     */
    @JsonProperty("test_diet_proxies")
    public void setTestDietProxies(List<TestDietRawProxy> testDietProxies) {
        this.testDietProxies = testDietProxies;
    }

    /**
     * @return The testDietAnswers
     */
    @JsonProperty("test_diet_answers")
    public List<TestDietAnswer> geTestDietAnswers() {
        return testDietAnswers;
    }

    /**
     * @param testDietAnswers The testDietAnswers
     */
    @JsonProperty("test_diet_answers")
    public void setTestDietAnswers(List<TestDietAnswer> testDietAnswers) {
        this.testDietAnswers = testDietAnswers;
    }

    public List<TestDiet> extractAllTestDiets() {
        Map<String, TestDietAnswer> mapTestDietAnswers = TestDietAnswer.listToMap(testDietAnswers);

        List<TestDiet> extractedTestDiet = new ArrayList<>();
        for (TestDietRawProxy currentTestDietRawProxy : testDietProxies) {
            TestDiet testDiet = extractTestDiet(currentTestDietRawProxy, mapTestDietAnswers);
            extractedTestDiet.add(testDiet);
        }

        return extractedTestDiet;
    }

    private TestDiet extractTestDiet(TestDietRawProxy testDietRawProxy, Map<String, TestDietAnswer> mapTestDietAnswers) {
        TestDiet testDiet = new TestDiet();

        List<TestDietAnswer> testDietAnswers = new ArrayList<>();
        for (String stringTestDietAnswer : testDietRawProxy.getTestDietLinksProxy().getAnswers()) {
            testDietAnswers.add(mapTestDietAnswers.get(stringTestDietAnswer));
        }
        testDiet.setAnswers(testDietAnswers);
        testDiet.setId(testDietRawProxy.getId());
        testDiet.setQuestion(testDietRawProxy.getQuestion());


        return testDiet;
    }

    public static void mergeResponse(Response response) {
        JsonNode jsonNodeData = response.getData();
        JsonNode jsonNodeAnswers = response.getLinks().get("answers");

        ObjectNode jsonNodeMergedData = JsonNodeFactory.instance.objectNode();
        jsonNodeMergedData.put("test_diet_proxies", jsonNodeData);
        jsonNodeMergedData.put("test_diet_answers", jsonNodeAnswers);

        response.setData(jsonNodeMergedData);
    }

}