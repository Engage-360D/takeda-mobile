package ru.com.cardiomagnil.ca_api.cache;

import com.android.volley.Cache;

import java.util.Map;

import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_api.base.BaseVolleyDataLoader;
import ru.com.cardiomagnil.ca_api.http.CachedStringRequest;
import ru.com.cardiomagnil.ca_api.http.HttpHelper;
import ru.com.cardiomagnil.ca_model.common.Ca_Error;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ThtreadHelper;

public class CacheDataLoader extends BaseVolleyDataLoader {

    @Override
    public <T> void invokeDataLoad(
            final DataLoadSequence dataLoadSequence,
            final CallbackOne<T> callbackOneOnSuccess,
            final CallbackOne<Ca_Response> callbackOneOnError) {
        final CacheRequestHolder cacheRequestHolder = (CacheRequestHolder) dataLoadSequence.poll();

        CachedStringRequest cachedStringRequest = new CachedStringRequest(
                cacheRequestHolder.getMethod(),
                cacheRequestHolder.getUrl(),
                null, null) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = cacheRequestHolder.getParams();
                return params;
            }
        };

        String cacheKey = cachedStringRequest.getCacheKey();

        Cache cache = HttpHelper.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(cacheKey);

        if (entry == null || entry.data == null) {
            ThtreadHelper.logThread("CacheDataLoader->invokeDataLoad->error_null");
            Ca_Response responseError = new Ca_Response
                    .Builder(new Ca_Error(Status.CACHE_ERROR, Status.getDescription(Status.CACHE_ERROR)))
                    .create();
            handleErrorResponse(responseError, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        } else {
            Ca_Response response = dataToResponse(entry.data);
            if (response.isSuccessful()) {
                ThtreadHelper.logThread("CacheDataLoader->invokeDataLoad->success");
                handleSuccessResponse(response, cacheRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
            } else {
                ThtreadHelper.logThread("CacheDataLoader->invokeDataLoad->error");
                handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
            }
        }
    }
}
