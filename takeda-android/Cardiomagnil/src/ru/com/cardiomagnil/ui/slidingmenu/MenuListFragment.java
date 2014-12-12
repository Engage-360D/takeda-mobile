package ru.com.cardiomagnil.ui.slidingmenu;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import ru.com.cardiomagnil.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.application.Tools;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuListFragment extends ListFragment {
    private String[] mFragmentNames = null;
    private SampleAdapter mMenuItemsAdapte;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slidingmenu_list_fragment, null);
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
        mMenuItemsAdapte = new SampleAdapter(getActivity());
        mMenuItemsAdapte.addAll(menuItems);
        setListAdapter(mMenuItemsAdapte);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        Fragment newContent = null;

        try {
            String className = mFragmentNames[position];
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor();
            newContent = (Fragment) ctor.newInstance(new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(getActivity(), e);
        }

        // Fragment newContent = new BirdGridFragment(position);
        if (newContent != null) {
            switchFragment(newContent);
        }
    }

    public void refreshMenuItems() {
        mMenuItemsAdapte.notifyDataSetChanged();
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuActivity) {
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.slidingmenu_list_item, null);
            }

            // View menu_list_item_dummy =
            // LayoutInflater.from(getContext()).inflate(R.layout.menu_list_item_dummy, null);

            String complexItemTitle = getItem(position);
            String[] complexItemTitleStrings = complexItemTitle.split("\\*");
            boolean isClickable = complexItemTitleStrings[0].endsWith("+");
            boolean isVisible = complexItemTitleStrings[1].endsWith("+");
            mFragmentNames[position] = complexItemTitleStrings[2];
            String itemTitle = complexItemTitleStrings[3];

            TextView titleTextView = (TextView) convertView.findViewById(R.id.TextViewTitle);
            titleTextView.setText(itemTitle);

            // ru.com.cardiomagnil.ui.slidingmenu.AnalysisResultsFargment
            boolean isResultFragment = "ru.com.cardiomagnil.ui.slidingmenu.TestResultsFargment".equals(complexItemTitleStrings[2]);
            boolean resultNotEmpty = AppState.getInstatce().getTestResult() != null && AppState.getInstatce().getTestResult().isInitialized();

            if (isResultFragment && resultNotEmpty) {
                isClickable = true;
                isVisible = true;
            }

            // TODO: Why !isEnable?
            convertView.setClickable(!isClickable);
            convertView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            titleTextView.setTextColor(isClickable ? Color.WHITE : Color.BLACK);

            return convertView;
            // return isVisible ? convertView : menu_list_item_dummy;
        }
    }
}
