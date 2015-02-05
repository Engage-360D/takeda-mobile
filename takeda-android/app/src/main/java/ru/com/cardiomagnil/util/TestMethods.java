package ru.com.cardiomagnil.util;

import java.util.List;

import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.model.common.Dummy;
import ru.com.cardiomagnil.model.common.Email;
import ru.com.cardiomagnil.model.common.LgnPwd;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.model.region.Region;
import ru.com.cardiomagnil.model.region.RegionDao;
import ru.com.cardiomagnil.model.test.TestResult;
import ru.com.cardiomagnil.model.test.TestResultDao;
import ru.com.cardiomagnil.model.token.Token;
import ru.com.cardiomagnil.model.token.TokenDao;
import ru.com.cardiomagnil.model.user.User;
import ru.com.cardiomagnil.model.user.UserDao;

public class TestMethods {

    public static void testCurrentMethod() {
        Ca_UserDaoResetPassword();
//        Ca_TestResultDaoSendTestSource();
//        Ca_UserDaoGetById();
//        Ca_TokenDaoGetByUserId();
//        Ca_TokenDaoGetByLgnPwd();
//        Ca_UserRegister();
//        Ca_RegionDaoGetAll();
    }

    public static void Ca_UserDaoResetPassword() {
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


    public static void Ca_TestResultDaoSendTestSource() {
        TestResultDao.sendTestSource(
                null,
                AppState.getInstatce().getToken(),
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

    public static void Ca_UserDaoGetById() {
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

    public static void Ca_TokenDaoGetByUserId() {
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

    public static void Ca_TokenDaoGetByLgnPwd() {
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

    public static void Ca_UserRegister() {
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

    public static void Ca_RegionDaoGetAll() {
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
