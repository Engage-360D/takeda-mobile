package ru.com.cardiomagnil.model;

import com.google.gson.JsonObject;

//TODO: add constructor(jsonUser), constructor(jsonString)
// TODO: add id field
public class User {
    // ///////////////////////////////////////////////////////////////
    // Fields of User
    // ///////////////////////////////////////////////////////////////
    // required fields
    private Boolean mConfirmInformation = null; // "confirmInformation" : 1
    private Boolean mConfirmPersonalization = null; // "confirmPersonalization" : 1
    private Boolean mConfirmSubscription = null; //
    private String mFirstName = null; // "firstname" : "Имя"
    private String mBirthday = null; // "birthday" : "2014-04-16"
    private String mEmail = null; // "email" : "email@example.com"
    private String mRegion = null; // "region" : "Республика Саха"
    private String mPlainPassword = null; // "plainPassword" : {"first" : "password", "second" : "password"},
    private Boolean mDoctor = null; // "doctor" : 1

    // doctor's fields
    private String mSpecialization = null; // "specialization" : "Специализация"
    private String mExperience = null; // "experience" : "2" ??????????????
    private String mAddress = null; // "address" : "Адрес"
    private String mPhone = null; // "phone" : "89030330303
    private String mInstitution = null; // "institution" : "Учебное заведение"
    private String mGraduation = null; // "graduation" : "1950-01-01"

    // ///////////////////////////////////////////////////////////////

    public void setConfirmInformation(Boolean confirmInformation) {
        mConfirmInformation = confirmInformation;
    }

    public void setConfirmPersonalization(Boolean confirmPersonalization) {
        mConfirmPersonalization = confirmPersonalization;
    }

    public void setConfirmSubscription(Boolean confirmSubscription) {
        mConfirmSubscription = confirmSubscription;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setRegionl(String region) {
        mRegion = region;
    }

    public String getPlainPassword() {
        return mPlainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        mPlainPassword = plainPassword;
    }

    public void setDoctor(Boolean doctor) {
        mDoctor = doctor;
    }

    public void setSpecialization(String specialization) {
        mSpecialization = specialization;
    }

    public void setExperience(String experience) {
        mExperience = experience;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public void setInstitution(String institution) {
        mInstitution = institution;
    }

    public void setGraduation(String graduation) {
        mGraduation = graduation;
    }

    public JsonObject getAsJson() {
        // if (!validate()) {
        // return null;
        // }

        JsonObject jsonObjectUser = new JsonObject();

        // required fields
        jsonObjectUser.addProperty("confirmInformation", mConfirmInformation);
        jsonObjectUser.addProperty("confirmPersonalization", mConfirmPersonalization);
        jsonObjectUser.addProperty("confirmSubscription", mConfirmSubscription);
        jsonObjectUser.addProperty("firstname", mFirstName);
        jsonObjectUser.addProperty("birthday", mBirthday);
        jsonObjectUser.addProperty("email", mEmail);
        jsonObjectUser.addProperty("region", mRegion);
        jsonObjectUser.addProperty("plainPassword", mPlainPassword);
        jsonObjectUser.addProperty("doctor", mDoctor);

        if (mDoctor != null && mDoctor) {
            // doctor's fields
            jsonObjectUser.addProperty("specialization", mSpecialization);
            jsonObjectUser.addProperty("experience", mExperience);
            jsonObjectUser.addProperty("address", mAddress);
            jsonObjectUser.addProperty("phone", mPhone);
            jsonObjectUser.addProperty("institution", mInstitution);
            jsonObjectUser.addProperty("graduation", mGraduation);
        }

        return jsonObjectUser;
    }

    /**
     * All User's fields != null
     */
    public boolean validate(boolean validateForLogin) {
        boolean result = false;

        if (validateForLogin) {
            result = (mEmail != null) &&
            /*     */(mPlainPassword != null);
        } else {
            if (mDoctor != null) {
                result = (mConfirmInformation != null) &&
                /*     */(mConfirmPersonalization != null) &&
                /*     */(mConfirmSubscription != null) &&
                /*     */(mFirstName != null) &&
                /*     */(mBirthday != null) &&
                /*     */(mEmail != null) &&
                /*     */(mRegion != null) &&
                /*     */(mPlainPassword != null);

                if (mDoctor) {
                    result = (result) &&
                    /*     */(mSpecialization != null) &&
                    /*     */(mExperience != null) &&
                    /*     */(mAddress != null) &&
                    /*     */(mPhone != null) &&
                    /*     */(mInstitution != null) &&
                    /*     */(mGraduation != null);
                }

            }
        }

        return result;
    }

    @SuppressWarnings("unused")
    private void testUser() {
        User user = new User();
        user.setConfirmInformation(true);
        user.setConfirmPersonalization(true);
        user.setConfirmSubscription(true);
        user.setFirstName("Имя");
        user.setBirthday("2014-04-16");
        user.setEmail("email@example.com");
        user.setRegionl("Республика Саха");
        user.setSpecialization("Специализация");
        user.setExperience("2");
        user.setAddress("Адрес");
        user.setPhone("89030330303");
        user.setInstitution("Учебное заведение");
        user.setPlainPassword("password");
        user.setDoctor(false);
        user.setGraduation("1950-01-01");

        boolean isValid = user.validate(false);
        JsonObject jsonObject = user.getAsJson();
    }

    // {
    // "id" : 32,
    // "username" : "dbh@gvc.h",
    // "email" : "dbh@gvc.h",
    // "enabled" : false,
    // "lastLogin" : "2014-04-07T16:15:21+0400",
    // "firstname" : "yyyffsw",
    // "lastname" : null,
    // "facebookId" : null,
    // "facebookAccessToken" : null,
    // "doctor" : true,
    // "birthday" : "2014-04-30T00:00:00+0400",
    // "region" : "qwe",
    // "registration" : "2014-04-07T16:15:21+0400",
    // "specialization" : null,
    // "experience" : null,
    // "address" : null,
    // "phone" : null,
    // "institution" : null,
    // "graduation" : null,
    // "testResults" : [],
    // "confirmInformation" : true,
    // "confirmPersonalization" : true,
    // "confirmSubscription" : false,
    // "vkontakteId" : null,
    // "vkontakteAccessToken" : null
    // }

}
