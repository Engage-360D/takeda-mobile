package ru.com.cardiomagnyl.model.test;


import android.text.TextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import ru.com.cardiomagnyl.model.base.BaseModel;

@DatabaseTable(tableName = "test_result")
public class TestResultHolder {

    private TestResult mTestResult;

    @DatabaseField(id = true, canBeNull = false, dataType = DataType.STRING, columnName = "id")
    private String mId;

    @DatabaseField(dataType = DataType.STRING, columnName = "created_at")
    private String mCreatedAt;

    @DatabaseField(dataType = DataType.STRING, columnName = "json")
    private String mJson;

    @DatabaseField(dataType = DataType.STRING, columnName = "user_id")
    private String mUserId;

    public TestResultHolder(){}

    public TestResultHolder(TestResult testResult, String userId) {
        mTestResult = testResult;
        mId = testResult.getId();
        mCreatedAt = testResult.getCreatedAt();
        mJson = new ObjectMapper().valueToTree(testResult).toString();
        mUserId = userId;
    }

    public TestResult getTestResult() {
        if (mTestResult == null && !TextUtils.isEmpty(mJson)) {
            TypeReference typeReference = new TypeReference<TestResult>() {
            };
            mTestResult = (TestResult) BaseModel.stringToObject(mJson, typeReference);
        }
        return mTestResult;
    }
}
