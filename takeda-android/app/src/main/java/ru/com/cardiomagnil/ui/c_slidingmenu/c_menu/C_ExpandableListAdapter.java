package ru.com.cardiomagnil.ui.c_slidingmenu.c_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.c_base.C_BaseArgumens;

public class C_ExpandableListAdapter extends BaseExpandableListAdapter {
    private C_SlidingmenuMenuFragment mSlidingmenuMenuFragment;
    private ArrayList<C_MenuItems> mGroupItems;
    private LinkedHashMap<C_MenuItems, ArrayList<C_MenuItems>> mChildItems;

    public C_ExpandableListAdapter(C_SlidingmenuMenuFragment slidingmenuMenuFragment, ArrayList<C_MenuItems> groupItems, LinkedHashMap<C_MenuItems, ArrayList<C_MenuItems>> childItems) {
        mSlidingmenuMenuFragment = slidingmenuMenuFragment;
        mGroupItems = groupItems;
        mChildItems = childItems;
    }

    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        C_MenuItems groupItem = mGroupItems.get(groupPosition);
        ArrayList<C_MenuItems> childItems = mChildItems.get(groupItem);
        return childItems != null ? childItems.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        C_MenuItems groupItem = mGroupItems.get(groupPosition);
        ArrayList<C_MenuItems> childItems = mChildItems.get(groupItem);
        return childItems != null ? childItems.get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mSlidingmenuMenuFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.c_slidingmenu_list_item_group, null);
        }

        C_MenuItems item = mGroupItems.get(groupPosition);
        Class<?> clazz = item.getValue();
        initGroupView(clazz, isExpanded, convertView);

        return convertView;
    }

    private void initGroupView(Class<?> clazz, boolean isExpanded, View convertView) {
        try {
            Field fieldItemName = clazz.getDeclaredField(C_BaseArgumens.ITEM_NAME);
            fieldItemName.setAccessible(true);
            int itemTitle = (Integer) fieldItemName.get(null);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewTitle);
            titleTextView.setText(mSlidingmenuMenuFragment.getString(itemTitle));

            Field fieldItemsOrder = clazz.getField(C_BaseArgumens.ITEMS_ORDER);
            fieldItemsOrder.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mSlidingmenuMenuFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.c_slidingmenu_list_item_child, null);
        }

        C_MenuItems groupItem = mGroupItems.get(groupPosition);
        ArrayList<C_MenuItems> childItems = mChildItems.get(groupItem);
        if (childItems != null) {
            Class<?> clazz = childItems.get(childPosition).getValue();
            initChildView(clazz, convertView);
        }

        return convertView;
    }

    private void initChildView(Class<?> clazz, View convertView) {
        try {
            Field fieldItemName = clazz.getDeclaredField(C_BaseArgumens.ITEM_NAME);
            fieldItemName.setAccessible(true);
            int itemTitle = (Integer) fieldItemName.get(null);

            TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewTitle);
            titleTextView.setText(mSlidingmenuMenuFragment.getString(itemTitle));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
