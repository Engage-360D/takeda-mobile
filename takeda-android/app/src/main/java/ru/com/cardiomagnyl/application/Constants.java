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

    public final static class Growth {
        public final static int MIN = 30;
        public final static int MAX = 300;
        public final static int INIT = 170;
    }


    public final static class Weight {
        public final static int MIN = 30;
        public final static int MAX = 700;
        public final static int INIT = 70;
    }

    public final static class CholesterolLevel {
        public final static int MIN = 3;
        public final static int MAX = 9;
        public final static int INIT = 3;
    }

    public final static class ArterialPressure {
        public final static int MIN = 80;
        public final static int MAX = 200;
        public final static int INIT = 120;
    }

    public final static class PhysicalActivity {
        public final static int MIN = 80;
        public final static int MAX = 200;
        public final static int INIT = 120;
    }

}
