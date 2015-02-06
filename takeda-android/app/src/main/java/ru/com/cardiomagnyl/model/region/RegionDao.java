package ru.com.cardiomagnyl.model.region;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.db.DbRequestHolder;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.util.CallbackOne;

public class RegionDao extends BaseDaoImpl<Region, Integer> {
    public RegionDao(ConnectionSource connectionSource, Class<Region> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getAll(final CallbackOne<List<Region>> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<List<Region>>() {
        };

        CallbackOne<List<Region>> onStoreIntoDatabase = new CallbackOne<List<Region>>() {
            @Override
            public void execute(List<Region> regionList) {
                storeIntoDatabase(regionList);
            }
        };

        RuntimeExceptionDao helperFactoryRegion = HelperFactory.getHelper().getRuntimeDataDao(Region.class);
        QueryBuilder queryBuilder = helperFactoryRegion.queryBuilder();

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, Url.REGIONS, typeReference)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        DataLoadSequence dataLoadSequence =
                new DataLoadSequence
                        .Builder(dbRequestHolder)
                        .addRequestHolder(httpRequestHolder)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        dataLoadSequence,
                        onSuccess,
                        onFailure
                );
    }

    public static void storeIntoDatabase(final List<Region> regionList) {
        if (regionList != null && !regionList.isEmpty()) {
            RuntimeExceptionDao helperFactoryRegion = HelperFactory.getHelper().getRuntimeDataDao(Region.class);
            for (Region region : regionList) {
                helperFactoryRegion.createOrUpdate(region);
            }
        }
    }

}
