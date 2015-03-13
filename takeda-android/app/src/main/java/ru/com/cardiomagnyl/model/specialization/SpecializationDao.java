package ru.com.cardiomagnyl.model.specialization;

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

public class SpecializationDao extends BaseDaoImpl<Specialization, Integer> {
    public SpecializationDao(ConnectionSource connectionSource, Class<Specialization> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getAll(final CallbackOne<List<Specialization>> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<List<Specialization>>() {
        };

        CallbackOne<List<Specialization>> onStoreIntoDatabase = new CallbackOne<List<Specialization>>() {
            @Override
            public void execute(List<Specialization> specializationsList) {
                storeIntoDatabase(specializationsList);
            }
        };

        RuntimeExceptionDao helperFactorySpecialization = HelperFactory.getHelper().getRuntimeDataDao(Specialization.class);
        QueryBuilder queryBuilder = helperFactorySpecialization.queryBuilder();

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, Url.INSTITUTION_SPECIALIZATIONS, typeReference)
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

    public static void storeIntoDatabase(final List<Specialization> specializationList) {
        if (specializationList != null && !specializationList.isEmpty()) {
            RuntimeExceptionDao helperFactorySpecialization = HelperFactory.getHelper().getRuntimeDataDao(Specialization.class);
            for (Specialization specialization : specializationList) {
                helperFactorySpecialization.createOrUpdate(specialization);
            }
        }
    }

}
