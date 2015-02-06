package ru.com.cardiomagnil.model.test;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnil.ca_api.DataLoadDispatcher;
import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.Url;
import ru.com.cardiomagnil.ca_api.db.DbRequestHolder;
import ru.com.cardiomagnil.ca_api.db.HelperFactory;
import ru.com.cardiomagnil.ca_api.http.HttpRequestHolder;
import ru.com.cardiomagnil.model.common.DataWraper;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.model.token.Token;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.CallbackOneReturnable;

public class TestResultDao extends BaseDaoImpl<TestResult, Integer> {
    public TestResultDao(ConnectionSource connectionSource, Class<TestResult> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void getAll(final Token token,
                              final CallbackOne<List<TestResult>> onSuccess,
                              final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<List<TestResult>>() {
        };

        CallbackOneReturnable<List<TestResultHolder>, List<TestResult>> onOnAfterExtractDb = new CallbackOneReturnable<List<TestResultHolder>, List<TestResult>>() {
            @Override
            public List<TestResult> execute(List<TestResultHolder> testResultHolderList) {
                return extractResultHolderList(testResultHolderList);
            }
        };

        CallbackOne<List<TestResult>> onStoreIntoDatabase = new CallbackOne<List<TestResult>>() {
            @Override
            public void execute(List<TestResult> testResultList) {
                storeIntoDatabase(testResultList, token.getUserId());
            }
        };

        RuntimeExceptionDao helperFactoryTestResultHolder = HelperFactory.getHelper().getRuntimeDataDao(TestResultHolder.class);
        QueryBuilder queryBuilder = helperFactoryTestResultHolder.queryBuilder();

        DbRequestHolder dbRequestHolder =
                new DbRequestHolder
                        .Builder(queryBuilder)
                        .setOnAfterExtracted(onOnAfterExtractDb)
                        .create();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.GET, String.format(Url.ACCOUNT_TEST_RESULTS, token.getTokenId()), typeReference)
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

    public static void sendTestSource(final TestSource testSource,
                                      final Token token,
                                      final CallbackOne<TestResult> onSuccess,
                                      final CallbackOne<Response> onFailure) {
        TypeReference typeReference = new TypeReference<TestResult>() {
        };

        CallbackOne<TestResult> onStoreIntoDatabase = new CallbackOne<TestResult>() {
            @Override
            public void execute(TestResult testResult) {
                RuntimeExceptionDao helperFactoryTestResultHolder = HelperFactory.getHelper().getRuntimeDataDao(TestResultHolder.class);
                TestResultHolder testResultHolder = new TestResultHolder(testResult, token.getUserId());
                helperFactoryTestResultHolder.createOrUpdate(testResultHolder);
            }
        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(testSource);
        String packedTestSource = DataWraper.wrap(objectNode).toString();

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST, String.format(Url.ACCOUNT_TEST_RESULTS, token.getTokenId()), typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(packedTestSource)
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

    public static void storeIntoDatabase(final List<TestResult> testResultList, final String userId) {
        if (testResultList != null && !testResultList.isEmpty()) {
            RuntimeExceptionDao helperFactoryTestResultHolder = HelperFactory.getHelper().getRuntimeDataDao(TestResultHolder.class);
            for (TestResult testResult : testResultList) {
                TestResultHolder testResultHolder = new TestResultHolder(testResult, userId);
                helperFactoryTestResultHolder.createOrUpdate(testResultHolder);
            }
        }
    }

    private static List<TestResult> extractResultHolderList(List<TestResultHolder> testResultHolderList) {
        if (testResultHolderList == null) return null;

        List testResultList = new ArrayList<TestResult>();
        for (TestResultHolder testResultHolder : testResultHolderList) {
            testResultList.add(testResultHolder.getTestResult());
        }

        return testResultList;
    }

}
