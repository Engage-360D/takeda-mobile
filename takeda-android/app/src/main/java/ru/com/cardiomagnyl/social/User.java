package ru.com.cardiomagnyl.social;

import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.application.ExceptionsHandler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class User {
    // ///////////////////////////////////////////////////////////////
    // Fields of User
    // ///////////////////////////////////////////////////////////////
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mCountry;
    private String mBirthday;
    private String mGender;
    // ///////////////////////////////////////////////////////////////

    protected boolean mIsInitialized;
    private JsonObject mJsonUser;

    private static User sInstance;
    public static int NO_USER_ID = -1;

    public User(String stringUser) {
        initUser(stringUser);
    }

    public User(JsonObject jsonUser) {
        initUser(jsonUser);
        mJsonUser = jsonUser;
    }

    public static void setInstance(User user) {
        sInstance = user;
    }

    public static User getInstance() {
        return sInstance;
    }

    public abstract boolean isAuthentificated();

    public String getFirstName() {
        return mFirstName;
    }

    protected void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    protected void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    protected void setEmail(String email) {
        mEmail = email;
    }

    public String getCountry() {
        return mCountry;
    }

    protected void setCountry(String country) {
        mCountry = country;
    }

    public String getBirthday() {
        return mBirthday;
    }

    protected void setBirthday(String birthday) {
        mBirthday = birthday;
    }

    public String getGender() {
        return mGender;
    }

    protected void setGender(String gender) {
        mGender = gender;
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public JsonObject getAsJson() {
        return mJsonUser;
    }

    private void initUser(String stringUser) {
        mIsInitialized = false;

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = (JsonElement) jsonParser.parse(stringUser);
            JsonObject jsonObject = jsonElement.isJsonObject() ? jsonElement.getAsJsonObject() : null;
            mJsonUser = jsonObject;
            initUser(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionsHandler.getInstatce().handleException(CardiomagnylApplication.getAppContext(), e);
        }
    }

    protected abstract void initUser(JsonObject jsonToken);
}
