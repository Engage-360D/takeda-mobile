package ru.com.cardiomagnyl.model.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "physicalActivityMinutes",
        "bmi",
        "isAddingExtraSalt",
        "isSmoker",
        "cholesterolLevel",
        "arterialPressure",
        "hadSugarProblems",
        "isArterialPressureDrugsConsumer",
        "isCholesterolDrugsConsumer"
})
public class TestBanners extends BaseModel {

    @JsonProperty("physicalActivityMinutes")
    private TestBanner physicalActivityMinutes;
    @JsonProperty("bmi")
    private TestBanner bmi;
    @JsonProperty("isAddingExtraSalt")
    private TestBanner isAddingExtraSalt;
    @JsonProperty("isSmoker")
    private TestBanner isSmoker;
    @JsonProperty("cholesterolLevel")
    private TestBanner cholesterolLevel;
    @JsonProperty("arterialPressure")
    private TestBanner arterialPressure;
    @JsonProperty("hadSugarProblems")
    private TestBanner hadSugarProblems;
    @JsonProperty("isArterialPressureDrugsConsumer")
    private TestBanner isArterialPressureDrugsConsumer;
    @JsonProperty("isCholesterolDrugsConsumer")
    private TestBanner isCholesterolDrugsConsumer;

    /**
     * @return The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public TestBanner getPhysicalActivityMinutes() {
        return physicalActivityMinutes;
    }

    /**
     * @param physicalActivityMinutes The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public void setPhysicalActivityMinutes(TestBanner physicalActivityMinutes) {
        this.physicalActivityMinutes = physicalActivityMinutes;
    }

    /**
     * @return The bmi
     */
    @JsonProperty("bmi")
    public TestBanner getBmi() {
        return bmi;
    }

    /**
     * @param bmi The bmi
     */
    @JsonProperty("bmi")
    public void setBmi(TestBanner bmi) {
        this.bmi = bmi;
    }

    /**
     * @return The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public TestBanner getIsAddingExtraSalt() {
        return isAddingExtraSalt;
    }

    /**
     * @param isAddingExtraSalt The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public void setIsAddingExtraSalt(TestBanner isAddingExtraSalt) {
        this.isAddingExtraSalt = isAddingExtraSalt;
    }

    /**
     * @return The isSmoker
     */
    @JsonProperty("isSmoker")
    public TestBanner getIsSmoker() {
        return isSmoker;
    }

    /**
     * @param isSmoker The isSmoker
     */
    @JsonProperty("isSmoker")
    public void setIsSmoker(TestBanner isSmoker) {
        this.isSmoker = isSmoker;
    }

    /**
     * @return The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public TestBanner getCholesterolLevel() {
        return cholesterolLevel;
    }

    /**
     * @param cholesterolLevel The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public void setCholesterolLevel(TestBanner cholesterolLevel) {
        this.cholesterolLevel = cholesterolLevel;
    }

    /**
     * @return The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public TestBanner getArterialPressure() {
        return arterialPressure;
    }

    /**
     * @param arterialPressure The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public void setArterialPressure(TestBanner arterialPressure) {
        this.arterialPressure = arterialPressure;
    }

    /**
     * @return The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public TestBanner getHadSugarProblems() {
        return hadSugarProblems;
    }

    /**
     * @param hadSugarProblems The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public void setHadSugarProblems(TestBanner hadSugarProblems) {
        this.hadSugarProblems = hadSugarProblems;
    }

    /**
     * @return The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public TestBanner getIsArterialPressureDrugsConsumer() {
        return isArterialPressureDrugsConsumer;
    }

    /**
     * @param isArterialPressureDrugsConsumer The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public void setIsArterialPressureDrugsConsumer(TestBanner isArterialPressureDrugsConsumer) {
        this.isArterialPressureDrugsConsumer = isArterialPressureDrugsConsumer;
    }

    /**
     * @return The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public TestBanner getIsCholesterolDrugsConsumer() {
        return isCholesterolDrugsConsumer;
    }

    /**
     * @param isCholesterolDrugsConsumer The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public void setIsCholesterolDrugsConsumer(TestBanner isCholesterolDrugsConsumer) {
        this.isCholesterolDrugsConsumer = isCholesterolDrugsConsumer;
    }

}