package ru.com.cardiomagnil.ui.ca_content;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_content.RiskAnalysis.RiskAnalysisPatientDataFargment;
import ru.com.cardiomagnil.ui.slidingmenu.TestResultsFargment;

public enum Ca_MenuItem {
    item_main(Ca_MainFargment.class, R.string.menu_item_main, true, true),
    item_risk_analysis(RiskAnalysisPatientDataFargment.class, R.string.menu_item_risk_analysis, true, true),
    item_diary(Ca_PersonalCabinetFargment.class, R.string.menu_item_diary, true, true),
    item_search_institutions(Ca_SearchInstitutionsFargment.class, R.string.menu_item_search_institutions, true, true),
    item_information(Ca_InformationFragment.class, R.string.menu_item_information, true, true),
    item_settings(Ca_SettingsFargment.class, R.string.menu_item_settings, true, true),
    item_recommendations(Ca_RecommendationsFargment.class, R.string.menu_item_recommendations, true, false),
    item_analysis_results(TestResultsFargment.class, R.string.menu_item_analysis_results, true, false),
    item_calendar(Ca_CalendarFragment.class, R.string.menu_item_calendar, true, false),
    item_useful_to_know(Ca_UsefulToKnowFargment.class, R.string.menu_item_useful_to_know, true, true),
    item_publications(Ca_PublicationsFargment.class, R.string.menu_item_publications, true, true),
    item_reports(Ca_ReportsFargment.class, R.string.menu_item_reports, true, true);

    private final Class mItemClass;
    private final int mItemName;
    private boolean mIsItemVisible;
    private boolean mIsItemEnabled;

    private enum ItemState {normal, selected, disabled, invisible}

    Ca_MenuItem(Class itemClass, int itemName, boolean isItemVisible, boolean isItemEnabled) {
        this.mItemClass = itemClass;
        this.mItemName = itemName;
        this.mIsItemVisible = isItemVisible;
        this.mIsItemEnabled = isItemEnabled;
    }

    public Class getItemClass() {
        return this.mItemClass;
    }

    public int getItemName() {
        return this.mItemName;
    }

    public void setItemIsVisible(boolean isItemVisible) {
        this.mIsItemVisible = isItemVisible;
    }

    public boolean isItemVisible() {
        return this.mIsItemVisible;
    }

    public void setItemIsEnabled(boolean isItemEnabled) {
        this.mIsItemEnabled = isItemEnabled;
    }

    public boolean isItemEnabled() {
        return this.mIsItemEnabled;
    }
}