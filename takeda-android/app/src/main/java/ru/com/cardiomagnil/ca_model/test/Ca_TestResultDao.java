package ru.com.cardiomagnil.ca_model.test;

import com.android.volley.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.com.cardiomagnil.ca_api.DataLoadDispatcher;
import ru.com.cardiomagnil.ca_api.Url;
import ru.com.cardiomagnil.ca_api.http.HttpRequestHolder;
import ru.com.cardiomagnil.ca_model.common.Ca_DataWraper;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.util.CallbackOne;

public class Ca_TestResultDao extends BaseDaoImpl<Ca_TestResult, Integer> {
    public Ca_TestResultDao(ConnectionSource connectionSource, Class<Ca_TestResult> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public static void sendTestSource(final Ca_TestSource testSource,
                                      final Ca_Token token,
                                      final CallbackOne<Ca_TestResult> onSuccess,
                                      final CallbackOne<Ca_Response> onFailure) {
        TypeReference typeReference = new TypeReference<Ca_TestResult>() {
        };

//        CallbackOne<Ca_TestResult> onStoreIntoDatabase = new CallbackOne<Ca_TestResult>() {
//            @Override
//            public void execute(Ca_TestResult testResult) {
//                RuntimeExceptionDao helperFactoryToken = HelperFactory.getHelper().getRuntimeDataDao(Ca_Token.class);
//                helperFactoryToken.createOrUpdate(testResult);
//            }
//        };

        ObjectNode objectNode = new ObjectMapper().valueToTree(testSource);
        String packedTestSource = Ca_DataWraper.wrap(objectNode).toString();
//        String packedTestSource = "{\"data\":{\"sex\":\"male\",\"birthday\":\"1991-01-19T00:00:00+0000\",\"growth\":185,\"weight\":98,\"isSmoker\":false,\"cholesterolLevel\":null,\"isCholesterolDrugsConsumer\":null,\"hasDiabetes\":false,\"hadSugarProblems\":false,\"isSugarDrugsConsumer\":null,\"arterialPressure\":120,\"isArterialPressureDrugsConsumer\":null,\"physicalActivityMinutes\":100,\"hadHeartAttackOrStroke\":true,\"isAddingExtraSalt\":true,\"isAcetylsalicylicDrugsConsumer\":true}}";

        HttpRequestHolder httpRequestHolder =
                new HttpRequestHolder
                        .Builder(Request.Method.POST,  String.format(Url.ACCOUNT_TEST_RESULTS, token.getTokenId()), typeReference)
                        .addHeaders(Url.POST_HEADERS)
                        .setBody(packedTestSource)
//                        .setOnStoreIntoDatabase(onStoreIntoDatabase)
                        .create();

        DataLoadDispatcher
                .getInstance()
                .receive(
                        httpRequestHolder,
                        onSuccess,
                        onFailure
                );
    }

}
