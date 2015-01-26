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
    private String scoreNote;
    @JsonProperty("fullscreenAlert")
    private String fullscreenAlert;
    @JsonProperty("mainRecommendation")
    private String mainRecommendation;
    @JsonProperty("placesLinkShouldBeVisible")
    private String placesLinkShouldBeVisible;
    @JsonProperty("banners")
    private String banners;

    /**
     * @return The scoreNote
     */
    @JsonProperty("scoreNote")
    public String getScoreNote() {
        return scoreNote;
    }

    /**
     * @param scoreNote The scoreNote
     */
    @JsonProperty("scoreNote")
    public void setScoreNote(String scoreNote) {
        this.scoreNote = scoreNote;
    }

    /**
     * @return The fullscreenAlert
     */
    @JsonProperty("fullscreenAlert")
    public String getFullscreenAlert() {
        return fullscreenAlert;
    }

    /**
     * @param fullscreenAlert The fullscreenAlert
     */
    @JsonProperty("fullscreenAlert")
    public void setFullscreenAlert(String fullscreenAlert) {
        this.fullscreenAlert = fullscreenAlert;
    }

    /**
     * @return The mainRecommendation
     */
    @JsonProperty("mainRecommendation")
    public String getMainRecommendation() {
        return mainRecommendation;
    }

    /**
     * @param mainRecommendation The mainRecommendation
     */
    @JsonProperty("mainRecommendation")
    public void setMainRecommendation(String mainRecommendation) {
        this.mainRecommendation = mainRecommendation;
    }

    /**
     * @return The placesLinkShouldBeVisible
     */
    @JsonProperty("placesLinkShouldBeVisible")
    public String getPlacesLinkShouldBeVisible() {
        return placesLinkShouldBeVisible;
    }

    /**
     * @param placesLinkShouldBeVisible The placesLinkShouldBeVisible
     */
    @JsonProperty("placesLinkShouldBeVisible")
    public void setPlacesLinkShouldBeVisible(String placesLinkShouldBeVisible) {
        this.placesLinkShouldBeVisible = placesLinkShouldBeVisible;
    }

    /**
     * @return The banners
     */
    @JsonProperty("banners")
    public String getBanners() {
        return banners;
    }

    /**
     * @param banners The banners
     */
    @JsonProperty("banners")
    public void setBanners(String banners) {
        this.banners = banners;
    }

}