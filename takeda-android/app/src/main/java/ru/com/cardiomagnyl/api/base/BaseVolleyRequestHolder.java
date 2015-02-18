package ru.com.cardiomagnyl.api.base;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.CallbackOneReturnable;

public abstract class BaseVolleyRequestHolder extends BaseRequestHolder {
    /**
     * according to com.android.volley.Request
     */
    protected static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    protected final int mMethod;
    protected final String mUrl;
    protected final Map<String, String> mParams;
    protected final TypeReference mTypeReference;
    protected final CallbackOne mBeforeExtracted;
    protected final CallbackOneReturnable mAfterExtracted;
    protected final CallbackOne mOnStoreIntoDatabase;

    public BaseVolleyRequestHolder(final int method,
                                   final String url,
                                   final Map<String, String> params,
                                   final TypeReference typeReference,
                                   final CallbackOne beforeExtracted,
                                   final CallbackOneReturnable afterExtracted,
                                   final CallbackOne onStoreIntoDatabase) {
        mMethod = method;
        mUrl = url;
        mParams = params;
        mTypeReference = typeReference;
        mBeforeExtracted = beforeExtracted;
        mAfterExtracted = afterExtracted;
        mOnStoreIntoDatabase = onStoreIntoDatabase;
    }

    public int getMethod() {
        return mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public TypeReference getTypeReference() {
        return mTypeReference;
    }

    public CallbackOne getBeforeExtracted() {
        return mBeforeExtracted;
    }

    public CallbackOneReturnable getAfterExtracted() {
        return mAfterExtracted;
    }

    public CallbackOne getOnStoreIntoDatabase() {
        return mOnStoreIntoDatabase;
    }
}