package ru.com.cardiomagnyl.model.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ru.com.cardiomagnyl.model.base.BaseModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scoreNote",
        "fullScreenAlert",
        "mainRecommendation",
        "placesLinkShouldBeVisible",
        "banners"
})

public class Recommendations extends BaseModel {

    @JsonProperty("scoreNote")
    private TestNote scoreNote;
    @JsonProperty("fullScreenAlert")
    private TestNote fullScreenAlert;
    @JsonProperty("mainRecommendation")
    private TestNote mainRecommendation;
    @JsonProperty("placesLinkShouldBeVisible")
    private boolean placesLinkShouldBeVisible;
    @JsonProperty("banners")
    private TestBanners banners;

    /**
     * @return The scoreNote
     */
    @JsonProperty("scoreNote")
    public TestNote getScoreNote() {
        return scoreNote;
    }

    /**
     * @param scoreNote The scoreNote
     */
    @JsonProperty("scoreNote")
    public void setScoreNote(TestNote scoreNote) {
        this.scoreNote = scoreNote;
    }

    /**
     * @return The fullScreenAlert
     */
    @JsonProperty("fullScreenAlert")
    public TestNote getFullScreenAlert() {
        return fullScreenAlert;
    }

    /**
     * @param fullScreenAlert The fullScreenAlert
     */
    @JsonProperty("fullScreenAlert")
    public void setFullScreenAlert(TestNote fullScreenAlert) {
        this.fullScreenAlert = fullScreenAlert;
    }

    /**
     * @return The mainRecommendation
     */
    @JsonProperty("mainRecommendation")
    public TestNote getMainRecommendation() {
        return mainRecommendation;
    }

    /**
     * @param mainRecommendation The mainRecommendation
     */
    @JsonProperty("mainRecommendation")
    public void setMainRecommendation(TestNote mainRecommendation) {
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
    public TestBanners getBanners() {
        return banners;
    }

    /**
     * @param banners The banners
     */
    @JsonProperty("banners")
    public void setBanners(TestBanners banners) {
        this.banners = banners;
    }

}