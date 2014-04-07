package com.cardiomagnil.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;

public class Authorization {
    // ///////////////////////////////////////////////////////////////
    // Fields of User
    // ///////////////////////////////////////////////////////////////
    private String mClientId = null;
    private String mClientSecret = null;
    private String mGrantType = null;
    private String mUsername = null;
    private String mPassword = null;

    // ///////////////////////////////////////////////////////////////

    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        mClientSecret = clientSecret;
    }

    public void setGrantType(String grantType) {
        mGrantType = grantType;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public JsonObject asJson() {
        if (!validate()) {
            return null;
        }

        JsonObject jsonObjectAuthorization = new JsonObject();

        jsonObjectAuthorization.addProperty("client_id", mClientId);
        jsonObjectAuthorization.addProperty("client_secret", mClientSecret);
        jsonObjectAuthorization.addProperty("grant_type", mGrantType);
        jsonObjectAuthorization.addProperty("username", mUsername);
        jsonObjectAuthorization.addProperty("password", mPassword);

        return jsonObjectAuthorization;
    }

    public List<NameValuePair> asValuePairs() {
//        if (!validate()) {
//            return null;
//        }

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("client_id", mClientId));
        nameValuePairs.add(new BasicNameValuePair("client_secret", mClientSecret));
        nameValuePairs.add(new BasicNameValuePair("grant_type", mGrantType));
        nameValuePairs.add(new BasicNameValuePair("username", mUsername));
        nameValuePairs.add(new BasicNameValuePair("password", mPassword));

        return nameValuePairs;
    }

    /**
     * All Authorization's fields != null
     */
    public boolean validate() {
        boolean result = false;

        result = (mClientId != null) &&
        /*     */(mClientSecret != null) &&
        /*     */(mGrantType != null) &&
        /*     */(mUsername != null) &&
        /*     */(mPassword != null);

        return result;
    }

    @SuppressWarnings("unused")
    private void testAuthorization() {
        Authorization authorization = new Authorization();
        authorization.setClientId("X_XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        authorization.setClientSecret("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
        authorization.setGrantType("password");
        authorization.setUsername("admin");
        authorization.setPassword("password");

        boolean isValid = authorization.validate();
        JsonObject jsonObject = authorization.asJson();

    }
}