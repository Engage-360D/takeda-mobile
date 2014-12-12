package ru.com.cardiomagnil.social;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.application.Tools;

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
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }
}
