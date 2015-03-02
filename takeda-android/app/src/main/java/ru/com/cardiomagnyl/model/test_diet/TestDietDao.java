package ru.com.cardiomagnyl.model.test_diet;

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
import ru.com.cardiomagnyl.model.test_diet_answer.TestDietAnswer;
import ru.com.cardiomagnyl.model.test_diet_proxy.TestDietMergedProxy;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public class TestDietDao extends BaseDaoImpl<TestDiet, Integer> {
    public TestDietDao(ConnectionSource connectionSource, Class<TestDiet> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getByTestId(final String testId,
                                   final Token token,
                                   final CallbackOne<List<TestDiet>> onSuccess,
                                   final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<TestDietMergedProxy>() {
        };

        CallbackOne<Response> beforeExtracted = new CallbackOne<Response>() {
            @Override
            public void execute(Response response) {
                TestDietMergedProxy.mergeResponse(response);
            }
        };

        CallbackOneReturnable<TestDietMergedProxy, List<TestDiet>> afterExtracted = new CallbackOneReturnable<TestDietMergedProxy, List<TestDiet>>() {
            @Override
            public List<TestDiet> execute(TestDietMergedProxy testDietMergedProxy) {
                return testDietMergedProxy != null ? testDietMergedProxy.extractAllTestDiets() : null;
            }
        };

        CallbackOne<List<TestDiet>> onStoreIntoDatabase = new CallbackOne<List<TestDiet>>() {
            @Override
            public void execute(List<TestDiet> testDiet) {
                storeIntoDatabase(testDiet, testId);
            }
        };

        RuntimeExceptionDao helperFactoryTestDiet = HelperFactory.getHelper().getRuntimeDataDao(TestDiet.class);
        QueryBuilder queryBuilder = helperFactoryTestDiet.queryBuilder();
        try {
            queryBuilder.where().eq("test_id", testId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, String.format(Url.ACCOUNT_TEST_RESULTS_DIET_QUESTIONS, testId, token.getTokenId()), typeReference)
                        .addHeaders(Url.GET_HEADERS)
                        .setBeforeExtracted(beforeExtracted)
                        .setAfterExtracted(afterExtracted)
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

    public static void storeIntoDatabase(final List<TestDiet> testDietList, final String testId) {
        if (testDietList != null && !testDietList.isEmpty()) {
            final RuntimeExceptionDao helperFactoryTestDiet = HelperFactory.getHelper().getRuntimeDataDao(TestDiet.class);
            final RuntimeExceptionDao helperFactoryTestDietAnswer = HelperFactory.getHelper().getRuntimeDataDao(TestDietAnswer.class);

            for (TestDiet currentTestDiet : testDietList) {
                currentTestDiet.setTestId(testId);
                helperFactoryTestDiet.createOrUpdate(currentTestDiet);
                for (TestDietAnswer currentTestDietAnswer : currentTestDiet.getAnswers()) {
                    currentTestDietAnswer.setTestDiet(currentTestDiet);
                    helperFactoryTestDietAnswer.createOrUpdate(currentTestDietAnswer);
                }
            }
        }
    }

}
