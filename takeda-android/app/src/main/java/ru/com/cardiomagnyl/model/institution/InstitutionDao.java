package ru.com.cardiomagnyl.model.institution;

import android.text.TextUtils;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.net.URLEncoder;
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

public class InstitutionDao extends BaseDaoImpl<Institution, Integer> {
    public InstitutionDao(ConnectionSource connectionSource, Class<Institution> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getByTownAndSpec(final String town,
                                        final String spec,
                                        final CallbackOne<List<Institution>> onSuccess,
                                        final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<List<Institution>>() {
        };

        CallbackOne<List<Institution>> onStoreIntoDatabase = new CallbackOne<List<Institution>>() {
            @Override
            public void execute(List<Institution> institutionsList) {
                storeIntoDatabase(institutionsList);
            }
        };

        RuntimeExceptionDao helperFactoryInstitution = HelperFactory.getHelper().getRuntimeDataDao(Institution.class);
        QueryBuilder queryBuilder = helperFactoryInstitution.queryBuilder();
        try {
            if (!TextUtils.isEmpty(town)) queryBuilder.where().eq("parsed_town", town);
            if (!TextUtils.isEmpty(spec)) queryBuilder.where().eq("specialization", spec);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, String.format(Url.INSTITUTIONS, URLEncoder.encode(town), URLEncoder.encode(spec)), typeReference)
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

    public static void getById(final String institutionId,
                                final CallbackOne<Institution> onSuccess,
                                final CallbackOne<Response> onFailure) {
        RuntimeExceptionDao helperFactoryInstitutionDao = HelperFactory.getHelper().getRuntimeDataDao(Institution.class);
        QueryBuilder queryBuilder = helperFactoryInstitutionDao.queryBuilder();
        try {
            queryBuilder.where().eq("id", institutionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .setQueryMethod(DbRequestHolder.QueryMethod.queryForFirst)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        dbRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

    public static void storeIntoDatabase(final List<Institution> institutionsList) {
        if (institutionsList != null && !institutionsList.isEmpty()) {
            RuntimeExceptionDao helperFactoryInstitution = HelperFactory.getHelper().getRuntimeDataDao(Institution.class);
            for (Institution institution : institutionsList) {
                helperFactoryInstitution.createOrUpdate(institution);
            }
        }
    }

}
