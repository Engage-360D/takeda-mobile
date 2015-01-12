package ru.com.cardiomagnil.model;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.util.Tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Token {
    // ///////////////////////////////////////////////////////////////
    // Fields of Token
    // ///////////////////////////////////////////////////////////////
    private String mAccessToken; // access_token
    private int mExpiresIn; // expires_in
    private String mTokenType; // token_type
    private String mScope; // scope
    private String mRefreshToken; // refresh_token
    // ///////////////////////////////////////////////////////////////

    private boolean mIsInitialized;
    private JsonObject mJsonToken;

    public Token(String stringToken) {
        initToken(stringToken);
    }

    public Token(JsonObject jsonToken) {
        initToken(jsonToken);
        mJsonToken = jsonToken;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    private void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public int getExpiresIn() {
        return mExpiresIn;
    }

    private void setExpiresIn(int expiresIn) {
        mExpiresIn = expiresIn;
    }

    public String getTokenType() {
        return mTokenType;
    }

    private void setTokenType(String tokenType) {
        mTokenType = tokenType;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    private void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
    }

    public String getScope() {
        return mScope;
    }

    private void setScope(String scope) {
        mScope = scope;
    }

    public boolean isInitialized() {
        return mIsInitialized && !mAccessToken.isEmpty();
    }

    public JsonObject getAsJson() {
        return mJsonToken;
    }

    // stringToken may be null
    private void initToken(String stringToken) {
        mIsInitialized = false;

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = (JsonElement) jsonParser.parse(stringToken);
            JsonObject jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
            initToken(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }

    private void initToken(JsonObject jsonToken) {
        mIsInitialized = false;

        try {
            if (jsonToken == null || jsonToken.isJsonNull()) {
                return;
            }

            JsonElement jsonElementAccessToken = Tools.jsonElementByMemberName(jsonToken, "access_token");
            setAccessToken((jsonElementAccessToken != null) ? jsonElementAccessToken.getAsString() : "");

            JsonElement jsonElementExpiresIn = Tools.jsonElementByMemberName(jsonToken, "expires_in");
            setExpiresIn((jsonElementExpiresIn != null) ? jsonElementExpiresIn.getAsInt() : 0);

            JsonElement jsonElementTokenType = Tools.jsonElementByMemberName(jsonToken, "token_type");
            setTokenType((jsonElementTokenType != null) ? jsonElementTokenType.getAsString() : "");

            JsonElement jsonElementScope = Tools.jsonElementByMemberName(jsonToken, "scope");
            setScope((jsonElementScope != null) ? jsonElementScope.getAsString() : "");

            JsonElement jsonRefreshToken = Tools.jsonElementByMemberName(jsonToken, "refresh_token");
            setRefreshToken((jsonRefreshToken != null) ? jsonRefreshToken.getAsString() : "");

            mIsInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }
}