package ru.com.cardiomagnyl.api.http;

import com.android.volley.VolleyError;

import ru.com.cardiomagnyl.api.CachedStringRequest;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Status;
import ru.com.cardiomagnyl.api.base.BaseVolleyDataLoader;
import ru.com.cardiomagnyl.model.common.Error;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.util.Callback;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.ThreadHelper;

public class HttpDataLoader extends BaseVolleyDataLoader {
    @Override
    public <T> void invokeDataLoad(final DataLoadSequence dataLoadSequence,
                                   final CallbackOne<T> callbackOneOnSuccess,
                                   final CallbackOne<Response> callbackOneOnError) {
        final HttpRequestHolder httpRequestHolder = (HttpRequestHolder) dataLoadSequence.poll();
        final com.android.volley.Response.Listener<String> successListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(final String responseString) {
                ThreadHelper.startNotInMain(new Callback() {
                    @Override
                    public void execute() {
                        Response response = stringToResponse(responseString);
                        if (response.isSuccessful()) {
                            ThreadHelper.logThread("HttpDataLoader->onResponse->success");
                            handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        } else {
                            ThreadHelper.logThread("HttpDataLoader->onResponse->error");
                            handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        }
                    }
                });
            }
        };
        final com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                ThreadHelper.startNotInMain(new Callback() {
                    @Override
                    public void execute() {
                        if (volleyError.networkResponse == null || volleyError.networkResponse.data == null) {
                            ThreadHelper.logThread("HttpDataLoader->onErrorResponse->error_null");
                            Response error = new Response
                                    .Builder(new Error(Status.LAN_ERROR, Status.getDescription(Status.LAN_ERROR)))
                                    .create();
                            handleErrorResponse(error, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        } else {
                            ThreadHelper.logThread("HttpDataLoader->onErrorResponse->error");
                            int errorCode = volleyError.networkResponse.statusCode;
                            String errorDescription = Status.getDescription(errorCode);
                            Response responseError = new Response.Builder(new Error(errorCode, errorDescription)).create();
                            handleErrorResponse(responseError, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                        }
                    }
                });
            }
        };

//        final String fakeResponse = "{\"links\":{\"users.region\":{\"href\":\"/api/v1/regions/{users.region}\",\"type\":\"regions\"}},\"data\":{\"id\":\"8\",\"email\":\"y.andreyko3@gmail.com\",\"firstname\":\"XXXX\",\"lastname\":\"XXXX\",\"birthday\":\"1990-10-10T00:00:00+00:00\",\"vkontakteId\":null,\"facebookId\":null,\"specializationExperienceYears\":null,\"specializationGraduationDate\":null,\"specializationInstitutionAddress\":null,\"specializationInstitutionName\":null,\"specializationInstitutionPhone\":null,\"specializationName\":null,\"roles\":[\"ROLE_USER\", \"ROLE_Test1\", \"ROLE_Test2\"],\"isEnabled\":false,\"links\":{\"region\":\"1\"}}}";
        final String fakeResponse = "{\"data\":{\"state\":\"attention\",\"title\":\"У вас недостаточно физической активности 80 минут в неделю\",\"subtitle\":\"Нормой является значение от 150 минут в неделю\",\"text\":\"Физическая активность является средством сохранения здоровья сердца и сосудов.\\n\\nЖелательно предпочесть аэробную динамическую нагрузку 5 раз в неделю от 30 мин. до 2 ч. в день.\\nНагрузка  рекомендуется такая, чтобы частота пульса достигла 65-70% от максимальной частоты пульса для данного возраста.\\n\\nМаксимальная частота пульса рассчитывается по формуле: 220 минус возраст в годах.\\n\\nБольным с заболеваниями сердца и сосудов режим нагрузок подбирается врачом индивидуально в соответствии с результатами электрокардиографического теста с физической нагрузкой.\\n\"}}";
        final com.android.volley.Response.Listener<String> fakeSuccessListener = new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(final String responseString) {
                Response response = stringToResponse(fakeResponse);
                if (response.isSuccessful()) {
                    handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                } else {
                    handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                }
            }
        };
        final com.android.volley.Response.ErrorListener fakeErrorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                Response response = stringToResponse(fakeResponse);
                if (response.isSuccessful()) {
                    handleSuccessResponse(response, httpRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                } else {
                    handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
                }
            }
        };

        CachedStringRequest cachedStringRequest = new CachedStringRequest(
                httpRequestHolder.getMethod(),
                httpRequestHolder.getUrl(),
                httpRequestHolder.getHeaders(),
                httpRequestHolder.getParams(),
                httpRequestHolder.getBody(),
//                fakeSuccessListener,
//                fakeErrorListener
                successListener,
                errorListener
        );

        // Adding request to request queue
        HttpHelper.getInstance().addToRequestQueue(cachedStringRequest);
    }
}