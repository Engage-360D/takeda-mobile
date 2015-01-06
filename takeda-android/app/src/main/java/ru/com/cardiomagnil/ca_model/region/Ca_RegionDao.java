package ru.com.cardiomagnil.ca_model.region;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.com.cardiomagnil.api.Url;
import ru.com.cardiomagnil.ca_api.DataLoadDispatcher;
import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.db.DbRequestHolder;
import ru.com.cardiomagnil.ca_api.db.HelperFactory;
import ru.com.cardiomagnil.ca_api.http.HttpRequestHolder;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.util.CallbackOne;

public class Ca_RegionDao extends BaseDaoImpl<Ca_Region, Integer> {
    public Ca_RegionDao(ConnectionSource connectionSource, Class<Ca_Region> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getAll(final CallbackOne<List<Ca_Region>> onSuccess,
                              final CallbackOne<Ca_Response> onFailure) {
        TypeReference typeReference = new TypeReference<List<Ca_Region>>() {
        };

        CallbackOne<List<Ca_Region>> onStoreIntoDatabase = new CallbackOne<List<Ca_Region>>() {
            @Override
            public void execute(List<Ca_Region> lpuAutocompleteList) {
                storeIntoDatabase(lpuAutocompleteList);
            }
        };

        RuntimeExceptionDao helperFactoryRegion = HelperFactory.getHelper().getRuntimeDataDao(Ca_Region.class);
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

    public static void storeIntoDatabase(final List<Ca_Region> regionList) {
        storeIntoDatabaseHelper(regionList);
    }

    private static void storeIntoDatabaseHelper(final List<Ca_Region> regionList) {
        if (regionList != null && !regionList.isEmpty()) {
            RuntimeExceptionDao helperFactoryLpuAutocomplete = HelperFactory.getHelper().getRuntimeDataDao(Ca_Region.class);
            for (Ca_Region region : regionList) {
                helperFactoryLpuAutocomplete.createOrUpdate(region);
            }
        }
    }

}
