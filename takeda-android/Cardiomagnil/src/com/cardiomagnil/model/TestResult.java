package com.cardiomagnil.model;

import com.google.gson.JsonObject;

public class TestResult {
    // ///////////////////////////////////////////////////////////////
    // Fields of TestResult
    // ///////////////////////////////////////////////////////////////
    private Boolean mAcetylsalicylicDrugs = null; // "acetylsalicylicDrugs" : true
    private Integer mArterialPressure = null; // "arterialPressure" : 100
    private String mBirthday = null; // "birthday" : "1900-10-10"
    private Boolean mCholesterolDrugs = null; // "cholesterolDrugs" : true
    private Integer mCholesterolLevel = null; // "cholesterolLevel" : 8
    private Boolean mDiabetes = null; // "diabetes" : true
    private Boolean mExtraSalt = null; // "extraSalt" : true
    private Integer mGrowth = null; // "growth" : 185
    private Boolean mHeartAttackOrStroke = null; // "heartAttackOrStroke" : true
    private Integer mPhysicalActivity = null; // "physicalActivity" : 100
    private String mSex = null; // "sex" : "female"
    private Boolean mSmoking = null; // "smoking" : true
    private Integer mWeight = null; // "weight" : 95

    // ///////////////////////////////////////////////////////////////

    public enum RESULT_GROUPS {
        all, first, second, third
    };

    public void setAcetylsalicylicDrugs(Boolean acetylsalicylicDrugs) {
        mAcetylsalicylicDrugs = acetylsalicylicDrugs;
    }

    public void setArterialPressure(Integer arterialPressure) {
        mArterialPressure = arterialPressure;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }

    public void setCholesterolDrugss(Boolean cholesterolDrugs) {
        mCholesterolDrugs = cholesterolDrugs;
    }

    public void setCholesterolLevel(Integer cholesterolLevel) {
        mCholesterolLevel = cholesterolLevel;
    }

    public void setDiabetes(Boolean diabetes) {
        mDiabetes = diabetes;
    }

    public void setExtraSalt(Boolean extraSalt) {
        mExtraSalt = extraSalt;
    }

    public void setGrowth(Integer growth) {
        mGrowth = growth;
    }

    public void setHeartAttackOrStroke(Boolean heartAttackOrStroke) {
        mHeartAttackOrStroke = heartAttackOrStroke;
    }

    public void setPhysicalActivity(Integer physicalActivity) {
        mPhysicalActivity = physicalActivity;
    }

    public void setSex(String sex) {
        mSex = sex;
    }

    public void setSmoking(Boolean smoking) {
        mSmoking = smoking;
    }

    public void setWeight(Integer weight) {
        mWeight = weight;
    }

    // private Integer mWeight = null; // "weight" : 95

    public JsonObject asJson() {
        // if (!validate()) {
        // return null;
        // }

        JsonObject jsonObjectTestResult = new JsonObject();
        jsonObjectTestResult.addProperty("acetylsalicylicDrugs", mAcetylsalicylicDrugs);
        jsonObjectTestResult.addProperty("arterialPressure", mArterialPressure);
        jsonObjectTestResult.addProperty("birthday", mBirthday);
        jsonObjectTestResult.addProperty("cholesterolDrugs", mCholesterolDrugs);
        jsonObjectTestResult.addProperty("cholesterolLevel", mCholesterolLevel);
        jsonObjectTestResult.addProperty("diabetes", mDiabetes);
        jsonObjectTestResult.addProperty("extraSalt", mExtraSalt);
        jsonObjectTestResult.addProperty("growth", mGrowth);
        jsonObjectTestResult.addProperty("heartAttackOrStroke", mHeartAttackOrStroke);
        jsonObjectTestResult.addProperty("physicalActivity", mPhysicalActivity);
        jsonObjectTestResult.addProperty("sex", mSex);
        jsonObjectTestResult.addProperty("smoking", mSmoking);
        jsonObjectTestResult.addProperty("weight", mWeight);

        JsonObject jsonObjectTestResultFinal = new JsonObject();
        jsonObjectTestResultFinal.add("testResult", jsonObjectTestResult);

        return jsonObjectTestResultFinal;
    }

    // TODO: аэробные/анаэробные, лекарства от давления
    /**
     * All User's fields != null
     *
     */
    public boolean validate(RESULT_GROUPS group) {
        boolean result = true;

        if (group == RESULT_GROUPS.first || group == RESULT_GROUPS.all) {
            result = (mSex != null) &&
            /*     */(mBirthday != null) &&
            /*     */(mGrowth != null) &&
            /*     */(mWeight != null) &&
            /*     */(mCholesterolLevel != null) &&
            /*     */(mSmoking != null);
        }

        if (group == RESULT_GROUPS.second || group == RESULT_GROUPS.all) {
            result = (result) &&
            /*     */(mDiabetes != null) &&
            /*     */(mCholesterolDrugs != null) &&
            /*     */(mArterialPressure != null) &&
            /* (?? != null) && */
            /*     */(mPhysicalActivity != null) &&
            /* (?? != null) && */
            /*     */(mHeartAttackOrStroke != null);
        }

        if (group == RESULT_GROUPS.third || group == RESULT_GROUPS.all) {
            result = (result) &&
            /*     */(mExtraSalt != null) &&
            /*     */(mAcetylsalicylicDrugs != null);
        }

        return result;
    }
}
