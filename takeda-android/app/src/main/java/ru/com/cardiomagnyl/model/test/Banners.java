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
public class Banners extends BaseModel {

    @JsonProperty("physicalActivityMinutes")
    private Banner physicalActivityMinutes;
    @JsonProperty("bmi")
    private Banner bmi;
    @JsonProperty("isAddingExtraSalt")
    private Banner isAddingExtraSalt;
    @JsonProperty("isSmoker")
    private Banner isSmoker;
    @JsonProperty("cholesterolLevel")
    private Banner cholesterolLevel;
    @JsonProperty("arterialPressure")
    private Banner arterialPressure;
    @JsonProperty("hadSugarProblems")
    private Banner hadSugarProblems;
    @JsonProperty("isArterialPressureDrugsConsumer")
    private Banner isArterialPressureDrugsConsumer;
    @JsonProperty("isCholesterolDrugsConsumer")
    private Banner isCholesterolDrugsConsumer;

    /**
     * @return The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public Banner getPhysicalActivityMinutes() {
        return physicalActivityMinutes;
    }

    /**
     * @param physicalActivityMinutes The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public void setPhysicalActivityMinutes(Banner physicalActivityMinutes) {
        this.physicalActivityMinutes = physicalActivityMinutes;
    }

    /**
     * @return The bmi
     */
    @JsonProperty("bmi")
    public Banner getBmi() {
        return bmi;
    }

    /**
     * @param bmi The bmi
     */
    @JsonProperty("bmi")
    public void setBmi(Banner bmi) {
        this.bmi = bmi;
    }

    /**
     * @return The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public Banner getIsAddingExtraSalt() {
        return isAddingExtraSalt;
    }

    /**
     * @param isAddingExtraSalt The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public void setIsAddingExtraSalt(Banner isAddingExtraSalt) {
        this.isAddingExtraSalt = isAddingExtraSalt;
    }

    /**
     * @return The isSmoker
     */
    @JsonProperty("isSmoker")
    public Banner getIsSmoker() {
        return isSmoker;
    }

    /**
     * @param isSmoker The isSmoker
     */
    @JsonProperty("isSmoker")
    public void setIsSmoker(Banner isSmoker) {
        this.isSmoker = isSmoker;
    }

    /**
     * @return The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public Banner getCholesterolLevel() {
        return cholesterolLevel;
    }

    /**
     * @param cholesterolLevel The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public void setCholesterolLevel(Banner cholesterolLevel) {
        this.cholesterolLevel = cholesterolLevel;
    }

    /**
     * @return The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public Banner getArterialPressure() {
        return arterialPressure;
    }

    /**
     * @param arterialPressure The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public void setArterialPressure(Banner arterialPressure) {
        this.arterialPressure = arterialPressure;
    }

    /**
     * @return The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public Banner getHadSugarProblems() {
        return hadSugarProblems;
    }

    /**
     * @param hadSugarProblems The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public void setHadSugarProblems(Banner hadSugarProblems) {
        this.hadSugarProblems = hadSugarProblems;
    }

    /**
     * @return The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public Banner getIsArterialPressureDrugsConsumer() {
        return isArterialPressureDrugsConsumer;
    }

    /**
     * @param isArterialPressureDrugsConsumer The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public void setIsArterialPressureDrugsConsumer(Banner isArterialPressureDrugsConsumer) {
        this.isArterialPressureDrugsConsumer = isArterialPressureDrugsConsumer;
    }

    /**
     * @return The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public Banner getIsCholesterolDrugsConsumer() {
        return isCholesterolDrugsConsumer;
    }

    /**
     * @param isCholesterolDrugsConsumer The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public void setIsCholesterolDrugsConsumer(Banner isCholesterolDrugsConsumer) {
        this.isCholesterolDrugsConsumer = isCholesterolDrugsConsumer;
    }

}