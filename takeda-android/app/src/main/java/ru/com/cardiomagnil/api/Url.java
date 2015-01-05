package ru.com.cardiomagnil.api;

public final class Url {
    public static final String API_SCHEME = "http://";
    // TODO: change host after test
    public static final String API_HOST = "cardiomagnyl.dev.iengage.ru";
    public static final String API_PREFIX = "/api/v1/";
    public static final String API_ADDR = API_SCHEME + API_HOST + API_PREFIX;

    public static final String USERS = API_ADDR + "users";
    public static final String TOKENS = API_ADDR + "tokens";

    // TODO: remove after tests
    // private final String REQUEST_TEST_RESULTS = mAppServerApiURL + "/test-results/";
    // private final String REQUEST_RESET_PASSWORD = mAppServerApiURL + "/users/reset";
}
