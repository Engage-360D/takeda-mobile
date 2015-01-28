package ru.com.cardiomagnil.application;

import android.view.View;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.com.cardiomagnil.app.BuildConfig;
import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ca_model.base.BaseModel;
import ru.com.cardiomagnil.ca_model.test.Ca_TestResult;
import ru.com.cardiomagnil.ca_model.test.Ca_TestSource;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.util.TestMethods;

public class AppState {
    // ///////////////////////////////////////////////////////////////
    // Singleton implementation
    // ///////////////////////////////////////////////////////////////
    private static AppState instance;

    private AppState() {
    }

    public static AppState getInstatce() {
        if (instance == null) {
            synchronized (AppState.class) {
                if (instance == null)
                    instance = new AppState();
            }
        }

        return instance;
    }

    // ///////////////////////////////////////////////////////////////

    private Ca_Token mToken;
    private Ca_User mUser;
    private Ca_TestSource mTestSource;
    private Ca_TestResult mTestResult;

    public Ca_Token getToken() {
        return mToken;
    }

    public void setToken(Ca_Token token) {
        mToken = token;
    }

    public Ca_User getUser() {
        return mUser;
    }

    public void setUser(Ca_User user) {
        mUser = user;
    }

    public Ca_TestSource getTestSource() {
        return mTestSource;
    }

    public void setTestSource(Ca_TestSource testSource) {
        mTestSource = testSource;
    }

    public Ca_TestResult getTestResult() {
        if (BuildConfig.DEBUG) {
            if (mTestResult == null) {
                String testResultString = "{\"id\":\"15\",\"sex\":\"male\",\"birthday\":\"1980-01-26T18:43:58+0000\",\"growth\":180,\"weight\":95,\"isSmoker\":false,\"cholesterolLevel\":3,\"isCholesterolDrugsConsumer\":false,\"hasDiabetes\":false,\"hadSugarProblems\":false,\"isSugarDrugsConsumer\":false,\"arterialPressure\":120,\"isArterialPressureDrugsConsumer\":false,\"physicalActivityMinutes\":200,\"hadHeartAttackOrStroke\":false,\"isAddingExtraSalt\":false,\"isAcetylsalicylicDrugsConsumer\":false,\"bmi\":29.3,\"score\":0,\"recommendations\":{\"scoreNote\":{\"state\":\"ok\",\"text\":\"Категориянизкогорискаразвитиясмертельныхсердечно-сосудистыхзаболеванийвближайшие10лет\"},\"fullScreenAlert\":null,\"mainRecommendation\":{\"text\":\"Обратитеськврачудляконсультациионеобходимостиприемапрепаратовацетилсалициловойкислотыдляснижениярискатромбозов.\\n\"},\"placesLinkShouldBeVisible\":false,\"banners\":{\"isSmoker\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Курение\",\"subtitle\":\"\",\"note\":\"Всехорошо\"},\"arterialPressure\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Систолическоедавление\",\"subtitle\":\"близкокнорме\",\"note\":\"Всехорошо\"},\"isAddingExtraSalt\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Потреблениесоли\",\"subtitle\":\"\",\"note\":\"Всехорошо\"},\"cholesterolLevel\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Уровеньхолестирина\",\"subtitle\":\"близкокнорме\",\"note\":\"Всехорошо\"},\"physicalActivityMinutes\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Физическаяактивность\",\"subtitle\":\"близкокнорме\",\"note\":\"Всехорошо\"},\"bmi\":{\"pageUrl\":\"/test-results/15/bmi\",\"state\":\"bell\",\"title\":\"Вес\",\"subtitle\":\"среднееотклонение\",\"note\":\"Необходимоулучшение\"},\"hadSugarProblems\":null,\"isArterialPressureDrugsConsumer\":null,\"isCholesterolDrugsConsumer\":null}}}";
                TypeReference typeReference = new TypeReference<Ca_TestResult>() {
                };
                mTestResult = (Ca_TestResult) BaseModel.stringToObject(testResultString, typeReference);
            }
        }

        return mTestResult;
    }

    public void setTestResult(Ca_TestResult testResult) {
        mTestResult = testResult;
    }

    public void setTestResult(String testResult) {
        TypeReference typeReference = new TypeReference<Ca_TestResult>() {
        };
        mTestResult = (Ca_TestResult) BaseModel.stringToObject(testResult, typeReference);
    }
}