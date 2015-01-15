package ru.com.cardiomagnil.util;

import java.util.List;

import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.region.Ca_Region;
import ru.com.cardiomagnil.ca_model.region.Ca_RegionDao;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;

public class TestMethods {

    public static void testCurrentMethod() {
//        Ca_UserRegister();
        Ca_RegionDaoGetAll();
    }


    public static void Ca_UserRegister() {
        Ca_UserDao.register(
                null,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User user) {
                        ThtreadHelper.logThread("doWhatYouNeed->success");
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        ThtreadHelper.logThread("doWhatYouNeed->error");
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
                        ThtreadHelper.logThread("doWhatYouNeed->success");
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        ThtreadHelper.logThread("doWhatYouNeed->error");
                        int t = 1;
                        t++;
                    }
                }
        );
    }

}
