package ru.com.cardiomagnil.application;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.com.cardiomagnil.app.BuildConfig;
import ru.com.cardiomagnil.ca_model.base.BaseModel;
import ru.com.cardiomagnil.ca_model.test.Ca_TestResult;
import ru.com.cardiomagnil.ca_model.test.Ca_TestSource;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.user.Ca_User;

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
                String testResultString = "{\"id\":\"15\",\"sex\":\"male\",\"birthday\":\"1980-01-26T00:00:00+0300\",\"growth\":180,\"weight\":95,\"isSmoker\":false,\"cholesterolLevel\":3,\"isCholesterolDrugsConsumer\":false,\"hasDiabetes\":false,\"hadSugarProblems\":false,\"isSugarDrugsConsumer\":false,\"arterialPressure\":120,\"isArterialPressureDrugsConsumer\":false,\"physicalActivityMinutes\":200,\"hadHeartAttackOrStroke\":false,\"isAddingExtraSalt\":false,\"isAcetylsalicylicDrugsConsumer\":false,\"bmi\":29.3,\"score\":77,\"recommendations\":{\"scoreNote\":{\"state\":\"ok\",\"text\":\"Категория низкого риска развития смертельных сердечно-сосудистых заболеваний в ближайшие 10 лет\"},\"fullScreenAlert\":null,\"mainRecommendation\":{\"text\":\"Обратитесь к врачу для консультации о необходимости приема препаратов ацетилсалициловой кислоты для снижения риска тромбозов.\"},\"placesLinkShouldBeVisible\":false,\"banners\":{\"isSmoker\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Курение\",\"subtitle\":\"\",\"note\":\"Все хорошо\"},\"arterialPressure\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Систолическое давление\",\"subtitle\":\"близко к норме\",\"note\":\"Все хорошо\"},\"isAddingExtraSalt\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Потребление соли\",\"subtitle\":\"\",\"note\":\"Все хорошо\"},\"cholesterolLevel\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Уровень холестирина\",\"subtitle\":\"близко к норме\",\"note\":\"Все хорошо\"},\"physicalActivityMinutes\":{\"pageUrl\":null,\"state\":\"ok\",\"title\":\"Физическая активность\",\"subtitle\":\"близко к норме\",\"note\":\"Все хорошо\"},\"bmi\":{\"pageUrl\":\"/test-results/15/bmi\",\"state\":\"bell\",\"title\":\"Вес\",\"subtitle\":\"среднее отклонение\",\"note\":\"Необходимо улучшение\"},\"hadSugarProblems\":null,\"isArterialPressureDrugsConsumer\":null,\"isCholesterolDrugsConsumer\":null}}}";
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