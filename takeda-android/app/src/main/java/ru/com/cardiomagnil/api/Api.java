package ru.com.cardiomagnil.api;

import android.util.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

import ru.com.cardiomagnil.application.AppConfig;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.model.TestIncoming;
import ru.com.cardiomagnil.model.Token;

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
    private final String REQUEST_TEST_RESULTS = mAppServerApiURL + "/test-results/";
    private final String REQUEST_RESET_PASSWORD = mAppServerApiURL + "/users/reset";

    public JsonObject userAuthorization(Authorization authorization) {
        return performGet(REQUEST_AUTHORIZATION, authorization.asValuePairs());
    }

    public JsonObject restorePassword(String email) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", email);
        return performPost(REQUEST_RESET_PASSWORD, jsonObject, null);
    }

    public JsonObject testResults(TestIncoming testIncoming, Token token) {
        BasicHeader header = new BasicHeader("Authorization", "Bearer " + token.getAccessToken());
        return performPost(REQUEST_TEST_RESULTS, testIncoming.getAsJson(), new Header[] { header });
    }

    private JsonObject performGet(String url, List<NameValuePair> nameValuePairs) {
        JsonObject jsonObjectResponse = null;

        try {
            String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
            String fullUrl = url + "?" + paramString;
            HttpGet request = new HttpGet(fullUrl);
//            CustomHttpClient http = new CustomHttpClient();
//            HttpResponse response = http.execute(request);
//            jsonObjectResponse = responseToJsonObject(response);

        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }

        return jsonObjectResponse;
    }

    private JsonObject performPost(String url, JsonObject jsonObjectParams, Header[] headers) {
        JsonObject jsonObjectResponse = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeaders(headers);
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            httpPost.setHeader("Accept", "*/*");
            httpPost.setEntity(new StringEntity(jsonObjectParams.toString()));
//            CustomHttpClient http = new CustomHttpClient();
//            HttpResponse response = http.execute(httpPost);
//            jsonObjectResponse = responseToJsonObject(response);
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }

        return jsonObjectResponse;
    }

    private JsonObject responseToJsonObject(HttpResponse response) {
        JsonObject jsonObject = null;

        try {
            String stringResponse = EntityUtils.toString(response.getEntity());

            // TODO: кослыль, попросить переписать АПИ!
            if (stringResponse.endsWith("[]")) {
                stringResponse = "{}";
            }

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = (JsonElement) jsonParser.parse(stringResponse);
            jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }

        return jsonObject;
    }

    @SuppressWarnings("unused")
    private String stringToBase64(String string) {
        String base64 = "";
        try {
            byte[] data = string.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }

        return base64;
    }

}