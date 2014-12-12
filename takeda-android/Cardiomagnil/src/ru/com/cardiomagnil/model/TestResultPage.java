package ru.com.cardiomagnil.model;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.application.Tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestResultPage {
    // ///////////////////////////////////////////////////////////////
    // Fields of TestResultPage
    // ///////////////////////////////////////////////////////////////
    private boolean mInstitutionsUrl; // institutionsUrl
    private String mSubtitle; // subtitle
    private String mText; // text
    private String mTitle; // title
    private String mUrlText; // urlText
    // ///////////////////////////////////////////////////////////////

    private boolean mIsInitialized = false;
    private JsonObject mJsonTestResultPage;

    public TestResultPage(String stringTestResultPage) {
        initTestResultPage(stringTestResultPage);
    }

    public TestResultPage(JsonObject jsonTestResultPage) {
        initTestResultPage(jsonTestResultPage);
        mJsonTestResultPage = jsonTestResultPage;
    }

    public boolean getInstitutionsUrl() {
        return mInstitutionsUrl;
    }

    private void setInstitutionsUrl(boolean institutionsUrl) {
        mInstitutionsUrl = institutionsUrl;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    private void setSubtitle(String Subtitle) {
        mSubtitle = Subtitle;
    }

    public String getText() {
        return mText;
    }

    private void setText(String text) {
        mText = text;
    }

    public String getTitle() {
        return mTitle;
    }

    private void setTitle(String title) {
        mTitle = title;
    }

    public String getUrlText() {
        return mUrlText;
    }

    private void setUrlText(String UrlText) {
        mUrlText = UrlText;
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public JsonObject getAsJson() {
        return mJsonTestResultPage;
    }

    private void initTestResultPage(String stringTestResultPage) {
        mIsInitialized = false;

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = (JsonElement) jsonParser.parse(stringTestResultPage);
            JsonObject jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
            initTestResultPage(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }

    private void initTestResultPage(JsonObject jsonTestResultPage) {
        mIsInitialized = false;

        try {
            if (jsonTestResultPage == null || jsonTestResultPage.isJsonNull()) {
                return;
            }

            JsonElement jsonElementInstitutionsUrl = Tools.jsonElementByMemberName(jsonTestResultPage, "institutionsUrl");
            setInstitutionsUrl((jsonElementInstitutionsUrl != null) ? jsonElementInstitutionsUrl.getAsBoolean() : false);

            JsonElement jsonElementSubtitle = Tools.jsonElementByMemberName(jsonTestResultPage, "subtitle");
            setSubtitle((jsonElementSubtitle != null) ? jsonElementSubtitle.getAsString() : "");

            JsonElement jsonElementText = Tools.jsonElementByMemberName(jsonTestResultPage, "text");
            setText((jsonElementText != null) ? jsonElementText.getAsString() : "");

            JsonElement jsonElementTitle = Tools.jsonElementByMemberName(jsonTestResultPage, "title");
            setTitle((jsonElementTitle != null) ? jsonElementTitle.getAsString() : "");

            JsonElement jsonElementUrlText = Tools.jsonElementByMemberName(jsonTestResultPage, "urlText");
            setUrlText((jsonElementUrlText != null) ? jsonElementUrlText.getAsString() : "");

            mIsInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }
}