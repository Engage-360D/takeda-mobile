package ru.com.cardiomagnil.ca_api.cache;

import com.android.volley.Cache;

import ru.com.cardiomagnil.ca_api.CachedStringRequest;
import ru.com.cardiomagnil.ca_api.DataLoadSequence;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_api.base.BaseVolleyDataLoader;
import ru.com.cardiomagnil.ca_api.http.HttpHelper;
import ru.com.cardiomagnil.model.common.Error;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ThtreadHelper;

public class CacheDataLoader extends BaseVolleyDataLoader {

    @Override
    public <T> void invokeDataLoad(
            final DataLoadSequence dataLoadSequence,
            final CallbackOne<T> callbackOneOnSuccess,
            final CallbackOne<Response> callbackOneOnError) {
        final CacheRequestHolder cacheRequestHolder = (CacheRequestHolder) dataLoadSequence.poll();

        CachedStringRequest cachedStringRequest = new CachedStringRequest(
                cacheRequestHolder.getMethod(),
                cacheRequestHolder.getUrl(),
                null,
                cacheRequestHolder.getParams(),
                null,
                null,
                null);


        String cacheKey = cachedStringRequest.getCacheKey();

        Cache cache = HttpHelper.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(cacheKey);

        if (entry == null || entry.data == null) {
            ThtreadHelper.logThread("CacheDataLoader->invokeDataLoad->error_null");
            Response responseError = new Response
                    .Builder(new Error(Status.CACHE_ERROR, Status.getDescription(Status.CACHE_ERROR)))
                    .create();
            handleErrorResponse(responseError, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        } else {
            Response response = dataToResponse(entry.data);
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
