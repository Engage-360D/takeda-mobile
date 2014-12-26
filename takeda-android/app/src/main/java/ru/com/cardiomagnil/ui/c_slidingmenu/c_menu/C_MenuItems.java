package ru.com.cardiomagnil.ui.c_slidingmenu.c_menu;

import ru.com.cardiomagnil.ui.c_slidingmenu.c_content.C_StartFragment;
import ru.com.cardiomagnil.ui.c_slidingmenu.c_content.C_TestFragment;

public enum C_MenuItems {
    item_start(C_StartFragment.class, true, true),
    item_test(C_TestFragment.class, true, true);

    private final Class clazz;
    private boolean isVisible;
    private boolean isEnabled;

    C_MenuItems(Class clazz, boolean isVisible, boolean isEnabled) {
        this.clazz = clazz;
        this.isVisible = isVisible;
        this.isEnabled = isEnabled;
    }

    public Class getValue() {
        return this.clazz;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}