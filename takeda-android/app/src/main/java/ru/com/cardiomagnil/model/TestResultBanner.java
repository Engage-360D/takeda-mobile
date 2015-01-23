package ru.com.cardiomagnil.model;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.util.Tools;
import ru.com.cardiomagnil.model.TestResult.STATES;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestResultBanner {
    // ///////////////////////////////////////////////////////////////
    // Fields of TestResultBanner
    // ///////////////////////////////////////////////////////////////
    private String mAberration; // aberration
    private String mNote; // note
    private String mPageUrl; // pageUrl
    private STATES mState; // state
    private String mTitle; // title
    // ///////////////////////////////////////////////////////////////

    private boolean mIsInitialized = false;
    private JsonObject mJsonTestResultBanner;

    public TestResultBanner(String stringTestResultBanner) {
        initTestResultBanner(stringTestResultBanner);
    }

    public TestResultBanner(JsonObject jsonTestResultBanner) {
        initTestResultBanner(jsonTestResultBanner);
        mJsonTestResultBanner = jsonTestResultBanner;
    }

    public String getAberration() {
        return mAberration;
    }

    private void setAberration(String aberration) {
        mAberration = aberration;
    }

    public String getNote() {
        return mNote;
    }

    private void setNote(String note) {
        mNote = note;
    }

    public String getPageUrl() {
        return mPageUrl;
    }

    private void setPageUrl(String pageUrl) {
        mPageUrl = pageUrl;
    }

    public STATES getState() {
        return mState;
    }

    private void setState(STATES state) {
        mState = state;
    }

    public String getTitle() {
        return mTitle;
    }

    private void setTitle(String title) {
        mTitle = title;
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public JsonObject getAsJson() {
        return mJsonTestResultBanner;
    }

    private void initTestResultBanner(String stringTestResultBanner) {
        mIsInitialized = false;

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = (JsonElement) jsonParser.parse(stringTestResultBanner);
            JsonObject jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
            initTestResultBanner(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }

    private void initTestResultBanner(JsonObject jsonTestResultBanner) {
        mIsInitialized = false;

        try {
            if (jsonTestResultBanner == null || jsonTestResultBanner.isJsonNull()) {
                return;
            }

            JsonElement jsonElementAberration = Tools.jsonElementByMemberName(jsonTestResultBanner, "aberration");
            setAberration((jsonElementAberration != null) ? jsonElementAberration.getAsString() : "");

            JsonElement jsonElementNote = Tools.jsonElementByMemberName(jsonTestResultBanner, "note");
            setNote((jsonElementNote != null) ? jsonElementNote.getAsString() : "");

            JsonElement jsonElementPageUrl = Tools.jsonElementByMemberName(jsonTestResultBanner, "pageUrl");
            setPageUrl((jsonElementPageUrl != null) ? jsonElementPageUrl.getAsString() : "");

            JsonElement jsonElementState = Tools.jsonElementByMemberName(jsonTestResultBanner, "state");
            setState((jsonElementState != null) ? STATES.valueOf(jsonElementState.getAsString()) : STATES.empty);

            JsonElement jsonElementTitle = Tools.jsonElementByMemberName(jsonTestResultBanner, "title");
            setTitle((jsonElementTitle != null) ? jsonElementTitle.getAsString() : "");

            mIsInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }
}