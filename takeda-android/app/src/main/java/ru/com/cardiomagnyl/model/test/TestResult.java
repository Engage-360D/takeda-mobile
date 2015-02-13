package ru.com.cardiomagnyl.model.test;

import android.util.Pair;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Arrays;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "sex",
        "birthday",
        "growth",
        "weight",
        "isSmoker",
        "cholesterolLevel",
        "isCholesterolDrugsConsumer",
        "hasDiabetes",
        "hadSugarProblems",
        "isSugarDrugsConsumer",
        "arterialPressure",
        "isArterialPressureDrugsConsumer",
        "physicalActivityMinutes",
        "hadHeartAttackOrStroke",
        "isAddingExtraSalt",
        "isAcetylsalicylicDrugsConsumer",
        "bmi",
        "score",
        "recommendations",
        "createdAt"
})
public class TestResult extends BaseModel {
    private static final ArrayList<Pair<String, Integer>> MAX_SCORE =
            new ArrayList<>(Arrays.asList(
                    new Pair<String, Integer>("female", 20),
                    new Pair<String, Integer>("male", 47)));

    @JsonProperty("id")
    private String id;
    @JsonProperty("sex")
    private String sex;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("growth")
    private int growth;
    @JsonProperty("weight")
    private int weight;
    @JsonProperty("isSmoker")
    private boolean isSmoker;
    @JsonProperty("cholesterolLevel")
    private int cholesterolLevel;
    @JsonProperty("isCholesterolDrugsConsumer")
    private boolean isCholesterolDrugsConsumer;
    @JsonProperty("hasDiabetes")
    private boolean hasDiabetes;
    @JsonProperty("hadSugarProblems")
    private boolean hadSugarProblems;
    @JsonProperty("isSugarDrugsConsumer")
    private int isSugarDrugsConsumer;
    @JsonProperty("arterialPressure")
    private int arterialPressure;
    @JsonProperty("isArterialPressureDrugsConsumer")
    private boolean isArterialPressureDrugsConsumer;
    @JsonProperty("physicalActivityMinutes")
    private int physicalActivityMinutes;
    @JsonProperty("hadHeartAttackOrStroke")
    private boolean hadHeartAttackOrStroke;
    @JsonProperty("isAddingExtraSalt")
    private boolean isAddingExtraSalt;
    @JsonProperty("isAcetylsalicylicDrugsConsumer")
    private boolean isAcetylsalicylicDrugsConsumer;
    @JsonProperty("bmi")
    private double bmi;
    @JsonProperty("score")
    private int score;
    @JsonProperty("recommendations")
    private Recommendations recommendations;
    @JsonProperty("createdAt")
    private String createdAt;

    public enum STATES {
        attention, ok, bell, doctor, ask, empty, undefined
    }

    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The sex
     */
    @JsonProperty("sex")
    public String getSex() {
        return sex;
    }

    /**
     * @param sex The sex
     */
    @JsonProperty("sex")
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return The birthday
     */
    @JsonProperty("birthday")
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday The birthday
     */
    @JsonProperty("birthday")
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @return The growth
     */
    @JsonProperty("growth")
    public int getGrowth() {
        return growth;
    }

    /**
     * @param growth The growth
     */
    @JsonProperty("growth")
    public void setGrowth(int growth) {
        this.growth = growth;
    }

    /**
     * @return The weight
     */
    @JsonProperty("weight")
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    @JsonProperty("weight")
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return The isSmoker
     */
    @JsonProperty("isSmoker")
    public boolean isIsSmoker() {
        return isSmoker;
    }

    /**
     * @param isSmoker The isSmoker
     */
    @JsonProperty("isSmoker")
    public void setIsSmoker(boolean isSmoker) {
        this.isSmoker = isSmoker;
    }

    /**
     * @return The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public int getCholesterolLevel() {
        return cholesterolLevel;
    }

    /**
     * @param cholesterolLevel The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public void setCholesterolLevel(int cholesterolLevel) {
        this.cholesterolLevel = cholesterolLevel;
    }

    /**
     * @return The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public boolean isIsCholesterolDrugsConsumer() {
        return isCholesterolDrugsConsumer;
    }

    /**
     * @param isCholesterolDrugsConsumer The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public void setIsCholesterolDrugsConsumer(boolean isCholesterolDrugsConsumer) {
        this.isCholesterolDrugsConsumer = isCholesterolDrugsConsumer;
    }

    /**
     * @return The hasDiabetes
     */
    @JsonProperty("hasDiabetes")
    public boolean isHasDiabetes() {
        return hasDiabetes;
    }

    /**
     * @param hasDiabetes The hasDiabetes
     */
    @JsonProperty("hasDiabetes")
    public void setHasDiabetes(boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }

    /**
     * @return The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public boolean isHadSugarProblems() {
        return hadSugarProblems;
    }

    /**
     * @param hadSugarProblems The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public void setHadSugarProblems(boolean hadSugarProblems) {
        this.hadSugarProblems = hadSugarProblems;
    }

    /**
     * @return The isSugarDrugsConsumer
     */
    @JsonProperty("isSugarDrugsConsumer")
    public int getIsSugarDrugsConsumer() {
        return isSugarDrugsConsumer;
    }

    /**
     * @param isSugarDrugsConsumer The isSugarDrugsConsumer
     */
    @JsonProperty("isSugarDrugsConsumer")
    public void setIsSugarDrugsConsumer(int isSugarDrugsConsumer) {
        this.isSugarDrugsConsumer = isSugarDrugsConsumer;
    }

    /**
     * @return The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public int getArterialPressure() {
        return arterialPressure;
    }

    /**
     * @param arterialPressure The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public void setArterialPressure(int arterialPressure) {
        this.arterialPressure = arterialPressure;
    }

    /**
     * @return The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public boolean isIsArterialPressureDrugsConsumer() {
        return isArterialPressureDrugsConsumer;
    }

    /**
     * @param isArterialPressureDrugsConsumer The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public void setIsArterialPressureDrugsConsumer(boolean isArterialPressureDrugsConsumer) {
        this.isArterialPressureDrugsConsumer = isArterialPressureDrugsConsumer;
    }

    /**
     * @return The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public int getPhysicalActivityMinutes() {
        return physicalActivityMinutes;
    }

    /**
     * @param physicalActivityMinutes The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public void setPhysicalActivityMinutes(int physicalActivityMinutes) {
        this.physicalActivityMinutes = physicalActivityMinutes;
    }

    /**
     * @return The hadHeartAttackOrStroke
     */
    @JsonProperty("hadHeartAttackOrStroke")
    public boolean isHadHeartAttackOrStroke() {
        return hadHeartAttackOrStroke;
    }

    /**
     * @param hadHeartAttackOrStroke The hadHeartAttackOrStroke
     */
    @JsonProperty("hadHeartAttackOrStroke")
    public void setHadHeartAttackOrStroke(boolean hadHeartAttackOrStroke) {
        this.hadHeartAttackOrStroke = hadHeartAttackOrStroke;
    }

    /**
     * @return The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public boolean isIsAddingExtraSalt() {
        return isAddingExtraSalt;
    }

    /**
     * @param isAddingExtraSalt The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public void setIsAddingExtraSalt(boolean isAddingExtraSalt) {
        this.isAddingExtraSalt = isAddingExtraSalt;
    }

    /**
     * @return The isAcetylsalicylicDrugsConsumer
     */
    @JsonProperty("isAcetylsalicylicDrugsConsumer")
    public boolean isIsAcetylsalicylicDrugsConsumer() {
        return isAcetylsalicylicDrugsConsumer;
    }

    /**
     * @param isAcetylsalicylicDrugsConsumer The isAcetylsalicylicDrugsConsumer
     */
    @JsonProperty("isAcetylsalicylicDrugsConsumer")
    public void setIsAcetylsalicylicDrugsConsumer(boolean isAcetylsalicylicDrugsConsumer) {
        this.isAcetylsalicylicDrugsConsumer = isAcetylsalicylicDrugsConsumer;
    }

    /**
     * @return The bmi
     */
    @JsonProperty("bmi")
    public double getBmi() {
        return bmi;
    }

    /**
     * @param bmi The bmi
     */
    @JsonProperty("bmi")
    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    /**
     * @return The score
     */
    @JsonProperty("score")
    public int getScore() {
        for (Pair<String, Integer> maxScore : MAX_SCORE)
            if (maxScore.first.equals(sex)) return score * 100 / maxScore.second;

        // TODO: maybe exception?
        return 0;
    }

    /**
     * @param score The score
     */
    @JsonProperty("score")
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return The recommendations
     */
    @JsonProperty("recommendations")
    public Recommendations getRecommendations() {
        return recommendations;
    }

    /**
     * @param recommendations The recommendations
     */
    @JsonProperty("recommendations")
    public void setRecommendations(Recommendations recommendations) {
        this.recommendations = recommendations;
    }

    /**
     * @return The createdAt
     */
    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The createdAt
     */
    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}