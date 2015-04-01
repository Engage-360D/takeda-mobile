package ru.com.cardiomagnyl.ui.slidingmenu.menu;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.slidingmenu.content.InformationFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.MainFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.ReportsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.UsefulToKnowFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.institution.InstitutionsSearchFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.journal.JournalFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet.CabinetSettingsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis.RiskAnalysisPatientDataFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis.RiskAnalysisResultsFragment;

public enum MenuItem {
    item_main(MainFragment.class, R.string.menu_item_main, true, true),
    item_risk_analysis(RiskAnalysisPatientDataFragment.class, R.string.menu_item_risk_analysis, true, true),
    item_journal(JournalFragment.class, R.string.menu_item_journal, true, true),
    item_search_institutions(InstitutionsSearchFragment.class, R.string.menu_item_search_institutions, true, true),
    item_information(InformationFragment.class, R.string.menu_item_information, true, true),
    item_settings(CabinetSettingsFragment.class, R.string.menu_item_settings, true, true),
    item_analysis_results(RiskAnalysisResultsFragment.class, R.string.menu_item_analysis_results, true, true),
    item_useful_to_know(UsefulToKnowFragment.class, R.string.menu_item_useful_to_know, true, true),
    item_reports(ReportsFragment.class, R.string.menu_item_reports, true, true);

    private final Class itemClass;
    private final int itemName;
    private boolean isItemVisible;
    private boolean isItemEnabled;

    private enum ItemState {normal, selected, disabled, invisible}

    MenuItem(Class itemClass, int itemName, boolean isItemVisible, boolean isItemEnabled) {
        this.itemClass = itemClass;
        this.itemName = itemName;
        this.isItemVisible = isItemVisible;
        this.isItemEnabled = isItemEnabled;
    }

    public Class getItemClass() {
        return this.itemClass;
    }

    public int getItemName() {
        return this.itemName;
    }

    public void setItemIsVisible(boolean isItemVisible) {
        this.isItemVisible = isItemVisible;
    }

    public boolean isItemVisible() {
        return this.isItemVisible;
    }

    public void setItemIsEnabled(boolean isItemEnabled) {
        this.isItemEnabled = isItemEnabled;
    }

    public boolean isItemEnabled() {
        return this.isItemEnabled;
    }

}