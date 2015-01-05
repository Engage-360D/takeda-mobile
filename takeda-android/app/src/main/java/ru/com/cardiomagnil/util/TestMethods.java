package ru.com.cardiomagnil.util;

import java.util.List;

import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.region.Ca_Region;
import ru.com.cardiomagnil.ca_model.region.Ca_RegionDao;

public class TestMethods {

    public static void testCurrentMethod() {
        MLpuAutocompleteDaoGetAutocomplete();
    }

    public static void MLpuAutocompleteDaoGetAutocomplete() {
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
