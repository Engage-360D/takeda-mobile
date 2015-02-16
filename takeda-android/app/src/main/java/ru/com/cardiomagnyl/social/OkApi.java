package ru.com.cardiomagnyl.social;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class OkApi extends BaseSocialApi {
    private static final String CLIENT_ID = "XXXXXXXX";
    // private static final String APP_KEY = "YYYYYYYYYYYYYYYYY";
    private static final String API_SECRET = "ZZZZZZZZZZZZZZZZZZZZZZZZ";
    // private static final String SCOPE = "PUBLISH_TO_STREAM";
    private static final String SCOPE = "0";
    private static final String CALLBACK_URL = "http://localhost/";

    private static final String HOST_NAME = "http://www.odnoklassniki.ru";
    private static final String AUTHORIZE_URL = HOST_NAME + "/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code&layout=m";
    private static final String SCOPED_AUTHORIZE_URL = String.format("%s&scope=%%s", AUTHORIZE_URL);
    private static final String ACCESS_TOKEN_ENDPOINT = "http://api.odnoklassniki.ru/oauth/token.do";

    private static final String API_BASE_URL = "http://api.odnoklassniki.ru/fb.do?";
    private static final String METHOD_GET_USER_INFO = API_BASE_URL + "application_key=YYYYYYYYYYYYYYYYY&format=json&method=users.getCurrentUser";
    private OAuthService mService;

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

    @Override
    public OAuthService createService(OAuthConfig config) {
        mService = new OkOAuth20Service(this, config);
        return mService;
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

    public Verb getAccessTokenVerb() {
        return Verb.POST;
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
        return METHOD_GET_USER_INFO;
    }

    public String getSecretKey() {
        return API_SECRET;
    }

    public String getApiBaseUrl() {
        return API_BASE_URL;
    }

    @Override
    public String getSpecifiedParams() {
        return "";
    }

    @Override
    public void restoreSpecifiedParams(String params) {
    }
}
