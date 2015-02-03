package ru.com.cardiomagnil.ca_api;

import java.util.HashMap;

public final class Url {
    public static final String API_SCHEME = "http://";
    // TODO: change host after test
    public static final String API_HOST = "cardiomagnyl.dev.iengage.ru";
    public static final String API_PREFIX = "/api/v1/";
    public static final String API_ADDR = API_SCHEME + API_HOST + API_PREFIX;

    public static final String USERS = API_ADDR + "users";
    public static final String ACCOUNT = API_ADDR + "account";
    public static final String ACCOUNT_UPDATE = API_ADDR + "account?token=%s";
    public static final String ACCOUNT_RESET_PASSWORD = API_ADDR + "account/reset-password";
    public static final String ACCOUNT_TEST_RESULTS = API_ADDR + "account/test-results?token=%s";
    public static final String TOKENS = API_ADDR + "tokens";
    public static final String REGIONS = API_ADDR + "regions";

    public static final HashMap<String, String> POST_HEADERS = new HashMap<String, String>() {{
        put("Content-Type", "application/vnd.api+json");
    }};

    public static final String LOCAL = "local/";
    public static final String BANNER_CHOOSE_MEDICAL_INSTITUTION = LOCAL + "choose_medical_institution";
    public static final String BANNER_PASS_POLL = LOCAL + "pass_poll";

}
