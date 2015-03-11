package ru.com.cardiomagnyl.model.town;

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

public class TownDao extends BaseDaoImpl<Town, Integer> {
    public TownDao(ConnectionSource connectionSource, Class<Town> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getAll(final CallbackOne<List<Town>> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<List<Town>>() {
        };

        CallbackOne<List<Town>> onStoreIntoDatabase = new CallbackOne<List<Town>>() {
            @Override
            public void execute(List<Town> townsList) {
                storeIntoDatabase(townsList);
            }
        };

        RuntimeExceptionDao helperFactoryTown = HelperFactory.getHelper().getRuntimeDataDao(Town.class);
        QueryBuilder queryBuilder = helperFactoryTown.queryBuilder();

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, Url.INSTITUTION_PARSED_TOWNS, typeReference)
                        .addHeaders(Url.GET_HEADERS)
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

    public static void storeIntoDatabase(final List<Town> townsList) {
        if (townsList != null && !townsList.isEmpty()) {
            RuntimeExceptionDao helperFactoryTown = HelperFactory.getHelper().getRuntimeDataDao(Town.class);
            for (Town town : townsList) {
                helperFactoryTown.createOrUpdate(town);
            }
        }
    }

}
