package ru.com.cardiomagnil.ui.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_menu.Ca_MenuAdapter;

public class SlidingMenuListFragment extends ListFragment {
    private Ca_MenuAdapter mMenuItemsAdapter;
    private int mPreviousSelectedItemPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ca_slidingmenu_list_fragment, null);
        initViewTreeObserver(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMenuItemsAdapter = new Ca_MenuAdapter(getActivity());
        mMenuItemsAdapter.addAll(SlidingMenuActivity.MENU_ITEMS);
        setListAdapter(mMenuItemsAdapter);

        onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_item", mPreviousSelectedItemPosition);
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            mPreviousSelectedItemPosition = savedInstanceState.getInt("selected_item", SlidingMenuActivity.START_ITEM_POSITION);
    }

    private void initViewTreeObserver(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        setSelectedItem(mPreviousSelectedItemPosition);
                        // unregister listener (this is important)
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
    }

    private void setSelectedItem(final int currentSelectedItemPosition) {
        View previousSelectedItemView = getViewByPosition(mPreviousSelectedItemPosition, getListView());
        View currentSelectedItemView = getViewByPosition(currentSelectedItemPosition, getListView());

        if (previousSelectedItemView != null) {
            TextView previousTextViewTitle = (TextView) previousSelectedItemView.findViewById(R.id.textViewTitle);
//            previousSelectedItemView.setSelected(false);
            previousTextViewTitle.setSelected(false);
        }

        if (currentSelectedItemView != null) {
            TextView currentTextViewTitle = (TextView) currentSelectedItemView.findViewById(R.id.textViewTitle);
//            currentSelectedItemView.setSelected(true);
            currentTextViewTitle.setSelected(true);
        }

        mPreviousSelectedItemPosition = currentSelectedItemPosition;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
            Fragment currentFragment = slidingMenuActivity.getCurrentFragment();
            if (currentFragment != null && currentFragment.getClass().getName().equals(SlidingMenuActivity.MENU_ITEMS[position].getItemClass().getName())) {
                slidingMenuActivity.getSlidingMenu().showContent();
                return;
            }
        }

        setSelectedItem(position);

        String fragmentClassName = SlidingMenuActivity.MENU_ITEMS[position].getItemClass().getName();
        Fragment newContent = Fragment.instantiate(this.getActivity(), fragmentClassName, null);
        switchFragment(newContent);

        super.onListItemClick(listView, view, position, id);
    }

    public void refreshMenuItems() {
        mMenuItemsAdapter.notifyDataSetChanged();
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
            slidingMenuActivity.replaceAllContent(fragment, true);
        }
    }
}
