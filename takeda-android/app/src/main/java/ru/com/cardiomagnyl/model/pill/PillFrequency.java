package ru.com.cardiomagnyl.model.pill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;

public enum PillFrequency implements BaseModelHelper<Integer> {
    daily(1, R.string.daily, 1 * 24 * 60 * 60 * 1000 /*1 day*/),
    every_2_days(2, R.string.every_2_days, 2 * 24 * 60 * 60 * 1000 /*2 days*/),
    undefined(0, R.string.not_specified, 0);

    private final int id;
    private final int name;
    private final long period;

    private PillFrequency(int id, int name, long period) {
        this.id = id;
        this.name = name;
        this.period = period;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) { /*does nothing*/ }

    @Override
    public String getName() {
        return CardiomagnylApplication.getAppContext().getString(name);
    }

    @Override
    public void setName(String name) { /*does nothing*/ }

    public long getPeriod() {
        return period;
    }

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
