package ru.com.cardiomagnyl.model.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scoreNote",
        "fullscreenAlert",
        "mainRecommendation",
        "placesLinkShouldBeVisible",
        "banners"
})

public class Recommendations extends BaseModel {

    @JsonProperty("scoreNote")
    private Note scoreNote;
    @JsonProperty("fullscreenAlert")
    private Note fullscreenAlert;
    @JsonProperty("mainRecommendation")
    private Note mainRecommendation;
    @JsonProperty("placesLinkShouldBeVisible")
    private boolean placesLinkShouldBeVisible;
    @JsonProperty("banners")
    private Banners banners;

    /**
     * @return The scoreNote
     */
    @JsonProperty("scoreNote")
    public Note getScoreNote() {
        return scoreNote;
    }

    /**
     * @param scoreNote The scoreNote
     */
    @JsonProperty("scoreNote")
    public void setScoreNote(Note scoreNote) {
        this.scoreNote = scoreNote;
    }

    /**
     * @return The fullscreenAlert
     */
    @JsonProperty("fullscreenAlert")
    public Note getFullscreenAlert() {
        return fullscreenAlert;
    }

    /**
     * @param fullscreenAlert The fullscreenAlert
     */
    @JsonProperty("fullscreenAlert")
    public void setFullscreenAlert(Note fullscreenAlert) {
        this.fullscreenAlert = fullscreenAlert;
    }

    /**
     * @return The mainRecommendation
     */
    @JsonProperty("mainRecommendation")
    public Note getMainRecommendation() {
        return mainRecommendation;
    }

    /**
     * @param mainRecommendation The mainRecommendation
     */
    @JsonProperty("mainRecommendation")
    public void setMainRecommendation(Note mainRecommendation) {
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
    public Banners getBanners() {
        return banners;
    }

    /**
     * @param banners The banners
     */
    @JsonProperty("banners")
    public void setBanners(Banners banners) {
        this.banners = banners;
    }

}