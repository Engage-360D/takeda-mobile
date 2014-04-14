package ru.com.cardiomagnil.social;

import ru.com.cardiomagnil.application.Tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class OkUser extends User {
    public OkUser(JsonObject jsonUser) {
        super(jsonUser);
    }

    public OkUser(String stringUser) {
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

            // email - fake field
            // TODO": get email for OK
            JsonElement jsonEmail = Tools.jsonElementByMemberName(jsonUser, "email");
            setEmail((jsonEmail != null) ? jsonEmail.getAsString() : "");

            JsonElement jsonLocation = Tools.jsonElementByMemberName(jsonUser, "location");
            JsonObject jsonObjectLocation = ((jsonLocation != null) ? jsonLocation.getAsJsonObject() : new JsonObject());

            JsonElement jsonCountry = Tools.jsonElementByMemberName(jsonObjectLocation, "country");
            setCountry((jsonCountry != null) ? jsonCountry.getAsString() : "");

            JsonElement jsonBirthday = Tools.jsonElementByMemberName(jsonUser, "birthday");
            setBirthday((jsonBirthday != null) ? jsonBirthday.getAsString() : "");

            JsonElement jsonGender = Tools.jsonElementByMemberName(jsonUser, "gender");
            setGender((jsonGender != null) ? jsonGender.getAsString() : "");

            mIsInitialized = true;
        } catch (Exception e) {
            // TODO: exception_handler
            e.printStackTrace();
            // ExeptionsHandler.getInstatce().handleException(WinstuffApplication.getAppContext(), e);
        }
    }
}
