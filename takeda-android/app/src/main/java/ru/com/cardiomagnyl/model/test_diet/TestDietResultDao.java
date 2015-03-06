package ru.com.cardiomagnyl.model.test_diet;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnyl.api.DataLoadDispatcher;
import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.api.db.DbRequestHolder;
import ru.com.cardiomagnyl.api.db.HelperFactory;
import ru.com.cardiomagnyl.api.http.HttpRequestHolder;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class TestDietResultDao extends BaseDaoImpl<TestDietResult, Integer> {
    public TestDietResultDao(ConnectionSource connectionSource, Class<TestDietResult> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void sendTestDietSource(final String testDietSource,
                                          final String testId,
                                          final Token token,
                                          final CallbackOne<TestDietResult> onSuccess,
                                          final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<TestDietResult>() {
        };

        CallbackOne<TestDietResult> onStoreIntoDatabase = new CallbackOne<TestDietResult>() {
            @Override
            public void execute(TestDietResult testDietResult) {
                RuntimeExceptionDao helperFactoryTestDietResultHolder = HelperFactory.getHelper().getRuntimeDataDao(TestDietResultHolder.class);
                TestDietResultHolder testDietResultHolder = new TestDietResultHolder(testDietResult, testId);
                helperFactoryTestDietResultHolder.createOrUpdate(testDietResultHolder);
            }
        };

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, String.format(Url.ACCOUNT_TEST_RESULTS_DIET_RECOMMENDATIONS, testId, testDietSource, token.getTokenId()), typeReference)
                        .addHeaders(Url.GET_HEADERS)
                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        httpRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

    public static void getByTestId(final String testId,
                                   final CallbackOne<TestDietResult> onSuccess,
                                   final CallbackOne<Response> onFailure) {
        RuntimeExceptionDao helperFactoryTestDietResultHolder = HelperFactory.getHelper().getRuntimeDataDao(TestDietResultHolder.class);
        QueryBuilder queryBuilder = helperFactoryTestDietResultHolder.queryBuilder();
        try {
            queryBuilder.where().idEq(testId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        CallbackOneReturnable<TestDietResultHolder, TestDietResult> afterExtractedDb = new CallbackOneReturnable<TestDietResultHolder, TestDietResult>() {
            @Override
            public TestDietResult execute(TestDietResultHolder testDietResultHolder) {
                return testDietResultHolder.getTestDietResult();
            }
        };

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .setQueryMethod(DbRequestHolder.QueryMethod.queryForFirst)
                        .setAfterExtracted(afterExtractedDb)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        dbRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

}
