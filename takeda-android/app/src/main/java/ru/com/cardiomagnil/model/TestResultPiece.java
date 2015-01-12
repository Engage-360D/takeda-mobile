package ru.com.cardiomagnil.model;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.util.Tools;
import ru.com.cardiomagnil.model.TestResult.STATES;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestResultPiece {
    // ///////////////////////////////////////////////////////////////
    // Fields of TestResultPiece
    // ///////////////////////////////////////////////////////////////
    private STATES mState; // state
    private String mText; // text
    // ///////////////////////////////////////////////////////////////

    private boolean mIsInitialized = false;
    private JsonObject mJsonTestResultPiece;

    public TestResultPiece(String stringTestResultPiece) {
        initTestResultPiece(stringTestResultPiece);
    }

    public TestResultPiece(JsonObject jsonTestResultPiece) {
        initTestResultPiece(jsonTestResultPiece);
        mJsonTestResultPiece = jsonTestResultPiece;
    }

    public String getText() {
        return mText;
    }

    private void setText(String text) {
        mText = text;
    }

    public STATES getState() {
        return mState;
    }

    private void setState(STATES state) {
        mState = state;
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public JsonObject getAsJson() {
        return mJsonTestResultPiece;
    }

    private void initTestResultPiece(String stringTestResultPiece) {
        mIsInitialized = false;

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = (JsonElement) jsonParser.parse(stringTestResultPiece);
            JsonObject jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
            initTestResultPiece(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }

    private void initTestResultPiece(JsonObject jsonTestResultPiece) {
        mIsInitialized = false;

        try {
            if (jsonTestResultPiece == null || jsonTestResultPiece.isJsonNull()) {
                return;
            }

            JsonElement jsonElementText = Tools.jsonElementByMemberName(jsonTestResultPiece, "text");
            setText((jsonElementText != null) ? jsonElementText.getAsString() : "");

            JsonElement jsonElementState = Tools.jsonElementByMemberName(jsonTestResultPiece, "state");
            setState((jsonElementState != null) ? STATES.valueOf(jsonElementState.getAsString()) : STATES.empty);

            mIsInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }
}