package ru.com.cardiomagnil.ca_model.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnil.ca_model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scoreNote",
        "fullscreenAlert",
        "mainRecommendation",
        "placesLinkShouldBeVisible",
        "banners"
})

public class Ca_Recomendations extends BaseModel {

    @JsonProperty("scoreNote")
    private Ca_Note scoreNote;
    @JsonProperty("fullscreenAlert")
    private Ca_Note fullscreenAlert;
    @JsonProperty("mainRecommendation")
    private Ca_Note mainRecommendation;
    @JsonProperty("placesLinkShouldBeVisible")
    private boolean placesLinkShouldBeVisible;
    @JsonProperty("banners")
    private Ca_Banners banners;

    /**
     * @return The scoreNote
     */
    @JsonProperty("scoreNote")
    public Ca_Note getScoreNote() {
        return scoreNote;
    }

    /**
     * @param scoreNote The scoreNote
     */
    @JsonProperty("scoreNote")
    public void setScoreNote(Ca_Note scoreNote) {
        this.scoreNote = scoreNote;
    }

    /**
     * @return The fullscreenAlert
     */
    @JsonProperty("fullscreenAlert")
    public Ca_Note getFullscreenAlert() {
        return fullscreenAlert;
    }

    /**
     * @param fullscreenAlert The fullscreenAlert
     */
    @JsonProperty("fullscreenAlert")
    public void setFullscreenAlert(Ca_Note fullscreenAlert) {
        this.fullscreenAlert = fullscreenAlert;
    }

    /**
     * @return The mainRecommendation
     */
    @JsonProperty("mainRecommendation")
    public Ca_Note getMainRecommendation() {
        return mainRecommendation;
    }

    /**
     * @param mainRecommendation The mainRecommendation
     */
    @JsonProperty("mainRecommendation")
    public void setMainRecommendation(Ca_Note mainRecommendation) {
        this.mainRecommendation = mainRecommendation;
    }

    /**
     * @return The placesLinkShouldBeVisible
     */
    @JsonProperty("placesLinkShouldBeVisible")
    public boolean getPlacesLinkShouldBeVisible() {
        return placesLinkShouldBeVisible;
    }

    /**
     * @param placesLinkShouldBeVisible The placesLinkShouldBeVisible
     */
    @JsonProperty("placesLinkShouldBeVisible")
    public void setPlacesLinkShouldBeVisible(boolean placesLinkShouldBeVisible) {
        this.placesLinkShouldBeVisible = placesLinkShouldBeVisible;
    }

    /**
     * @return The banners
     */
    @JsonProperty("banners")
    public Ca_Banners getBanners() {
        return banners;
    }

    /**
     * @param banners The banners
     */
    @JsonProperty("banners")
    public void setBanners(Ca_Banners banners) {
        this.banners = banners;
    }

}