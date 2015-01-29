package ru.com.cardiomagnil.social;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.util.Tools;

public class AuthorizationDialog {

    private Context mContext;
    private BaseSocialApi mApi;
    private OAuthService mService;
    private final Token EMPTY_TOKEN = null;
    private AuthorizationListener mListener;
    private Dialog mDialog;
    private String mAuthorizationUrl;
    private WebView mWebView;

    public AuthorizationDialog(Context context, BaseSocialApi api) {
        mContext = context;
        mApi = api;

        mService = new ServiceBuilder().provider(mApi).apiKey(mApi.getClientId()).apiSecret(mApi.getApiSecret()).scope(mApi.getScope()).callback(mApi.getCallbackUrl()).build();

        mDialog = createDialog();
    }

    public void setProgressVisible(boolean visibility) {
        View view = mDialog.findViewById(R.id.progressBar);
        if (view != null)
            view.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    public Dialog createDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onAuthorizationCanceled();
                mDialog.dismiss();
            }
        });

        View view = View.inflate(mContext, R.layout.dialog_authorization, null);
        builder.setView(view);

        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(PluginState.ON);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new WebClient());

        Dialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // CustomTypeface.setTypeface(dialog);

        return dialog;
    }

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String hash = new BigInteger(1, md.digest(str.getBytes())).toString(16);
            if (hash.length() % 2 != 0)
                hash = "0" + hash;
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }

        return "";
    }

    public void show(AuthorizationListener listener) {
        mListener = listener;
        mAuthorizationUrl = mService.getAuthorizationUrl(EMPTY_TOKEN);

        SharedPreferences prefs = mContext.getSharedPreferences(md5(mAuthorizationUrl), Context.MODE_PRIVATE);
        Token accessToken = new Token(prefs.getString("token", ""), prefs.getString("secret", ""));
        mApi.restoreSpecifiedParams(prefs.getString("extra", ""));

        if (!accessToken.isEmpty()) {
            onObtainedAccessToken(accessToken, true);
        } else {
            mWebView.loadUrl(mAuthorizationUrl);
            mDialog.show();
        }
    }

    private Map<String, String> getParsedParams(String url) {
        int index = StringUtils.indexOfAny(url, "?#");
        String query = (index == -1 ? "" : url.substring(index + 1));

        List<String> argList = Arrays.asList(StringUtils.split(query, "&"));
        Map<String, String> map = new HashMap<String, String>();
        for (String arg : argList) {
            String[] pair = StringUtils.split(arg, "=");
            map.put(pair[0], pair.length > 1 ? pair[1] : "");
        }

        return map;
    }

    private class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                /*
                 * WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                 * lp.copyFrom(mDialog.getWindow().getAttributes()); lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                 * lp.height = WindowManager.LayoutParams.MATCH_PARENT; mDialog.getWindow().setAttributes(lp);
                 */
                // WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

                Map<String, String> params = getParsedParams(url);

                if (!url.startsWith(mApi.getCallbackUrl()))
                    return super.shouldOverrideUrlLoading(view, url);

                if (params.containsKey("error")) {
                    Tools.showToast(mContext, R.string.authorization_error, Toast.LENGTH_LONG);
                    mDialog.dismiss();
                } else if (params.containsKey("code")) {
                    retrieveAccessToken(params.get("code"));
                }

            } catch (Exception e) {
                e.printStackTrace();
                ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);

                Tools.showToast(mContext, "Unexpected error", Toast.LENGTH_LONG);
                mDialog.dismiss();
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            setProgressVisible(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!url.startsWith(mApi.getCallbackUrl()))
                setProgressVisible(false);

            WindowManager wm = (WindowManager) mDialog.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            // view.getLayoutParams().height = display.getHeight();
            // view.setMinimumHeight(display.getHeight());
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            Tools.showToast(mContext, description, Toast.LENGTH_LONG);
            mDialog.dismiss();
        }
    }

    private void retrieveAccessToken(final String code) {
        setProgressVisible(true);

        new AsyncTask<Void, Void, Token>() {
            @Override
            protected Token doInBackground(Void... args) {
                try {
                    Verifier verifier = new Verifier(code);
                    return mService.getAccessToken(EMPTY_TOKEN, verifier);
                } catch (Exception e) {
                    e.printStackTrace();
                    ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Token accessToken) {
                if (accessToken != null) {
                    onObtainedAccessToken(accessToken, false);
                } else {
                    Tools.showToast(mContext, R.string.authorization_error, Toast.LENGTH_LONG);
                    setProgressVisible(false);
                }
            }
        }.execute();
    }

    protected void onObtainedAccessToken(final Token accessToken, final boolean retryOnError) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... args) {
                try {
                    OAuthRequest request = new OAuthRequest(Verb.GET, mApi.getMethodUserInfoUrl());
                    mService.signRequest(accessToken, request);
                    Response response = request.send();

                    return response.getBody();
                } catch (Exception e) {
                    e.printStackTrace();
                    ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String userInfo) {
                if (userInfo != null) {
                    SharedPreferences.Editor prefs = mContext.getSharedPreferences(md5(mAuthorizationUrl), Context.MODE_PRIVATE).edit();
                    prefs.putString("token", accessToken.getToken()).putString("secret", accessToken.getSecret()).putString("extra", mApi.getSpecifiedParams()).commit();

                    mListener.onAuthorized(userInfo);
                } else if (retryOnError) {
                    mContext.getSharedPreferences(md5(mAuthorizationUrl), Context.MODE_PRIVATE).edit().remove(md5(mAuthorizationUrl)).commit();
                    show(mListener);
                } else {
                    mListener.onAuthorizationFailed();
                }

                mDialog.dismiss();
            }
        }.execute();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    protected OAuthService getService() {
        return mService;
    }

    protected BaseSocialApi getApi() {
        return mApi;
    }

    protected Context getContext() {
        return mContext;
    }
}