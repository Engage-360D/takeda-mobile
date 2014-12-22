package ru.com.cardiomagnil.social;

import org.scribe.builder.api.DefaultApi20;

public abstract class BaseSocialApi extends DefaultApi20 {
    public abstract String getClientId();

    public abstract String getApiSecret();

    public abstract String getScope();

    public abstract String getCallbackUrl();

    public abstract String getMethodUserInfoUrl();

    public abstract String getSpecifiedParams();

    public abstract void restoreSpecifiedParams(String params);

}
