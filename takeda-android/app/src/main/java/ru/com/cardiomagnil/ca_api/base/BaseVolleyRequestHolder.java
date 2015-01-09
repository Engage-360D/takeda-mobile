package ru.com.cardiomagnil.ca_api.base;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.CallbackOneReturnable;

public abstract class BaseVolleyRequestHolder extends BaseRequestHolder {
    protected final int mMethod;
    protected final String mUrl;
    protected final Map<String, String> mParams;
    protected final TypeReference mTypeReference;
    protected final CallbackOne mOnBeforeExtract;
    protected final CallbackOneReturnable mOnAfterExtracted;
    protected final CallbackOne mOnStoreIntoDatabase;

    public BaseVolleyRequestHolder(final int method,
                                   final String url,
                                   final Map<String, String> params,
                                   final TypeReference typeReference,
                                   final CallbackOne onBeforeExtract,
                                   final CallbackOneReturnable onAfterExtracted,
                                   final CallbackOne onStoreIntoDatabase) {
        mMethod = method;
        mUrl = url;
        mParams = params;
        mTypeReference = typeReference;
        mOnBeforeExtract = onBeforeExtract;
        mOnAfterExtracted = onAfterExtracted;
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

    public CallbackOne getOnBeforeExtracted() {
        return mOnBeforeExtract;
    }

    public CallbackOneReturnable getOnAfterExtracted() {
        return mOnAfterExtracted;
    }

    public CallbackOne getOnStoreIntoDatabase() {
        return mOnStoreIntoDatabase;
    }
}