package ru.com.cardiomagnil.api;

import java.util.HashMap;

public final class Url {
    public static final String API_SCHEME = "http://";
    // TODO: change host after test
    public static final String API_HOST = "cardiomagnyl.dev.iengage.ru";
    public static final String API_PREFIX = "/api/v1/";
    public static final String API_ADDR = API_SCHEME + API_HOST + API_PREFIX;

    public static final String USERS = API_ADDR + "users";
    public static final String TOKENS = API_ADDR + "tokens";
    public static final String REGIONS = API_ADDR + "regions";


    public static final HashMap<String, String> POST_HEADERS = new HashMap<String, String>() {{
        put("Content-Type", "application/vnd.api+json");
    }};
}
