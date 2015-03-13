package ru.com.cardiomagnyl.application;

public class Constants {
    public static int AGE_LIMIT = 21;
    public static int YEARS_RANGE = 50;
    public static int TEST_PERIOD = 30;
//    public static int TEST_PERIOD = 1;

    // bundle parcelable
    public static String TEST_PAGE = "test_page";
    public static String PILL = "pill";
    public static String TEST_SOURCE = "test_source";
    // bundle string
    public static String PAGE_TITLE = "title";
    public static String INSTITUTION_ID = "institution_id";
    // bundle boolean
    public static String IS_NOT_PASSED = "is_not_passed";

    // -------------------------------------
    //                    passed -> work
    //                  /
    // new_user -> test
    //                  \
    //                    error -> test
    // -------------------------------------
    //                      passed -> work
    //                    /
    // new_doctor -> test
    //                    \
    //                      error -> test
    // -------------------------------------
    //                        passed -> work
    //                      /
    // 30_days_user -> test
    //                      \
    //                        error -> work
    // -------------------------------------
    //
    //
    // 30_days_doctor -> work
    //
    //
    // -------------------------------------
}
