package ru.com.cardiomagnyl.model.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
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
        "isAcetylsalicylicDrugsConsumer"
})
public class TestSource extends BaseModel {

    public enum RESULT_GROUPS {
        all, first, second, third
    }

    @JsonProperty("sex")
    private String sex;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("growth")
    private Integer growth;
    @JsonProperty("weight")
    private Integer weight;
    @JsonProperty("isSmoker")
    private Boolean isSmoker;
    @JsonProperty("cholesterolLevel")
    private Integer cholesterolLevel;
    @JsonProperty("isCholesterolDrugsConsumer")
    private Boolean isCholesterolDrugsConsumer;
    @JsonProperty("hasDiabetes")
    private Boolean hasDiabetes;
    @JsonProperty("hadSugarProblems")
    private Boolean hadSugarProblems;
    @JsonProperty("isSugarDrugsConsumer")
    private Boolean isSugarDrugsConsumer;
    @JsonProperty("arterialPressure")
    private Integer arterialPressure;
    @JsonProperty("isArterialPressureDrugsConsumer")
    private Boolean isArterialPressureDrugsConsumer;
    @JsonProperty("physicalActivityMinutes")
    private Integer physicalActivityMinutes;
    @JsonProperty("hadHeartAttackOrStroke")
    private Boolean hadHeartAttackOrStroke;
    @JsonProperty("isAddingExtraSalt")
    private Boolean isAddingExtraSalt;
    @JsonProperty("isAcetylsalicylicDrugsConsumer")
    private Boolean isAcetylsalicylicDrugsConsumer;

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
    public Integer getGrowth() {
        return growth;
    }

    /**
     * @param growth The growth
     */
    @JsonProperty("growth")
    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    /**
     * @return The weight
     */
    @JsonProperty("weight")
    public Integer getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    @JsonProperty("weight")
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * @return The isSmoker
     */
    @JsonProperty("isSmoker")
    public Boolean isSmoker() {
        return isSmoker;
    }

    /**
     * @param isSmoker The isSmoker
     */
    @JsonProperty("isSmoker")
    public void setIsSmoker(Boolean isSmoker) {
        this.isSmoker = isSmoker;
    }

    /**
     * @return The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public Integer getCholesterolLevel() {
        return cholesterolLevel;
    }

    /**
     * @param cholesterolLevel The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public void setCholesterolLevel(Integer cholesterolLevel) {
        this.cholesterolLevel = cholesterolLevel;
    }

    /**
     * @return The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public Boolean isCholesterolDrugsConsumer() {
        return isCholesterolDrugsConsumer;
    }

    /**
     * @param isCholesterolDrugsConsumer The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public void setIsCholesterolDrugsConsumer(Boolean isCholesterolDrugsConsumer) {
        this.isCholesterolDrugsConsumer = isCholesterolDrugsConsumer;
    }

    /**
     * @return The hasDiabetes
     */
    @JsonProperty("hasDiabetes")
    public Boolean isHasDiabetes() {
        return hasDiabetes;
    }

    /**
     * @param hasDiabetes The hasDiabetes
     */
    @JsonProperty("hasDiabetes")
    public void setHasDiabetes(Boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }

    /**
     * @return The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public Boolean isHadSugarProblems() {
        return hadSugarProblems;
    }

    /**
     * @param hadSugarProblems The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public void setHadSugarProblems(Boolean hadSugarProblems) {
        this.hadSugarProblems = hadSugarProblems;
    }

    /**
     * @return The isSugarDrugsConsumer
     */
    @JsonProperty("isSugarDrugsConsumer")
    public Boolean isSugarDrugsConsumer() {
        return isSugarDrugsConsumer;
    }

    /**
     * @param isSugarDrugsConsumer The isSugarDrugsConsumer
     */
    @JsonProperty("isSugarDrugsConsumer")
    public void setIsSugarDrugsConsumer(Boolean isSugarDrugsConsumer) {
        this.isSugarDrugsConsumer = isSugarDrugsConsumer;
    }

    /**
     * @return The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public Integer getArterialPressure() {
        return arterialPressure;
    }

    /**
     * @param arterialPressure The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public void setArterialPressure(Integer arterialPressure) {
        this.arterialPressure = arterialPressure;
    }

    /**
     * @return The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public Boolean isArterialPressureDrugsConsumer() {
        return isArterialPressureDrugsConsumer;
    }

    /**
     * @param isArterialPressureDrugsConsumer The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public void setIsArterialPressureDrugsConsumer(Boolean isArterialPressureDrugsConsumer) {
        this.isArterialPressureDrugsConsumer = isArterialPressureDrugsConsumer;
    }

    /**
     * @return The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public Integer getPhysicalActivityMinutes() {
        return physicalActivityMinutes;
    }

    /**
     * @param physicalActivityMinutes The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public void setPhysicalActivityMinutes(Integer physicalActivityMinutes) {
        this.physicalActivityMinutes = physicalActivityMinutes;
    }

    /**
     * @return The hadHeartAttackOrStroke
     */
    @JsonProperty("hadHeartAttackOrStroke")
    public Boolean isHadHeartAttackOrStroke() {
        return hadHeartAttackOrStroke;
    }

    /**
     * @param hadHeartAttackOrStroke The hadHeartAttackOrStroke
     */
    @JsonProperty("hadHeartAttackOrStroke")
    public void setHadHeartAttackOrStroke(Boolean hadHeartAttackOrStroke) {
        this.hadHeartAttackOrStroke = hadHeartAttackOrStroke;
    }

    /**
     * @return The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public Boolean isAddingExtraSalt() {
        return isAddingExtraSalt;
    }

    /**
     * @param isAddingExtraSalt The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public void setIsAddingExtraSalt(Boolean isAddingExtraSalt) {
        this.isAddingExtraSalt = isAddingExtraSalt;
    }

    /**
     * @return The isAcetylsalicylicDrugsConsumer
     */
    @JsonProperty("isAcetylsalicylicDrugsConsumer")
    public Boolean isAcetylsalicylicDrugsConsumer() {
        return isAcetylsalicylicDrugsConsumer;
    }

    /**
     * @param isAcetylsalicylicDrugsConsumer The isAcetylsalicylicDrugsConsumer
     */
    @JsonProperty("isAcetylsalicylicDrugsConsumer")
    public void setIsAcetylsalicylicDrugsConsumer(Boolean isAcetylsalicylicDrugsConsumer) {
        this.isAcetylsalicylicDrugsConsumer = isAcetylsalicylicDrugsConsumer;
    }

    /**
     * Only required fields != null
     */
    public boolean validate(RESULT_GROUPS group) {
        boolean result = true;

        if (group == RESULT_GROUPS.first || group == RESULT_GROUPS.all) {
            result = (sex != null) &&
            /*     */(birthday != null) &&
            /*     */(growth != null) &&
            /*     */(weight != null) &&
            /*     */(isSmoker() != null) &&
            /*     */(cholesterolLevel != null);
        }

        if (group == RESULT_GROUPS.second || group == RESULT_GROUPS.all) {
            result = (result) &&
            /*     */(isHasDiabetes() != null) &&
            /*     */(arterialPressure != null) &&
            /*     */(physicalActivityMinutes != null) &&
            /*     */(hadHeartAttackOrStroke != null);
        }

        if (group == RESULT_GROUPS.third || group == RESULT_GROUPS.all) {
            result = (result) &&
            /*     */(isAddingExtraSalt() != null) &&
            /*     */(isAcetylsalicylicDrugsConsumer != null);
        }

        return result;
    }
}