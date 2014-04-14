package ru.com.cardiomagnil.social;

public interface AuthorizationListener {
	public void onAuthorized(String userInfo);
	public void onAuthorizationFailed();
	public void onAuthorizationCanceled();
}
