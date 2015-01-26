package ru.com.cardiomagnil.ca_api;

import java.util.HashMap;

public final class Url {
    public static final String API_SCHEME = "http://";
    // TODO: change host after test
    public static final String API_HOST = "cardiomagnyl.dev.iengage.ru";
    public static final String API_PREFIX = "/api/v1/";
    public static final String API_ADDR = API_SCHEME + API_HOST + API_PREFIX;

    public static final String USERS = API_ADDR + "users";
    public static final String USERS_ID = API_ADDR + "users/%s";
    public static final String ACCOUNT_RESET_PASSWORD = API_ADDR + "account/reset-password";
    public static final String ACCOUNT_TEST_RESULTS = API_ADDR + "account/test-results?token=%s";
    public static final String TOKENS = API_ADDR + "tokens";
    public static final String REGIONS = API_ADDR + "regions";

    public static final HashMap<String, String> POST_HEADERS = new HashMap<String, String>() {{
        put("Content-Type", "application/vnd.api+json");
    }};

    // TODO:
    // /test-results
    // /test-results /{id}
    // /test-results /{id} /send-email

    // /account - PUT
    // /account /reset-password - POST
    // /account/test-results - GET, POST

}
