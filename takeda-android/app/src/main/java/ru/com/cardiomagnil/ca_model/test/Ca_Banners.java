package ru.com.cardiomagnil.ca_model.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnil.ca_model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "physicalActivityMinutes",
        "weight",
        "isAddingExtraSalt",
        "isSmoker",
        "cholesterolLevel",
        "arterialPressure",
        "hadSugarProblems",
        "isArterialPressureDrugsConsumer",
        "isCholesterolDrugsConsumer"
})
public class Ca_Banners extends BaseModel {

    @JsonProperty("physicalActivityMinutes")
    private Ca_Banner physicalActivityMinutes;
    @JsonProperty("weight")
    private Ca_Banner weight;
    @JsonProperty("isAddingExtraSalt")
    private Ca_Banner isAddingExtraSalt;
    @JsonProperty("isSmoker")
    private Ca_Banner isSmoker;
    @JsonProperty("cholesterolLevel")
    private Ca_Banner cholesterolLevel;
    @JsonProperty("arterialPressure")
    private Ca_Banner arterialPressure;
    @JsonProperty("hadSugarProblems")
    private Ca_Banner hadSugarProblems;
    @JsonProperty("isArterialPressureDrugsConsumer")
    private Ca_Banner isArterialPressureDrugsConsumer;
    @JsonProperty("isCholesterolDrugsConsumer")
    private Ca_Banner isCholesterolDrugsConsumer;

    /**
     * @return The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public Ca_Banner getPhysicalActivityMinutes() {
        return physicalActivityMinutes;
    }

    /**
     * @param physicalActivityMinutes The physicalActivityMinutes
     */
    @JsonProperty("physicalActivityMinutes")
    public void setPhysicalActivityMinutes(Ca_Banner physicalActivityMinutes) {
        this.physicalActivityMinutes = physicalActivityMinutes;
    }

    /**
     * @return The weight
     */
    @JsonProperty("weight")
    public Ca_Banner getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    @JsonProperty("weight")
    public void setWeight(Ca_Banner weight) {
        this.weight = weight;
    }

    /**
     * @return The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public Ca_Banner getIsAddingExtraSalt() {
        return isAddingExtraSalt;
    }

    /**
     * @param isAddingExtraSalt The isAddingExtraSalt
     */
    @JsonProperty("isAddingExtraSalt")
    public void setIsAddingExtraSalt(Ca_Banner isAddingExtraSalt) {
        this.isAddingExtraSalt = isAddingExtraSalt;
    }

    /**
     * @return The isSmoker
     */
    @JsonProperty("isSmoker")
    public Ca_Banner getIsSmoker() {
        return isSmoker;
    }

    /**
     * @param isSmoker The isSmoker
     */
    @JsonProperty("isSmoker")
    public void setIsSmoker(Ca_Banner isSmoker) {
        this.isSmoker = isSmoker;
    }

    /**
     * @return The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public Ca_Banner getCholesterolLevel() {
        return cholesterolLevel;
    }

    /**
     * @param cholesterolLevel The cholesterolLevel
     */
    @JsonProperty("cholesterolLevel")
    public void setCholesterolLevel(Ca_Banner cholesterolLevel) {
        this.cholesterolLevel = cholesterolLevel;
    }

    /**
     * @return The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public Ca_Banner getArterialPressure() {
        return arterialPressure;
    }

    /**
     * @param arterialPressure The arterialPressure
     */
    @JsonProperty("arterialPressure")
    public void setArterialPressure(Ca_Banner arterialPressure) {
        this.arterialPressure = arterialPressure;
    }

    /**
     * @return The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public Ca_Banner getHadSugarProblems() {
        return hadSugarProblems;
    }

    /**
     * @param hadSugarProblems The hadSugarProblems
     */
    @JsonProperty("hadSugarProblems")
    public void setHadSugarProblems(Ca_Banner hadSugarProblems) {
        this.hadSugarProblems = hadSugarProblems;
    }

    /**
     * @return The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public Ca_Banner getIsArterialPressureDrugsConsumer() {
        return isArterialPressureDrugsConsumer;
    }

    /**
     * @param isArterialPressureDrugsConsumer The isArterialPressureDrugsConsumer
     */
    @JsonProperty("isArterialPressureDrugsConsumer")
    public void setIsArterialPressureDrugsConsumer(Ca_Banner isArterialPressureDrugsConsumer) {
        this.isArterialPressureDrugsConsumer = isArterialPressureDrugsConsumer;
    }

    /**
     * @return The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public Ca_Banner getIsCholesterolDrugsConsumer() {
        return isCholesterolDrugsConsumer;
    }

    /**
     * @param isCholesterolDrugsConsumer The isCholesterolDrugsConsumer
     */
    @JsonProperty("isCholesterolDrugsConsumer")
    public void setIsCholesterolDrugsConsumer(Ca_Banner isCholesterolDrugsConsumer) {
        this.isCholesterolDrugsConsumer = isCholesterolDrugsConsumer;
    }

}