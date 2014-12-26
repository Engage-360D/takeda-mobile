package ru.com.cardiomagnil.ui.slidingmenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Constructor;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.application.Tools;

public class MenuListFragment extends ListFragment {
    private String[] mFragmentNames = null;
    private SampleAdapter mMenuItemsAdapter;
    private View mPreviousSelectedItem;

    private enum ItemState {normal, selected, disabled, invisible}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_slidingmenu_list_fragment, null);
        Tools.setFontSegoeWP((ViewGroup) view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] menuItems = getResources().getStringArray(R.array.menu_items);
        mFragmentNames = new String[menuItems.length];

        // ArrayAdapter<String> menuItemsAdapter = new
        // ArrayAdapter<String>(getActivity(),
        // android.R.layout.simple_list_item_1, android.R.id.text1, menuItems);
        mMenuItemsAdapter = new SampleAdapter(getActivity());
        mMenuItemsAdapter.addAll(menuItems);
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
            if (currentFragment != null && currentFragment.getClass().getName().equals(mFragmentNames[position])) {
                mainActivity.getSlidingMenu().showContent();
                return;
            }
        }

        Fragment newContent = null;
        setSelectedItem(view);

        try {
            String className = mFragmentNames[position];
            Class<?> clazz = Class.forName(className);
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

    public class SampleAdapter extends ArrayAdapter<String> {

        public SampleAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.ca_slidingmenu_list_item, null);
            }

            // View menu_list_item_dummy =
            // LayoutInflater.from(getContext()).inflate(R.layout.menu_list_item_dummy, null);

            String complexItemTitle = getItem(position);
            String[] complexItemTitleStrings = complexItemTitle.split("\\*");
            boolean isVisible = complexItemTitleStrings[0].endsWith("+");
            boolean isClickable = complexItemTitleStrings[1].endsWith("+");
            mFragmentNames[position] = complexItemTitleStrings[2];
            String itemTitle = complexItemTitleStrings[3];

            TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewTitle);
            titleTextView.setText(itemTitle);

            // ru.com.cardiomagnil.ui.slidingmenu.AnalysisResultsFargment
            boolean isResultFragment = "ru.com.cardiomagnil.ui.slidingmenu.TestResultsFargment".equals(complexItemTitleStrings[2]);
            boolean resultNotEmpty = AppState.getInstatce().getTestResult() != null && AppState.getInstatce().getTestResult().isInitialized();

            if (isResultFragment && resultNotEmpty) {
                isClickable = true;
                isVisible = true;
            }

            convertView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            if (isClickable) {
                // TODO: Why !isClickable?
                convertView.setClickable(false);
                convertView.setEnabled(true);
            } else {
                // TODO: Why !isClickable?
                convertView.setClickable(true);
                convertView.setEnabled(false);
                titleTextView.setTextColor(getResources().getColor(R.color.ca_text_menu_disabled));
            }

            if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
                Fragment currentFragment = mainActivity.getCurrentFragment();
                if (currentFragment != null && currentFragment.getClass().getName().equals(mFragmentNames[position]) && mPreviousSelectedItem == null) {
                    setSelectedItem(convertView);
                }
            }

            return convertView;
            // return isVisible ? convertView : menu_list_item_dummy;
        }
    }
}
