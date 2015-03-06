package ru.com.cardiomagnyl.model.test;

import android.util.Pair;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.db.DbRequestHolder;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;

public class PageDao extends BaseDaoImpl<TestPage, Integer> {
    public PageDao(ConnectionSource connectionSource, Class<TestPage> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getByLink(final String link,
                                 final Token token,
                                 final CallbackOne<TestPage> onSuccess,
                                 final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<TestPage>() {
        };

        CallbackOne<TestPage> onStoreIntoDatabase = new CallbackOne<TestPage>() {
            @Override
            public void execute(TestPage page) {
                storeIntoDatabase(page, link);
            }
        };

        RuntimeExceptionDao helperFactoryPage = HelperFactory.getHelper().getRuntimeDataDao(TestPage.class);
        QueryBuilder queryBuilder = helperFactoryPage.queryBuilder();
        try {
            queryBuilder.where().idEq(TestPage.generatePageId(link));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .setQueryMethod(DbRequestHolder.QueryMethod.queryForFirst)
                        .create();

        Pair<String, String> extractedIdAndName = TestPage.extractIdAndName(link);
        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, String.format(Url.ACCOUNT_TEST_RECOMMENDATIONS, extractedIdAndName.first, extractedIdAndName.second, token.getTokenId()), typeReference)
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

    private static void storeIntoDatabase(final TestPage testPage, final String link) {
        try {
            testPage.setId(TestPage.generatePageId(link));
            RuntimeExceptionDao helperFactoryPage = HelperFactory.getHelper().getRuntimeDataDao(TestPage.class);
            helperFactoryPage.createOrUpdate(testPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
