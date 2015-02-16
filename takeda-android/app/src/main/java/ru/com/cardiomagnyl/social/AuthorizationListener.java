package ru.com.cardiomagnyl.social;

public interface AuthorizationListener {
	public void onAuthorized(String userInfo);
	public void onAuthorizationFailed();
	public void onAuthorizationCanceled();
}
