package ru.com.cardiomagnil.model;

import com.google.gson.JsonObject;

public class TestIncoming {
    // ///////////////////////////////////////////////////////////////
    // Fields of TestResult
    // ///////////////////////////////////////////////////////////////
    private String mSex = null; // "sex" : "female"
    private String mBirthday = null; // "birthday" : "1900-10-10"
    private Integer mGrowth = null; // "growth" : 185
    private Integer mWeight = null; // "weight" : 95
    private Boolean mSmoking = null; // "smoking" : true
    private Integer mCholesterolLevel = null; // "cholesterolLevel" : 8
    private Boolean mCholesterolDrugs = null; // "cholesterolDrugs" : true; notRequired
    private Boolean mDiabetes = null; // "diabetes" : true
    private Boolean mSugarProblems = null; // notRequired
    private Boolean mSugarDrugs = null; // notRequired
    private Integer mArterialPressure = null; // "arterialPressure" : 100
    private Boolean mArterialPressureDrugs = null; // notRequired
    private Integer mPhysicalActivity = null; // "physicalActivity" : 100
    private Boolean mHeartAttackOrStroke = null; // "heartAttackOrStroke" : true
    private Integer mExtraSalt = null; // "extraSalt" : true
    private Boolean mAcetylsalicylicDrugs = null; // "acetylsalicylicDrugs" : true

    // ///////////////////////////////////////////////////////////////

    public enum RESULT_GROUPS {
        all, first, second, third
    };

    public void setSex(String sex) {
        mSex = sex;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }

    public void setGrowth(Integer growth) {
        mGrowth = growth;
    }

    public void setWeight(Integer weight) {
        mWeight = weight;
    }

    public void setSmoking(Boolean smoking) {
        mSmoking = smoking;
    }

    public void setCholesterolLevel(Integer cholesterolLevel) {
        mCholesterolLevel = cholesterolLevel;
    }

    public void setCholesterolDrugss(Boolean cholesterolDrugs) {
        mCholesterolDrugs = cholesterolDrugs;
    }

    public void setDiabetes(Boolean diabetes) {
        mDiabetes = diabetes;
    }

    public void setSugarProblems(Boolean sugarProblems) {
        mSugarProblems = sugarProblems;
    }

    public void setSugarDrugs(Boolean sugarDrugs) {
        mSugarDrugs = sugarDrugs;
    }

    public void setArterialPressure(Integer arterialPressure) {
        mArterialPressure = arterialPressure;
    }

    public void setArterialPressureDrugs(Boolean arterialPressureDrugs) {
        mArterialPressureDrugs = arterialPressureDrugs;
    }

    public void setPhysicalActivity(Integer physicalActivity) {
        mPhysicalActivity = physicalActivity;
    }

    public void setHeartAttackOrStroke(Boolean heartAttackOrStroke) {
        mHeartAttackOrStroke = heartAttackOrStroke;
    }

    public void setExtraSalt(Integer extraSalt) {
        mExtraSalt = extraSalt;
    }

    public void setAcetylsalicylicDrugs(Boolean acetylsalicylicDrugs) {
        mAcetylsalicylicDrugs = acetylsalicylicDrugs;
    }

    public JsonObject getAsJson() {
        // if (!validate()) {
        // return null;
        // }

        JsonObject jsonObjectTestResult = new JsonObject();
        jsonObjectTestResult.addProperty("sex", mSex);
        jsonObjectTestResult.addProperty("birthday", mBirthday);
        jsonObjectTestResult.addProperty("growth", mGrowth);
        jsonObjectTestResult.addProperty("weight", mWeight);
        jsonObjectTestResult.addProperty("smoking", mSmoking);
        jsonObjectTestResult.addProperty("cholesterolLevel", mCholesterolLevel);
        jsonObjectTestResult.addProperty("cholesterolDrugs", mCholesterolDrugs);
        jsonObjectTestResult.addProperty("diabetes", mDiabetes);
        jsonObjectTestResult.addProperty("sugarProblems", mSugarProblems);
        jsonObjectTestResult.addProperty("sugarDrugs", mSugarDrugs);
        jsonObjectTestResult.addProperty("arterialPressure", mArterialPressure);
        jsonObjectTestResult.addProperty("arterialPressureDrugs", mArterialPressureDrugs);
        jsonObjectTestResult.addProperty("physicalActivity", mPhysicalActivity);
        jsonObjectTestResult.addProperty("heartAttackOrStroke", mHeartAttackOrStroke);
        jsonObjectTestResult.addProperty("extraSalt", mExtraSalt);
        jsonObjectTestResult.addProperty("acetylsalicylicDrugs", mAcetylsalicylicDrugs);
        JsonObject jsonObjectTestResultFinal = new JsonObject();
        jsonObjectTestResultFinal.add("testResult", jsonObjectTestResult);

        return jsonObjectTestResultFinal;
    }

    /**
     * Only required fields != null
     *
     */
    public boolean validate(RESULT_GROUPS group) {
        boolean result = true;

        if (group == RESULT_GROUPS.first || group == RESULT_GROUPS.all) {
            result = (mSex != null) &&
            /*     */(mBirthday != null) &&
            /*     */(mGrowth != null) &&
            /*     */(mWeight != null) &&
            /*     */(mSmoking != null) &&
            /*     */(mCholesterolLevel != null);
        }

        if (group == RESULT_GROUPS.second || group == RESULT_GROUPS.all) {
            result = (result) &&
            /*     */(mDiabetes != null) &&
            /*     */(mArterialPressure != null) &&
            /*     */(mPhysicalActivity != null) &&
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
