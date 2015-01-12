package ru.com.cardiomagnil.model;

import java.util.HashMap;
import java.util.Map;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.util.Tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestResult {
    // ///////////////////////////////////////////////////////////////
    // Fields of TestResult
    // ///////////////////////////////////////////////////////////////
    // unused fields from TestData
    // ////////////////////////////////////////
    // "acetylsalicylicDrugs" : true,
    // "arterialPressureDrugs" : false,
    // "cholesterolDrugs" : true,
    // "sugarDrugs" : false,
    // "sugarProblems" : false,
    // "arterialPressure" : 100,
    // "birthday" : "1900-10-10T00:00:00+0230",
    // "cholesterolLevel" : 8,
    // "diabetes" : true,
    // "extraSalt" : true,
    // "heartAttackOrStroke" : true,
    // "physicalActivity" : 100,
    // "smoking" : true,
    // "weight" : 95,
    // "growth": 185,
    // ////////////////////////////////////////
    private String mSex = null; // "sex" : "female"

    private float mBmi; // bmi
    private String mCreatedAt; // createdAt
    private int mId; // id
    private int mScoreValue = -1; // scoreValue
    private boolean mInstitutionsUrl; // institutionsUrl
    private Map<String, TestResultBanner> mBanners; // banners
    private Map<String, TestResultPage> mPages; // pages
    private Map<String, TestResultPiece> mPieces = new HashMap<String, TestResultPiece>(); // pieces
    // ///////////////////////////////////////////////////////////////

    private boolean mIsInitialized = false;
    private JsonObject mJsonTestResult;

    public enum PIECES {
        scoreDescription, dangerAlert, mainRecommendation
    };

    public enum BANNERS {
        arterialPressure, arterialPressureDrugs, bmi, cholesterolDrugs, cholesterolLevel, extraSalt, physicalActivity, smoking, sugarProblems
    };

    public enum PAGES {
        // real: arterialPressure, bmi, cholesterolLevel, extraSalt, physicalActivity, smoking
        arterialPressure, arterialPressureDrugs, bmi, cholesterolDrugs, cholesterolLevel, extraSalt, physicalActivity, smoking, sugarProblems
    };

    public enum STATES {
        attention, ok, bell, doctor, ask, empty
    }

    public TestResult(String stringTestResult) {
        initTestResult(stringTestResult);
    }

    public TestResult(JsonObject jsonTestResult) {
        initTestResult(jsonTestResult);
        mJsonTestResult = jsonTestResult;
    }

    public String getSex() {
        return mSex;
    }

    private void setSex(String sex) {
        mSex = sex;
    }

    public float getBmi() {
        return mBmi;
    }

    private void setBmi(float bmi) {
        mBmi = bmi;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    private void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public int getId() {
        return mId;
    }

    private void setId(int id) {
        mId = id;
    }

    public int getScoreValue() {
        return mScoreValue;
    }

    private void setScoreValue(int scoreValue) {
        mScoreValue = scoreValue;
    }

    public boolean getInstitutionsUrl() {
        return mInstitutionsUrl;
    }

    private void setInstitutionsUrl(boolean institutionsUrl) {
        mInstitutionsUrl = institutionsUrl;
    }

    public Map<String, TestResultBanner> getBanners() {
        return mBanners;
    }

    private void setBanners(Map<String, TestResultBanner> banners) {
        mBanners = banners;
    }

    public Map<String, TestResultPage> getPages() {
        return mPages;
    }

    private void setPages(Map<String, TestResultPage> pages) {
        mPages = pages;
    }

    public Map<String, TestResultPiece> getPieces() {
        return mPieces;
    }

    private void setPieces(Map<String, TestResultPiece> pieces) {
        mPieces = pieces;
    }

    public boolean isInitialized() {
        return mIsInitialized && (mScoreValue >= 0);
    }

    public JsonObject getAsJson() {
        return mJsonTestResult;
    }

    private void initTestResult(String stringTestResult) {
        mIsInitialized = false;

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = (JsonElement) jsonParser.parse(stringTestResult);
            JsonObject jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
            initTestResult(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }

    private void initTestResult(JsonObject jsonTestResult) {
        mIsInitialized = false;

        try {
            if (jsonTestResult == null || jsonTestResult.isJsonNull()) {
                return;
            }

            JsonElement jsonElementSex = Tools.jsonElementByMemberName(jsonTestResult, "sex");
            setSex((jsonElementSex != null) ? jsonElementSex.getAsString() : "");

            JsonElement jsonElementBmi = Tools.jsonElementByMemberName(jsonTestResult, "bmi");
            setBmi((jsonElementBmi != null) ? jsonElementBmi.getAsFloat() : 0f);

            JsonElement jsonElementCreatedAt = Tools.jsonElementByMemberName(jsonTestResult, "createdAt");
            setCreatedAt((jsonElementCreatedAt != null) ? jsonElementCreatedAt.getAsString() : "");

            JsonElement jsonElementId = Tools.jsonElementByMemberName(jsonTestResult, "id");
            setId((jsonElementId != null) ? jsonElementId.getAsInt() : 0);

            JsonElement jsonElementScoreValue = Tools.jsonElementByMemberName(jsonTestResult, "scoreValue");
            setScoreValue((jsonElementScoreValue != null) ? jsonElementScoreValue.getAsInt() : -1);

            JsonElement jsonElementInstitutionsUrl = Tools.jsonElementByMemberName(jsonTestResult, "institutionsUrl");
            setInstitutionsUrl((jsonElementInstitutionsUrl != null) ? jsonElementInstitutionsUrl.getAsBoolean() : false);

            // recommendations
            JsonElement jsonElementRecommendations = Tools.jsonElementByMemberName(jsonTestResult, "recommendations");
            JsonObject jsonObjectRecommendations = (jsonElementRecommendations != null) ? jsonElementRecommendations.getAsJsonObject() : new JsonObject();

            JsonElement jsonElementBanners = Tools.jsonElementByMemberName(jsonObjectRecommendations, "banners");
            setBanners((jsonElementBanners != null) ? extractBanners(jsonElementBanners.getAsJsonObject()) : null);

            JsonElement jsonElementPages = Tools.jsonElementByMemberName(jsonObjectRecommendations, "pages");
            setPages((jsonElementPages != null) ? extractPages(jsonElementPages.getAsJsonObject()) : null);

            // scoreDescription
            JsonElement jsonElementScoreDescription = Tools.jsonElementByMemberName(jsonObjectRecommendations, "scoreDescription");
            mPieces.put("scoreDescription", ((jsonElementScoreDescription != null) ? new TestResultPiece(jsonElementScoreDescription.getAsJsonObject()) : null));

            // dangerAlert
            JsonElement jsonElementDangerAlert = Tools.jsonElementByMemberName(jsonObjectRecommendations, "dangerAlert");
            mPieces.put("dangerAlert", ((jsonElementDangerAlert != null) ? new TestResultPiece(jsonElementDangerAlert.getAsJsonObject()) : null));

            // mainRecommendation
            JsonElement jsonElementMainRecommendation = Tools.jsonElementByMemberName(jsonObjectRecommendations, "mainRecommendation");
            mPieces.put("mainRecommendation", ((jsonElementMainRecommendation != null) ? new TestResultPiece(jsonElementMainRecommendation.getAsJsonObject()) : new TestResultPiece(new JsonObject())));

            mIsInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }

    private Map<String, TestResultBanner> extractBanners(JsonObject jsonBanners) {
        Map<String, TestResultBanner> banners = new HashMap<String, TestResultBanner>();

        for (BANNERS banner : BANNERS.values()) {
            JsonElement jsonElementBanner = Tools.jsonElementByMemberName(jsonBanners, banner.name());
            TestResultBanner testResultBanner = (jsonElementBanner != null) ? new TestResultBanner(jsonElementBanner.getAsJsonObject()) : new TestResultBanner(new JsonObject());
            banners.put(banner.name(), testResultBanner);
        }

        return banners;
    }

    private Map<String, TestResultPage> extractPages(JsonObject jsonPages) {
        Map<String, TestResultPage> pages = new HashMap<String, TestResultPage>();

        for (PAGES page : PAGES.values()) {
            JsonElement jsonElementPage = Tools.jsonElementByMemberName(jsonPages, page.name());
            TestResultPage testResultPage = (jsonElementPage != null) ? new TestResultPage(jsonElementPage.getAsJsonObject()) : null;
            pages.put(page.name(), testResultPage);
        }

        return pages;
    }
}