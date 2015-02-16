package ru.com.cardiomagnyl.api.cache;

import com.android.volley.Cache;

import ru.com.cardiomagnyl.api.CachedStringRequest;
import ru.com.cardiomagnyl.api.DataLoadSequence;
import ru.com.cardiomagnyl.api.Status;
import ru.com.cardiomagnyl.api.base.BaseVolleyDataLoader;
import ru.com.cardiomagnyl.api.http.HttpHelper;
import ru.com.cardiomagnyl.model.common.Error;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.ThreadHelper;

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
            ThreadHelper.logThread("CacheDataLoader->invokeDataLoad->error_null");
            Response responseError = new Response
                    .Builder(new Error(Status.CACHE_ERROR, Status.getDescription(Status.CACHE_ERROR)))
                    .create();
            handleErrorResponse(responseError, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
        } else {
            Response response = dataToResponse(entry.data);
            if (response.isSuccessful()) {
                ThreadHelper.logThread("CacheDataLoader->invokeDataLoad->success");
                handleSuccessResponse(response, cacheRequestHolder, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
            } else {
                ThreadHelper.logThread("CacheDataLoader->invokeDataLoad->error");
                handleErrorResponse(response, dataLoadSequence, callbackOneOnSuccess, callbackOneOnError);
            }
        }
    }
}
