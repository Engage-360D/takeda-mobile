package ru.com.cardiomagnyl.api;

import java.util.HashMap;

public final class Url {
    public static final String API_SCHEME = "http://";
    // TODO: change host after test
    public static final String API_HOST = "cardiomagnyl.dev.iengage.ru";
    public static final String API_PREFIX = "/api/v1/";
    public static final String API_ADDR = API_SCHEME + API_HOST + API_PREFIX;

    public static final String USERS = API_ADDR + "users";
    public static final String ACCOUNT = API_ADDR + "account";
    public static final String ACCOUNT_RESET = API_ADDR + "account/reset?token=%s";
    public static final String ACCOUNT_UPDATE = API_ADDR + "account?token=%s";
    public static final String ACCOUNT_RESET_PASSWORD = API_ADDR + "account/reset-password";
    public static final String ACCOUNT_TEST_RESULTS = API_ADDR + "account/test-results?token=%s";
    public static final String ACCOUNT_TEST_SEND_EMAIL = API_ADDR + "account/test-results/%s/send-email?token=%s";
    public static final String ACCOUNT_ISR = API_ADDR + "account/isr?token=%s";
    public static final String ACCOUNT_PILLS = API_ADDR + "account/pills?token=%s";
    public static final String ACCOUNT_PILLS_ID = API_ADDR + "account/pills/%s?token=%s";
    public static final String ACCOUNT_TIMELINE = API_ADDR + "account/timeline?token=%s";
    public static final String ACCOUNT_TIMELINE_TASKS = API_ADDR + "account/timeline/tasks/%s?token=%s";
    public static final String ACCOUNT_TEST_RECOMMENDATIONS = API_ADDR + "account/test-results/%s/pages/%s?token=%s";
    public static final String ACCOUNT_TEST_RESULTS_DIET_QUESTIONS = API_ADDR + "account/test-results/%s/diet-questions?token=%s";
    public static final String ACCOUNT_TEST_RESULTS_DIET_RECOMMENDATIONS = API_ADDR + "account/test-results/%s/diet-recommendations?%s&token=%s";
    public static final String ACCOUNT_TEST_RESULTS_DIET_ANSWERS = "answers[%s]=%s&";
    public static final String ACCOUNT_INCIDENTS = API_ADDR + "account/incidents?token=%s";
    public static final String TOKENS = API_ADDR + "tokens";
    public static final String TOKENS_GP = API_ADDR + "tokens/google";
    public static final String TOKENS_FB = API_ADDR + "tokens/facebook";
    public static final String TOKENS_VK = API_ADDR + "tokens/vk";
    public static final String TOKENS_OK = API_ADDR + "tokens/odnoklassniki";
    public static final String REGIONS = API_ADDR + "regions";
    public static final String INSTITUTION_PARSED_TOWNS = API_ADDR + "institution-parsed-towns";
    public static final String INSTITUTION_PARSED_TOWNS_TOWN = API_ADDR + "institution-parsed-towns/%s,%s";
    public static final String INSTITUTION_SPECIALIZATIONS = API_ADDR + "institution-specializations";
    public static final String INSTITUTIONS = API_ADDR + "institutions?parsedTown=%s&specialization=%s";

    public static final String ACCOUNT_REPORTS = "http://cardiomagnyl.ru/account/reports";
    public static final String SHARE_LINK = "http://cardiomagnyl.dev.iengage.ru/good-to-know?blockId=%s#%s";
    public static final String SHARE_LINK_GP = "http://cardiomagnyl.dev.iengage.ru/good-to-know/%s?gplus=1#%s";

    public static final HashMap<String, String> GET_HEADERS = new HashMap<String, String>() {{
        put("Content-Type", "application/vnd.api+json");
        put("Accept", "*/*");
    }};

    public static final HashMap<String, String> POST_HEADERS = new HashMap<String, String>() {{
        put("Content-Type", "application/vnd.api+json");
        put("Accept", "*/*");
    }};

    public static final HashMap<String, String> PUT_HEADERS = new HashMap<String, String>() {{
        put("Content-Type", "application/vnd.api+json");
        put("Accept", "*/*");
    }};

    public static final HashMap<String, String> DELETE_HEADERS = new HashMap<String, String>() {{
        // add headers if need
    }};

    public static final String LOCAL = "local/";
    public static final String BANNER_CHOOSE_MEDICAL_INSTITUTION = LOCAL + "choose_medical_institution";
    public static final String BANNER_PASS_POLL = LOCAL + "pass_poll";

}
