package ru.com.cardiomagnil.util;

import java.util.List;

import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.region.Ca_Region;
import ru.com.cardiomagnil.ca_model.region.Ca_RegionDao;
import ru.com.cardiomagnil.ca_model.test.Ca_TestResult;
import ru.com.cardiomagnil.ca_model.test.Ca_TestResultDao;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.token.Ca_TokenDao;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;
import ru.com.cardiomagnil.ca_model.user.Ca_UserLgnPwd;

public class TestMethods {

    public static void testCurrentMethod() {
        Ca_TestResultDaoSendTestSource();
//        Ca_UserDaoGetById();
//        Ca_TokenDaoGetByUserId();
//        Ca_TokenDaoGetByLgnPwd();
//        Ca_UserRegister();
//        Ca_RegionDaoGetAll();
    }

    public static void Ca_TestResultDaoSendTestSource() {
        Ca_TestResultDao.sendTestSource(
                null,
                AppState.getInstatce().getToken(),
                new CallbackOne<Ca_TestResult>() {
                    @Override
                    public void execute(Ca_TestResult testResult) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void Ca_UserDaoGetById() {
        Ca_Token token = new Ca_Token();
        token.setUserId("24");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjE0ODc3MjYsInVzZXJuYW1lIjoiZXRoampmZF9fQGhnLmNvbSIsImlhdCI6IjE0MjE0MDEzMjYifQ.ocQ59hxGZ2fyMWBYU5IY1DBuMr2nqSzug12Qw8MLppDPJV_j5uhOV-UefIv9vd3K692TeIX0PdZt1SuuWKl2UeQ7nBr-VBT6x7BQ_mHeaUSrE45_x5eP6kkDQVH15FmfPsKJdLHFeuGugACOrt16AM92FKujpiW3b4f50F3M5fAjA_sQqEbDGz4yfFYtR-RM6hNGSTpLpeW9IG50h8O18WRralt72wC6A2o9uh7dW6ynUM9QyMQMipKnTC8aTBwX_66ytD-6DS78DwF74__wo2J0D4vNIBeGKP16MpQoY7cRR0q9xGa6XvVf0x9CS88zlNHgEw7nzI541-5u2WY8PqoemDhNBfASDIewVCBc1bwqJ7uRnJvOXgMnQO0-nARndQRmFaJTINGF-oEESbS2sGr0pIMa4jnGTYVtiDTElHOsMDP_GdBnX-c3nx4dfw61JaDW7v5Y57cSvluoZBExRffaXkagr6hAMdhK0-1EIynI11Y6YGWkDNJhyG1XDocADNqxAPAC6kFnnPxMi8ygqbxBG7EXn6Zy2tWz6mnXNfxh5GPG_cB7B4_i2LFQ3s-KmCfzEqoCnSpEmvbFFGkdcq1a_jUbiWDfd4BJ35CXb207LidQBT6WGNZK4NcfditKAxXblFheo8Cgn5p6WBrKChISjW6bLaQxKY6-b78ptG0");
        Ca_UserDao.getByToken(
                token,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User user) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        int t = 1;
                        t++;
                    }
                },
                false
        );
    }

    public static void Ca_TokenDaoGetByUserId() {
        Ca_TokenDao.getByUserId(
                "25",
                new CallbackOne<Ca_Token>() {
                    @Override
                    public void execute(Ca_Token token) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void Ca_TokenDaoGetByLgnPwd() {
        Ca_TokenDao.getByLgnPwd(
                new Ca_UserLgnPwd("ethjjfd@hg.com", "t"),
                new CallbackOne<Ca_Token>() {
                    @Override
                    public void execute(Ca_Token token) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void Ca_UserRegister() {
        Ca_UserDao.register(
                null,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User user) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

    public static void Ca_RegionDaoGetAll() {
        Ca_RegionDao.getAll(
                new CallbackOne<List<Ca_Region>>() {
                    @Override
                    public void execute(List<Ca_Region> regionList) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );
    }

}
