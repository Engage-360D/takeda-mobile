package ru.com.cardiomagnil.social;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Token;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class VkApi extends BaseSocialApi implements AccessTokenExtractor {
    private static final String CLIENT_ID = "4289667";
    private static final String API_SECRET = "st2P1mHyD7y95lrKJkFR";
    private static final String SCOPE = "notify";
    private static final String CALLBACK_URL = "https://oauth.vk.com/blank.html";

    private static final String HOST_NAME = "https://api.vk.com";
    private static final String AUTHORIZE_URL = HOST_NAME + "/oauth/authorize?client_id=%s&redirect_uri=%s&display=touch&response_type=code";
    private static final String SCOPED_AUTHORIZE_URL = String.format("%s&scope=%%s", AUTHORIZE_URL);
    private static final String ACCESS_TOKEN_ENDPOINT = HOST_NAME + "/oauth/access_token";
    private static final String USER_INFO_DESIRED_FIELDS = "first_name,last_name,sex,bdate,city,contacts";
    private static final String METHOD_GET_USER_INFO = HOST_NAME + "/method/users.get?uids=%user_id%&fields=" + USER_INFO_DESIRED_FIELDS;
    private static final Pattern USER_ID_PATTERN = Pattern.compile("\"user_id\":\\s*(\\d+)");
    private int mUserId = -1;
    private OAuthService mService;

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return this;
    }

    @Override
    public Token extract(String response) {
        Matcher matcher = USER_ID_PATTERN.matcher(response);
        if (matcher.find())
            mUserId = Integer.parseInt(matcher.group(1));
        else
            throw new OAuthException("Cannot extract an user id. Response was: " + response);

        return new JsonTokenExtractor().extract(response);
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Valid url is required for a callback. Vkontakte does not support OOB");

        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getClientId() {
        return CLIENT_ID;
    }

    @Override
    public String getScope() {
        return SCOPE;
    }

    @Override
    public String getCallbackUrl() {
        return CALLBACK_URL;
    }

    @Override
    public String getApiSecret() {
        return API_SECRET;
    }

    @Override
    public String getMethodUserInfoUrl() {
        return METHOD_GET_USER_INFO.replace("%user_id%", Integer.toString(mUserId));
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        mService = new OAuth20ServiceImpl(this, config);
        ;
        return mService;
    }

    @Override
    public String getSpecifiedParams() {
        JSONObject json = new JSONObject();
        try {
            json.put("user_id", mUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public void restoreSpecifiedParams(String params) {
        try {
            JSONObject json = new JSONObject(params);
            mUserId = json.getInt("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
