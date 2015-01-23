package ru.com.cardiomagnil.social;

import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class FbApi extends BaseSocialApi {

    private static final String CLIENT_ID = "247535765433542";
    private static final String API_SECRET = "24f49fc172289ea6b391b58848be0a1d";
    private static final String SCOPE = "basic_info, email, user_birthday, user_location";

    private static final String REDIRECT_URL = "fbconnect://success";
    private static final String AUTHORIZE_URL = "https://m.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s&display=touch";
    private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
    private static final String METHOD_GET_USER_INFO = "https://graph.facebook.com/me";
    private OAuthService mService;

    @Override
    public String getAccessTokenEndpoint() {
        return "https://graph.facebook.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");

        // Append scope if present
        if (config.hasScope()) {
            return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
        } else {
            return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
        }
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        mService = new OAuth20ServiceImpl(this, config);
        return mService;
    }

    @Override
    public String getClientId() {
        return CLIENT_ID;
    }

    @Override
    public String getApiSecret() {
        return API_SECRET;
    }

    @Override
    public String getScope() {
        return SCOPE;
    }

    @Override
    public String getCallbackUrl() {
        return REDIRECT_URL;
    }

    @Override
    public String getMethodUserInfoUrl() {
        return METHOD_GET_USER_INFO;
    }

    @Override
    public String getSpecifiedParams() {
        return "";
    }

    @Override
    public void restoreSpecifiedParams(String params) {
    }
}
