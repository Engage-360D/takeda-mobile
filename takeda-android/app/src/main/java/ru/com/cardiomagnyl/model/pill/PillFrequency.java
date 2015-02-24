package ru.com.cardiomagnyl.model.pill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;

public enum PillFrequency implements BaseModelHelper {
    daily(1, R.string.daily),
    every_2_days(2, R.string.every_2_days),
    undefined(0, R.string.not_specified);

    private final int id;
    private final int name;

    private PillFrequency(int id, int name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) { /* do nothing*/ }

    @Override
    public String getName() {
        return CardiomagnylApplication.getAppContext().getString(name);
    }

    @Override
    public void setName(String name) { /* do nothing*/ }

    public static List<BaseModelHelper> getFrequenciesList() {
        List<BaseModelHelper> frequenciesList = new ArrayList<BaseModelHelper>(Arrays.asList(daily, every_2_days, undefined));
        return frequenciesList;
    }

    public static PillFrequency getById(int id) {
        for (PillFrequency pillFrequency : PillFrequency.values()) {
            if (pillFrequency.getId() == id) return pillFrequency;
        }

        return PillFrequency.undefined;
    }

}
