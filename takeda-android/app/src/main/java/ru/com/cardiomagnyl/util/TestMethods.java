package ru.com.cardiomagnyl.util;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.user.Email;
import ru.com.cardiomagnyl.model.user.LgnPwd;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.institution.Institution;
import ru.com.cardiomagnyl.model.institution.InstitutionDao;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.region.Region;
import ru.com.cardiomagnyl.model.region.RegionDao;
import ru.com.cardiomagnyl.model.specialization.Specialization;
import ru.com.cardiomagnyl.model.specialization.SpecializationDao;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.task.TaskDao;
import ru.com.cardiomagnyl.model.test.PageDao;
import ru.com.cardiomagnyl.model.test.TestPage;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestResultDao;
import ru.com.cardiomagnyl.model.test_diet.TestDiet;
import ru.com.cardiomagnyl.model.test_diet.TestDietDao;
import ru.com.cardiomagnyl.model.test_diet.TestDietResult;
import ru.com.cardiomagnyl.model.test_diet.TestDietResultDao;
import ru.com.cardiomagnyl.model.timeline.Timeline;
import ru.com.cardiomagnyl.model.timeline.TimelineDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.token.TokenDao;
import ru.com.cardiomagnyl.model.town.Town;
import ru.com.cardiomagnyl.model.town.TownDao;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;

public class TestMethods {

    public static void testCurrentMethod() {
        InstitutionDaoGetByTownAndSpec();
//        SpecializationDaoGetAll();
//        TownDaoGetAll();
//        TestDietResultDaogetByTestId();
//        TestDietResultDaoSendTestDietSource();
//        TestDietDaoGetByTesftId();
//        TaskDaoUpdate();
//        PillDaoGetAll();
//        TimelineDaoGetAll();
//        TestPageDaoGetByLink();
//        TestResultDaoGetAll();
//        UserDaoResetPassword();
//        TestResultDaoSendTestSource();
//        UserDaoGetById();
//        TokenDaoGetByUserId();
//        TokenDaoGetByLgnPwd();
//        UserRegister();
//        RegionDaoGetAll();
    }

    public static void InstitutionDaoGetByTownAndSpec() {
        InstitutionDao.getByTownAndSpec(
                "Москва",
                "Кардиология",
                new CallbackOne<List<Institution>>() {
                    @Override
                    public void execute(List<Institution> InstitutionsList) {
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

    public static void SpecializationDaoGetAll() {
        SpecializationDao.getAll(
                new CallbackOne<List<Specialization>>() {
                    @Override
                    public void execute(List<Specialization> specializationsList) {
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

    public static void TownDaoGetAll() {
        TownDao.getAll(
                new CallbackOne<List<Town>>() {
                    @Override
                    public void execute(List<Town> townsList) {
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

    public static void TestDietResultDaogetByTestId() {
        String testId = "36";

        TestDietResultDao.getByTestId(
                testId,
                new CallbackOne<TestDietResult>() {
                    @Override
                    public void execute(TestDietResult testDietResult) {
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

    public static void TestDietResultDaoSendTestDietSource() {
        String testDietSource = "answers[1]=1&answers[2]=2&answers[3]=3&answers[4]=1&answers[5]=2&answers[6]=3&answers[7]=1&";
        String testId = "36";

        Token token = new Token();
        token.setUserId("63");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjU0MTY2MjEsInVzZXJuYW1lIjoieS5hbmRyZXlrbysxN0BnbWFpbC5jb20iLCJpYXQiOiIxNDI1MzMwMjIxIn0.Uu2yPIDviHtYCgewBAl13OYBS5bbBIVNFSvWkHlt48bbA-YHiDGRGhU9rCBx28RJc7CGJ37s1IF2u4S7UunCvRaCgqVto2NkBuVlRCqbkvoP_pLYEjnCGbNLu473oq4mny6Xs98cz0CEqze6qeeChExcIHLKGcqkjrjm5zBa_MqLy_-UiwlWdKyXLo8FJUy5fGinopx4XmoFzmgahVEOoUmqn70kc1YKu4gs8aT6tf498T-YBsZG95s5vfRO3dVURK6x-GWaN2996yXF4_Af3wymD986zMPrkX5XsbHM-3Mzo66uZm-4UmT25sTSHsFoQ2sfWmY5BgsoOldaSyKjw_uHVMQ4eZIWjIXLRsR-6bTSRUHU2DTzGQMmIzHzVFtk2-JPackd2fr-TazVluaCk9ZvXfPNeXGNy6Gr7IwLAsaES7m16eED9mkF_mHf0_ujX9jlxXnc5e1s35wwAVaHOLgJdgIzoL--D65A7F7kAPJQ58OIrbG7hmbVrwHC2U7ExRK2sTcZysAmgXHtQPWhA-aKaT0HttzpNdbzQ_pqOEiv42CCNcawlN8xaJgFfLlKbrYDzid4-CMZI9s6H-5qxsCRuI6H8koduHxO2HrKqPjIoIBsF79cN4nF3OEp63icTjtWfNZP-ybOue51I5DfawdIggoTozzuelLhfnCZvfo");
        TestDietResultDao.sendTestDietSource(
                testDietSource,
                testId,
                token,
                new CallbackOne<TestDietResult>() {
                    @Override
                    public void execute(TestDietResult testDietResult) {
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

    public static void TestDietDaoGetByTesftId() {
        Token token = new Token();
        token.setUserId("63");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjUzMTMzNDQsInVzZXJuYW1lIjoieS5hbmRyZXlrbysxN0BnbWFpbC5jb20iLCJpYXQiOiIxNDI1MjI2OTQ0In0.mK59xlnELWsRNHOB_xS--GCbSr152PH8Lo9zAQLC1bGqO0TjRb_fpRweQyckqZ8pr-jgpUJAzcemUQTq6HLDeGpx95ok7cMVQkvLOzwy1RIiyMIhCOFU-5nrrwwSQim8bAdhyZhu6nh5D7DC5byNRc8eLHgK5YpZpfGQvb0BtjONqUB5ZgDduOLKjSmMF_2TTsIwAXdIwDQ8LyqKNNraaI3k2V2JqOT7IXRhfbG3Ycr4a0k-qTjmnNvIR1ReV_iR3kNdzW1Q4SLfZ0V8Do5bNgs99Hukc3HhyJYku4qJL3AvfIIRDTcR4oUzG1sontks4EGG3JqlYMb0LVqa8RoawjAHDvKJwOTjNpWgVnvUQdq9QL34PicWTxZugzjV0U2gE9YIjU2LQ_Sq5t5fOC1_hWLZabXOkPKXTXZ7tWod0oFavTCJvM0o96NEDqsETOiMUOHO6R1XrY_0Zh1wK_XRzlEH8uvo0KdzZzXMGaAGKQY2PrF9e525xgoy5j-iEP8W69_qoBcnW-nBJAm6-Qu3mQW_2G533X_MHchzNTpjWAEtPc2L5ML10OvFFceLVYcnn8Thwhe_OrdB19Ks4ebA6rdTyLG7XlzC8y3MNyNXRaE3H7MVVkCQa72fW02WCQ_XKFBEBhXpQJyipB7YdzhP0Y8t18vCy29cxx8pJb8L3d4");
        TestDietDao.getTestDietByTestId(
                "36",
                token,
                new CallbackOne<List<TestDiet>>() {
                    @Override
                    public void execute(List<TestDiet> testDietList) {
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

    public static void TaskDaoUpdate() {
        Task task = new Task();
//        task.setId("2015021204");
//        task.setId("2015021205");
        task.setId("2015021405");

        ObjectNode nodeResult = Task.createResult(Task.Type.pill, true);

        Token token = new Token();
        token.setUserId("63");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjQ2MjM4MzgsInVzZXJuYW1lIjoieS5hbmRyZXlrbysxN0BnbWFpbC5jb20iLCJpYXQiOiIxNDI0NTM3NDM4In0.UyUIkF_M766kyv2aiYEWHyCwfrK4klj6zPBefa_-Br6zwg67__m1cv8KbkNAvOJKsWFgx4nsKmgK1aCQs1UQzQ7ZTwa3CS6JWK9roFaC7nw3aLKGltTQPgaA6rDM9OBgUFQvX2_Xzv_fEG4xNxcpLm6vN1MNpXDhUhQYecfkyH5Z80FoUl5wAvWoYravekC64t1gVW_sHa56we-k6Vw1QYBLCR5KYH5kLhm8bwj7nt_oOqQlXs_Da_V4f0ELNoBaOa7sgmaLTerIf1UCINQueK0Yq-eqHrOvg9LGIcIdkwk-_PHiDTWMJPz4w17AP5roVtcw8zkFTUJnDI32CW58w00hcbMYIKSENNwxqkI0Miyqe5RDyltTN1OxANrNNEg9aiGn1ehBQ0jeacFaggJ9rWROWEoLW4zGP6EMQLoNdvQho7I5l5jRBShOyGgXOiGUREquU9Uvph_A9MFGUMCzxgcTmN2MKslX27qyAE0p35Dr3VZ6cVR7yQctVb7Z-FyoxOw2rdlv4rtXdC3_MwajnsJpJxeU4R1ohWzxEi_IcUzKYOfSSkg8EC9H6bx5jSVF_FfhxqOurvA7u20igqKV5Xb-M7bhRnJTYeth-fbbgzbOr8LK-5gaFPBktKB5G_6OYHKKibh8Er0mjQ93Qmd76HoeAXkwPhMUlmh7NldqCdg");

        TaskDao.update(
                task,
                nodeResult,
                token,
                new CallbackOne<Task>() {
                    @Override
                    public void execute(Task task) {
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

    public static void PillDaoGetAll() {
        Token token = new Token();
        token.setUserId("63");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjQzNjcwOTgsInVzZXJuYW1lIjoieS5hbmRyZXlrbysxN0BnbWFpbC5jb20iLCJpYXQiOiIxNDI0MjgwNjk4In0.LcgyBqu6ey-ivFQyeh4BCD3rFxzeA5MyoA2sBhprcJhuAePA4OLFgZ61byCfH62j2zxm6QWo7WJAxH8CorohOxHOrnHjZfogwtSho3BmW-BwuAUO6M57aUxvmEdd9vYssQcfIQd95wU0l-XRIu16zNnb5pozrLa-5I6bXhhjTnfx95z-JH98-rXB6bK20JJVd8neKnhs6mC5aCJF8eNaEQn20JFPfc6fW94bE1rfRFvJP7traMI0tX7Ozhm7GxdPrRuH_KFwZJR06Uu25GSh0rMbUCvVS64b2GjwkA8l5XfP0O0GzTJlwXI2KupKyh12RQ7sWAhAGA09fvDp1y-Ea8gADeEDuV3TDb7y10V-CYKvnHr8AIAuqdgVH5jN_aaW3XHHsnxahZRCBk42Fk6KE_r0fxH387s8Q403B4quWYAzhAAjMG6pS_WLgtqoSjAbQHP3XhAWiziLQJkhq9hvWhyAi5USU_afqQlaQWmc5GDCNQjZVpQilTPnj4wOjqFgg32mWLGDZUVMfRXuqRjcbbSeS8fzwMUKkkKEe7mCxC-iGR83gateA9d10cAUluZ-_xvioMvmokCQ2dzUYv-buBwVVF4-LfrldBKMcXgA9mRtAtk1eHAVZEBItCu4z1TaGXP42amvOrv_uEFWVM9aABUChk5K8B-Srf5BKLSc24s");
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
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
                PillDao.Source.database_http
        );
    }

    public static void TimelineDaoGetAll() {
        Token token = new Token();
        token.setUserId("63");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjQzNjcwOTgsInVzZXJuYW1lIjoieS5hbmRyZXlrbysxN0BnbWFpbC5jb20iLCJpYXQiOiIxNDI0MjgwNjk4In0.LcgyBqu6ey-ivFQyeh4BCD3rFxzeA5MyoA2sBhprcJhuAePA4OLFgZ61byCfH62j2zxm6QWo7WJAxH8CorohOxHOrnHjZfogwtSho3BmW-BwuAUO6M57aUxvmEdd9vYssQcfIQd95wU0l-XRIu16zNnb5pozrLa-5I6bXhhjTnfx95z-JH98-rXB6bK20JJVd8neKnhs6mC5aCJF8eNaEQn20JFPfc6fW94bE1rfRFvJP7traMI0tX7Ozhm7GxdPrRuH_KFwZJR06Uu25GSh0rMbUCvVS64b2GjwkA8l5XfP0O0GzTJlwXI2KupKyh12RQ7sWAhAGA09fvDp1y-Ea8gADeEDuV3TDb7y10V-CYKvnHr8AIAuqdgVH5jN_aaW3XHHsnxahZRCBk42Fk6KE_r0fxH387s8Q403B4quWYAzhAAjMG6pS_WLgtqoSjAbQHP3XhAWiziLQJkhq9hvWhyAi5USU_afqQlaQWmc5GDCNQjZVpQilTPnj4wOjqFgg32mWLGDZUVMfRXuqRjcbbSeS8fzwMUKkkKEe7mCxC-iGR83gateA9d10cAUluZ-_xvioMvmokCQ2dzUYv-buBwVVF4-LfrldBKMcXgA9mRtAtk1eHAVZEBItCu4z1TaGXP42amvOrv_uEFWVM9aABUChk5K8B-Srf5BKLSc24s");
        TimelineDao.getAll(
                token,
                new CallbackOne<List<Timeline>>() {
                    @Override
                    public void execute(List<Timeline> timeline) {
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

    public static void TestPageDaoGetByLink() {
        Token token = new Token();
        token.setUserId("45");
        token.setTokenId("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXUyJ9.eyJleHAiOjE0MjM2NzMzMzgsInVzZXJuYW1lIjoieS5hbmRyZXlrbyszQGdtYWlsLmNvbSIsImlhdCI6IjE0MjM1ODY5MzgifQ.N_9UgyyhYHrXPheigoAlopwcrP6tvAxhhDT51ZPGRSBWxyJ0JnCrkUxoIvKAsyD9HMFI4r_w7m_4RT77qeJXe2GCT3Sm0tTo5O36q_Fksw8V4sTdzIYsAxeRhoCA1GToDS9EOt40gttQ4OSsMtukxA6nw9LV_XOEApaPNdDAIY0BUzZ3G6VnQolUnqTnNzACmEHfvjY4BZuL4N12EIRWz605AlqZCCZTGqc_JW6zWa34MKeDSBJKA31BfK160Jc-pQLJ93EOXJhDeH5nu5avKR0eHnotKeitbRPewXFgbbxIFPiLNbvuJ93xnUB_eweYO23yyV9d1m6OytoFp9jD8BM-d8OuX6WWVsdOMyCPCFX4pv_B-6MsJcKFMBZOfIGHKS3TBJjragfwCl79nPrqGOys7fDG94oC29X25VCSotETgFC2FUiHep8lGZLKrI8dDzykk_fYNkbt-wP9NsRLyxZuof2lVd5QCzXRw6KCfLh29G5wxC3fJ_D-loQwa0vrh01f1_L_DBtgpq_Id_d6j9YWimnEikIfAsZrhnrQCiNJog7x5_T7pUTxiv1qK7IiyNa5pu4Wgm3m9d2jDssLpOyF4nHyJ5b0I9kd06abxof84raEm7dJwe1znhrvgyPaLqtpWzINp5bVlBNiSa_H2GTdJ4ATv_pjPRAJLHagerE");
        PageDao.getByLink(
                "/api/v1/account/test-results/21/pages/physicalActivityMinutes",
                token,
                new CallbackOne<TestPage>() {
                    @Override
                    public void execute(TestPage testPage) {
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
                UserDao.Source.web
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
