package ru.com.cardiomagnyl.model.test_diet;

import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnyl.model.base.BaseModel;

@DatabaseTable(tableName = "test_diet_result")
public class TestDietResultHolder extends BaseModel {

    private TestDietResult mTestDietResult;

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "test_id")
    private String mTestId;

    @DatabaseField(dataType = DataType.STRING, columnName = "json")
    private String mJson;

    public TestDietResultHolder() {
    }

    public TestDietResultHolder(TestDietResult testDietResult, String testId) {
        mTestDietResult = testDietResult;
        mTestId = testId;
        mJson = new ObjectMapper().valueToTree(testDietResult).toString();
    }

    public TestDietResult getTestDietResult() {
        if (mTestDietResult == null && !TextUtils.isEmpty(mJson)) {
            TypeReference typeReference = new TypeReference<TestDietResult>() {
            };
            mTestDietResult = (TestDietResult) BaseModel.stringToObject(mJson, typeReference);
        }
        return mTestDietResult;
    }

}
