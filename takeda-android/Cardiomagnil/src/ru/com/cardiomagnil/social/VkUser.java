package ru.com.cardiomagnil.social;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.application.Tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class VkUser extends User {
    public VkUser(JsonObject jsonUser) {
        super(jsonUser);
    }

    public VkUser(String stringUser) {
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

            JsonArray jsonResponseArray = Tools.jsonElementByMemberName(jsonUser, "response").getAsJsonArray();
            JsonObject jsonResponse = jsonResponseArray.get(0).getAsJsonObject();

            JsonElement jsonElementFirstName = Tools.jsonElementByMemberName(jsonResponse, "first_name");
            setFirstName((jsonElementFirstName != null) ? jsonElementFirstName.getAsString() : "");

            JsonElement jsonElementLast = Tools.jsonElementByMemberName(jsonResponse, "last_name");
            setLastName((jsonElementLast != null) ? jsonElementLast.getAsString() : "");

            // email - fake field
            // TODO": get email for VK
            JsonElement jsonEmail = Tools.jsonElementByMemberName(jsonResponse, "email");
            setEmail((jsonEmail != null) ? jsonEmail.getAsString() : "");

            // country - fake field
            // TODO": get country for VK
            JsonElement jsonCountry = Tools.jsonElementByMemberName(jsonResponse, "country");
            setCountry((jsonCountry != null) ? jsonCountry.getAsString() : "");

            // bdate - fake field (?)
            // TODO": get bdate for VK
            JsonElement jsonBirthday = Tools.jsonElementByMemberName(jsonResponse, "bdate");
            setBirthday((jsonBirthday != null) ? jsonBirthday.getAsString() : "");

            JsonElement jsonGender = Tools.jsonElementByMemberName(jsonResponse, "sex");
            int vk_sex = (jsonGender != null) ? jsonGender.getAsInt() : 0;
            setGender((vk_sex == 0) ? "" : (vk_sex == 1 ? "famale" : "male"));

            mIsInitialized = true;
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }

}
