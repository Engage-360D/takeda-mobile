package ru.com.cardiomagnyl.ui.slidingmenu.menu;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.slidingmenu.content.InformationFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.JournalFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.MainFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.PublicationsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.RecommendationsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.ReportsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.SearchInstitutionsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet.CabinetSettingsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.UsefulToKnowFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis.RiskAnalysisPatientDataFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis.RiskAnalysisResultsFragment;

public enum MenuItem {
    item_main(MainFragment.class, R.string.menu_item_main, true, true),
    item_risk_analysis(RiskAnalysisPatientDataFragment.class, R.string.menu_item_risk_analysis, true, true),
    item_journal(JournalFragment.class, R.string.menu_item_calendar, true, true),
    item_search_institutions(SearchInstitutionsFragment.class, R.string.menu_item_search_institutions, true, true),
    item_information(InformationFragment.class, R.string.menu_item_information, true, true),
    item_settings(CabinetSettingsFragment.class, R.string.menu_item_settings, true, true),
    item_recommendations(RecommendationsFragment.class, R.string.menu_item_recommendations, true, false),
    item_analysis_results(RiskAnalysisResultsFragment.class, R.string.menu_item_analysis_results, true, false),
    item_useful_to_know(UsefulToKnowFragment.class, R.string.menu_item_useful_to_know, true, true),
    item_publications(PublicationsFragment.class, R.string.menu_item_publications, true, true),
    item_reports(ReportsFragment.class, R.string.menu_item_reports, true, true);

    private final Class mItemClass;
    private final int mItemName;
    private boolean mIsItemVisible;
    private boolean mIsItemEnabled;

    private enum ItemState {normal, selected, disabled, invisible}

    MenuItem(Class itemClass, int itemName, boolean isItemVisible, boolean isItemEnabled) {
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