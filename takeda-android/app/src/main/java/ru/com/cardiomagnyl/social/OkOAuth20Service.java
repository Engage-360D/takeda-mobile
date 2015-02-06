package ru.com.cardiomagnyl.social;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.application.ExceptionsHandler;

public class OkOAuth20Service extends OAuth20ServiceImpl {
	private OkApi mApi;
	private OAuthConfig mConfig;

	public OkOAuth20Service(OkApi api, OAuthConfig config) {
		super(api, config);
		mApi = api;
		mConfig = config;
	}

	@Override
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		OAuthRequest request = new OAuthRequest(mApi.getAccessTokenVerb(), mApi.getAccessTokenEndpoint());
		request.addBodyParameter(OAuthConstants.CLIENT_ID, mConfig.getApiKey());
		request.addBodyParameter(OAuthConstants.CLIENT_SECRET, mConfig.getApiSecret());
		request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
		request.addBodyParameter(OAuthConstants.REDIRECT_URI, mConfig.getCallback());
		request.addBodyParameter("grant_type", "authorization_code");
		Response response = request.send();
		return mApi.getAccessTokenExtractor().extract(response.getBody());
	}

	@Override
	public void signRequest(Token accessToken, OAuthRequest request) {
		try {
			List<String> args = Arrays.asList(request.getCompleteUrl().replace(mApi.getApiBaseUrl(), "").split("&"));

			CollectionUtils.transform(args, new Transformer() {
				@Override
				public Object transform(Object arg) {
					String str = (String)arg;
					String key = str.split("=")[0];
					String value = URLDecoder.decode(str.split("=")[1]);
					return key + "=" + value;
				}
			});

			Collections.sort(args, new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					String name1 = lhs.split("=")[0];
					String name2 = rhs.split("=")[0];
					return name1.compareTo(name2);
				}
			});

			String sessionSecret = accessToken.getToken() + mApi.getSecretKey();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			String sessionSecretMd5 = new BigInteger(1, md5.digest(sessionSecret.getBytes())).toString(16);
			if (sessionSecretMd5.length() % 2 != 0)
				sessionSecretMd5 = "0" + sessionSecretMd5;

			String argsString = StringUtils.join(args, "");
			sessionSecret = argsString + sessionSecretMd5;
			String sig = new BigInteger(1, md5.digest(sessionSecret.getBytes())).toString(16);
			if (sig.length() % 2 != 0)
				sig = "0" + sig;

			request.addQuerystringParameter("sig", sig);
			request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			ExceptionsHandler.getInstatce().handleException(CardiomagnylApplication.getAppContext(), e);
		}
	}

}
