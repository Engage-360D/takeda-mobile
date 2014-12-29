package ru.com.cardiomagnil.ui.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.application.Tools;
import ru.com.cardiomagnil.ui.ca_content.Ca_MenuItem;
import ru.com.cardiomagnil.ui.ca_menu.Ca_MenuAdapter;

public class MenuListFragment extends ListFragment {
    //    private String[] mFragmentNames = null;
    private Ca_MenuAdapter mMenuItemsAdapter;
    private View mPreviousSelectedItem;

    private enum ItemState {normal, selected, disabled, invisible}

    private final List<Ca_MenuItem> mMenuItems = Arrays.asList(
            Ca_MenuItem.item_main,
            Ca_MenuItem.item_risk_analysis,
            Ca_MenuItem.item_diary,
            Ca_MenuItem.item_search_institutions,
            Ca_MenuItem.item_settings,
            Ca_MenuItem.item_recommendations,
            Ca_MenuItem.item_analysis_results,
            Ca_MenuItem.item_calendar,
            Ca_MenuItem.item_useful_to_know,
            Ca_MenuItem.item_publications,
            Ca_MenuItem.item_reports
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_slidingmenu_list_fragment, null);
        Tools.setFontSegoeWP((ViewGroup) view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        String[] menuItems = getResources().getStringArray(R.array.menu_items);
//        mFragmentNames = new String[menuItems.length];

        // ArrayAdapter<String> menuItemsAdapter = new
        // ArrayAdapter<String>(getActivity(),
        // android.R.layout.simple_list_item_1, android.R.id.text1, menuItems);
        mMenuItemsAdapter = new Ca_MenuAdapter(getActivity());
        mMenuItemsAdapter.addAll(mMenuItems);
        setListAdapter(mMenuItemsAdapter);
    }

    private void setSelectedItem(View view) {
        TextView currentTextViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        currentTextViewTitle.setSelected(true);
        if (mPreviousSelectedItem != null) {
            mPreviousSelectedItem.setSelected(false);
        }
        mPreviousSelectedItem = currentTextViewTitle;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
            Fragment currentFragment = mainActivity.getCurrentFragment();
            if (currentFragment != null && currentFragment.getClass().getName().equals(mMenuItems.get(position).getItemClass().getName())) {
                mainActivity.getSlidingMenu().showContent();
                return;
            }
        }

        Fragment newContent = null;
        setSelectedItem(view);

        try {
            Class clazz = mMenuItems.get(position).getItemClass();
            Constructor<?> ctor = clazz.getConstructor();
            newContent = (Fragment) ctor.newInstance(new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(getActivity(), e);
        }

        // Fragment newContent = new BirdGridFragment(position);
        if (newContent != null) {
            switchFragment(newContent);
        }
        super.onListItemClick(listView, view, position, id);
    }

    public void refreshMenuItems() {
        mMenuItemsAdapter.notifyDataSetChanged();
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
            mainActivity.switchContent(fragment);
        }
    }
}
