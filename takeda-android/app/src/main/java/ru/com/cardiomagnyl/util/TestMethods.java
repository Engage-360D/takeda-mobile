package ru.com.cardiomagnyl.util;

import java.util.List;

import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Email;
import ru.com.cardiomagnyl.model.common.LgnPwd;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.region.Region;
import ru.com.cardiomagnyl.model.region.RegionDao;
import ru.com.cardiomagnyl.model.test.PageDao;
import ru.com.cardiomagnyl.model.test.TestPage;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestResultDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.token.TokenDao;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;

public class TestMethods {

    public static void testCurrentMethod() {
        TestPageDaoGetByLink();
//        TestResultDaoGetAll();
//        UserDaoResetPassword();
//        TestResultDaoSendTestSource();
//        UserDaoGetById();
//        TokenDaoGetByUserId();
//        TokenDaoGetByLgnPwd();
//        UserRegister();
//        RegionDaoGetAll();
    }

    public static void TestPageDaoGetByLink() {
        Token token = new Token();
        token.setUserId("45");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjM1NjU4NTksInVzZXJuYW1lIjoieS5hbmRyZXlrbyszQGdtYWlsLmNvbSIsImlhdCI6IjE0MjM0Nzk0NTkifQ.AFvbLYoQF8FAXu0onhj5O-6kD7sYdzym-zv5dwNk86s1Bq0HXE_ehYNIcaNfl2otMI3y6zehDCG2LCo9FjA-ZiaXzywO_Lsyu8tKlP1nl8109k4iORfzejISuz81yyRO6js5V9pN8WJ62erKF0LpmQzMJNjhx7QGWpoYII69A7hH2OcmhNsX5iqn_y5G1hvIVfQDiK5iCKy3Qq_ix6ZmIGOhg4xP8kDlR9HbuCS8AlJcZ6p1TfonQGxqJvGefvsh8DVU0uNaMerV6wAucbYePXBZ7vHPB3sUwT9OwV4TMB425XbdGACrRQF5lWe31ByPjefJA-Czk7YZqaDn8umD2PZGHF3Z-CppTgvNjZU6YuaBxoviCDdLE9fyXJqWEmfo0mmwshpbaTMZ-Pk0jewwRzMs2hVdXXBgWei3nULpDo9UFCotfoBFQjzN4tHuFv9QPtq_cfnfbSpn8uXf7-dQw9C1rs2LMRA6YGfiD1K0390-PY8hw5UWywWZ-Hm3Jg-537cp9pv_zbnEicNpPL4V3569XU8Svzyvrx0WUjYyIFaIy0-JRZRad5wszLwOFM2AO3HPe-MnkuFaRDUAbA5r3UGC8HkhzxfQ1FsEGOYYG6hnGdHSKdB-fvz-uURJMdM3FeUD_TyS1aTE1qdfDS5pa4zP5obWvQjTop6-fVyu7T0");
        PageDao.getByLink(
                "/api/v1/account/test-results/21/pages/physicalActivityMinutes",
                token,
                new CallbackOne<TestPage>() {
                    @Override
                    public void execute(TestPage testResultList) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void TestResultDaoGetAll() {
        Token token = new Token();
        token.setUserId("45");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjMyNjE5NjYsInVzZXJuYW1lIjoieS5hbmRyZXlrbyszQGdtYWlsLmNvbSIsImlhdCI6IjE0MjMxNzU1NjYifQ.prOz1gQGOCb8nRjL8nHVOLEQ-9tFBj7OlF7Q6W0U2piUHo-cVyzpSof_SrtOXNvducZEBvNx5EjdOD8FwGCJeVw7z-kltTPQDVywv-O5XHbAAT7JAjjpMP6dIQSEC5BC-dFat58q0Id1faRCSd17xZLxeX2fcFcD-lnGJJhzcYt_luwdNNPbC42kBGD-lVyj2dFL7Zrq_2-EPZy7oFpVvPLvcJP4jL-j2Hqk1-gkhFj7HiMfpfXp1kqRUNv5ZwZUDbAC0hRrs37pc9ALLEwERbjX0Hi-tclHVzmg4lYbf3JBePT0wzwnlRce2lDxZaHOQ9bK2PnnZeLXFivpvSTbF8HXfVB474-q4CLGTqXd2O1ZJffiKVaPAktzWeUrXXw8PWNoMhDEpwVGim7pR6GOiHZYYweltgL0IeIEXgurgFfo2tCrl2W_1qc3bgz3EaRkWd4uZ9Y7Ajzg5mUCZiy7WSLvPv0La32zrP36UhRetNIZWH3tOe7969q9YICCSP68OGMvSWUjIpJKtIo-hg7d1AATw-zwYTmCkxFJW3l1o0hgASVsqUzIs_ErbtOZ8Gv3VHtmEDCZKtJ_2_QlbX5q0KrJlpFa109DmIfZmQk02DAbTLPV5kv3TZfyOE4tu_wKd4eDM15g4tYjb2XGAEWCxxop4MOLUOVlH3rF1KO1Fi0");
        TestResultDao.getAll(
                token,
                new CallbackOne<List<TestResult>>() {
                    @Override
                    public void execute(List<TestResult> testResultList) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void UserDaoResetPassword() {
        Email email = new Email();
        email.setEmail("y.andreyko+11@gmail.com");

        UserDao.resetPassword(
                email,
                new CallbackOne<List<Dummy>>() {
                    @Override
                    public void execute(List<Dummy> dummy) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }


    public static void TestResultDaoSendTestSource() {
        TestResultDao.sendTestSource(
                null,
                AppState.getInsnatce().getToken(),
                new CallbackOne<TestResult>() {
                    @Override
                    public void execute(TestResult testResult) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void UserDaoGetById() {
        Token token = new Token();
        token.setUserId("24");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjE0ODc3MjYsInVzZXJuYW1lIjoiZXRoampmZF9fQGhnLmNvbSIsImlhdCI6IjE0MjE0MDEzMjYifQ.ocQ59hxGZ2fyMWBYU5IY1DBuMr2nqSzug12Qw8MLppDPJV_j5uhOV-UefIv9vd3K692TeIX0PdZt1SuuWKl2UeQ7nBr-VBT6x7BQ_mHeaUSrE45_x5eP6kkDQVH15FmfPsKJdLHFeuGugACOrt16AM92FKujpiW3b4f50F3M5fAjA_sQqEbDGz4yfFYtR-RM6hNGSTpLpeW9IG50h8O18WRralt72wC6A2o9uh7dW6ynUM9QyMQMipKnTC8aTBwX_66ytD-6DS78DwF74__wo2J0D4vNIBeGKP16MpQoY7cRR0q9xGa6XvVf0x9CS88zlNHgEw7nzI541-5u2WY8PqoemDhNBfASDIewVCBc1bwqJ7uRnJvOXgMnQO0-nARndQRmFaJTINGF-oEESbS2sGr0pIMa4jnGTYVtiDTElHOsMDP_GdBnX-c3nx4dfw61JaDW7v5Y57cSvluoZBExRffaXkagr6hAMdhK0-1EIynI11Y6YGWkDNJhyG1XDocADNqxAPAC6kFnnPxMi8ygqbxBG7EXn6Zy2tWz6mnXNfxh5GPG_cB7B4_i2LFQ3s-KmCfzEqoCnSpEmvbFFGkdcq1a_jUbiWDfd4BJ35CXb207LidQBT6WGNZK4NcfditKAxXblFheo8Cgn5p6WBrKChISjW6bLaQxKY6-b78ptG0");
        UserDao.getByToken(
                token,
                new CallbackOne<User>() {
                    @Override
                    public void execute(User user) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                },
                false
        );
    }

    public static void TokenDaoGetByUserId() {
        TokenDao.getByUserId(
                "25",
                new CallbackOne<Token>() {
                    @Override
                    public void execute(Token token) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void TokenDaoGetByLgnPwd() {
        TokenDao.getByLgnPwd(
                new LgnPwd("ethjjfd@hg.com", "t"),
                new CallbackOne<Token>() {
                    @Override
                    public void execute(Token token) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void UserRegister() {
        UserDao.register(
                null,
                new CallbackOne<User>() {
                    @Override
                    public void execute(User user) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void RegionDaoGetAll() {
        RegionDao.getAll(
                new CallbackOne<List<Region>>() {
                    @Override
                    public void execute(List<Region> regionList) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

}
