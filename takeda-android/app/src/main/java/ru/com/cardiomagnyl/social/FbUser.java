package ru.com.cardiomagnyl.social;

import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.application.ExceptionsHandler;
import ru.com.cardiomagnyl.util.Tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FbUser extends User {
    public FbUser(JsonObject jsonUser) {
        super(jsonUser);
    }

    public FbUser(String stringUser) {
        super(stringUser);
    }

    @Override
    public boolean isAuthentificated() {
        return true;
    }

    @Override
    protected void initUser(JsonObject jsonUser) {
        mIsInitialized = false;

        try {
            if (jsonUser == null || jsonUser.isJsonNull()) {
                return;
            }

            JsonElement jsonElementFirstName = Tools.jsonElementByMemberName(jsonUser, "first_name");
            setFirstName((jsonElementFirstName != null) ? jsonElementFirstName.getAsString() : "");

            JsonElement jsonElementLast = Tools.jsonElementByMemberName(jsonUser, "last_name");
            setLastName((jsonElementLast != null) ? jsonElementLast.getAsString() : "");

            JsonElement jsonEmail = Tools.jsonElementByMemberName(jsonUser, "email");
            setEmail((jsonEmail != null) ? jsonEmail.getAsString() : "");

            // country - fake field
            // TODO": get country (region) for FB
            JsonElement jsonCountry = Tools.jsonElementByMemberName(jsonUser, "country");
            setCountry((jsonCountry != null) ? jsonCountry.getAsString() : "");

            JsonElement jsonBirthday = Tools.jsonElementByMemberName(jsonUser, "birthday");
            String birthday = (jsonBirthday != null) ? jsonBirthday.getAsString() : "";
            String[] birthdaySplitted = birthday.split("/");
            setBirthday(birthdaySplitted.length == 3 ? birthdaySplitted[2] + "-" + birthdaySplitted[0] + "-" + birthdaySplitted[1] : "");

            JsonElement jsonGender = Tools.jsonElementByMemberName(jsonUser, "gender");
            setGender((jsonGender != null) ? jsonGender.getAsString() : "");

            mIsInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionsHandler.getInstatce().handleException(CardiomagnylApplication.getAppContext(), e);
        }
    }
}
