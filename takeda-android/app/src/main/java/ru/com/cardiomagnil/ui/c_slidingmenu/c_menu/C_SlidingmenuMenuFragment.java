package ru.com.cardiomagnil.ui.c_slidingmenu.c_menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.c_base.C_BaseArgumens;
import ru.com.cardiomagnil.ui.c_base.C_BaseItemFragment;

public class C_SlidingmenuMenuFragment extends C_BaseItemFragment {
    // 'ITEMS_ORDER' overriding; base group
    public static final C_MenuItems[] ITEMS_ORDER = new C_MenuItems[]{
            C_MenuItems.item_start,
            C_MenuItems.item_test
    };

    private ArrayList<C_MenuItems> mGroupItems = new ArrayList<C_MenuItems>();
    private LinkedHashMap<C_MenuItems, ArrayList<C_MenuItems>> mChildItems = new LinkedHashMap<C_MenuItems, ArrayList<C_MenuItems>>();
    private Class<?> mSelectedItem = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initItems();
        View view = inflater.inflate(R.layout.c_slidingmenu_menu_fragment, null);
        initExpandableListView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public Class<?> getSelectedItem() {
        return mSelectedItem;
    }

    private void initItems() {
        for (C_MenuItems menuItem : ITEMS_ORDER) {
            mGroupItems.add(menuItem);
            try {
                Class<?> clazz = menuItem.getValue();
                Field fieldItemsOrder = clazz.getField(C_BaseArgumens.ITEMS_ORDER);
                fieldItemsOrder.setAccessible(true);
                C_MenuItems[] C_MenuItems = (C_MenuItems[]) fieldItemsOrder.get(null);
                if (C_MenuItems != null) {
                    mChildItems.put(menuItem, new ArrayList<C_MenuItems>(Arrays.asList(C_MenuItems)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initExpandableListView(View view) {
        final ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListViewMenu);
        final C_ExpandableListAdapter expandableListAdapter = new C_ExpandableListAdapter(this, mGroupItems, mChildItems);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                C_MenuItems groupItem = mGroupItems.get(groupPosition);
                ArrayList<C_MenuItems> childItems = mChildItems.get(groupItem);
                boolean withSwitch = childItems == null || childItems.size() == 0;
                Class<?> groupClazz = groupItem.getValue();
                mSelectedItem = groupClazz;
                onClickListenerHelper(groupClazz, v, withSwitch);
                expandableListAdapter.notifyDataSetChanged();
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                C_MenuItems groupItem = mGroupItems.get(groupPosition);
                ArrayList<C_MenuItems> childItems = mChildItems.get(groupItem);
                Class<?> groupClazz = groupItem.getValue();
                mSelectedItem = groupClazz;
                if (childItems != null && childItems.size() > 0) {
                    Class<?> childClazz = childItems.get(childPosition).getValue();
                    onClickListenerHelper(childClazz, v, true);
                }
                expandableListAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void onClickListenerHelper(Class<?> clazz, View view, boolean withSwitch) {
        Fragment newContent = null;

        try {
            Constructor<?> constructor = clazz.getConstructor();
            newContent = (Fragment) constructor.newInstance(new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }

        Activity currentActivity = getActivity();
        if (currentActivity != null && currentActivity instanceof C_SlidingMenuActivity && newContent != null) {
            ((C_SlidingMenuActivity) currentActivity).replaceAllContent(newContent, withSwitch);
        }
    }
}