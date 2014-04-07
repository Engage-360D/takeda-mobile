package com.cardiomagnil.api;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.cardiomagnil.application.AppConfig;
import com.cardiomagnil.model.Authorization;
import com.cardiomagnil.model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Api {
    private final String mAppServerOAuthURL = /*                                                            */
    AppConfig.APPLICATION_SERVER_SCHEMA + /*                                                                */
    "://" + /*                                                                                              */
    AppConfig.APPLICATION_SERVER_HOST + /*                                                                  */
    (AppConfig.APPLICATION_SERVER_PORT.equals("80") ? "" : ":" + AppConfig.APPLICATION_SERVER_PORT) + /*    */
    "/" + /*                                                                                                */
    AppConfig.APPLICATION_SERVER_OAUTH;

    private final String mAppServerApiURL = /*                                                              */
    AppConfig.APPLICATION_SERVER_SCHEMA + /*                                                                */
    "://" + /*                                                                                              */
    AppConfig.APPLICATION_SERVER_HOST + /*                                                                  */
    (AppConfig.APPLICATION_SERVER_PORT.equals("80") ? "" : ":" + AppConfig.APPLICATION_SERVER_PORT) + /*    */
    "/" + /*                                                                                                */
    AppConfig.APPLICATION_SERVER_API;

    private final String REQUEST_AUTHORIZATION = mAppServerOAuthURL + "/token";
    private final String REQUEST_REGISTRATION = mAppServerApiURL + "/users";

    public JsonObject userAuthorization(Authorization authorization) {
        return performGet(REQUEST_AUTHORIZATION, authorization.asValuePairs());
    }

    public JsonObject userRegistration(User user) {
        return performPost(REQUEST_REGISTRATION, user.asJson());
    }

    private JsonObject performGet(String url, List<NameValuePair> nameValuePairs) {
        JsonObject jsonObjectResponse = null;

        try {
            String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            String fullUrl = url + "?" + paramString;
            HttpGet request = new HttpGet(fullUrl);
            CustomHttpClient http = new CustomHttpClient();
            HttpResponse response = http.execute(request);
            jsonObjectResponse = responseToJsonObject(response);

        } catch (Exception e) {
            // TODO: exception_handler
            e.printStackTrace();
            // ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getContext(), e);
        }

        return jsonObjectResponse;
    }

    private JsonObject performPost(String url, JsonObject jsonObjectParams) {
        JsonObject jsonObjectResponse = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            httpPost.setHeader("Accept", "*/*");
            httpPost.setEntity(new StringEntity(jsonObjectParams.toString()));
            CustomHttpClient http = new CustomHttpClient();
            HttpResponse response = http.execute(httpPost);
            jsonObjectResponse = responseToJsonObject(response);
        } catch (Exception e) {
            // TODO: exception_handler
            e.printStackTrace();
            // ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getContext(), e);
        }

        return jsonObjectResponse;
    }

    private JsonObject responseToJsonObject(HttpResponse response) {
        JsonObject jsonObject = null;

        try {
            String stringResponse = EntityUtils.toString(response.getEntity());
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = (JsonElement) jsonParser.parse(stringResponse);
            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
        } catch (Exception e) {
            // TODO: exception_handler
            e.printStackTrace();
            // ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getContext(), e);

        }

        return jsonObject;
    }

}