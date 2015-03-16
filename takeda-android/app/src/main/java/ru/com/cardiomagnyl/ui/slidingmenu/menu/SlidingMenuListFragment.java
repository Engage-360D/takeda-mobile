package ru.com.cardiomagnyl.ui.slidingmenu.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.ExecutableFragment;

public class SlidingMenuListFragment extends ListFragment {
    private MenuAdapter mMenuItemsAdapter;
    private int mPreviousSelectedItemPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.slidingmenu_list_fragment, null);
        initFragment(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMenuItemsAdapter = new MenuAdapter(getActivity());
        mMenuItemsAdapter.addAll(SlidingMenuActivity.MENU_ITEMS);
        setListAdapter(mMenuItemsAdapter);

        onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_item", mPreviousSelectedItemPosition);
    }

    private void initFragment(final View fragmentView) {
        initMenuItems();
        initViewTreeObserver(fragmentView);
    }

    private void initMenuItems() {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.refreshMenuItems();
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        if (savedInstanceState != null) {
            mPreviousSelectedItemPosition = savedInstanceState.getInt("selected_item", slidingMenuActivity.getFistItem());
        } else {
            mPreviousSelectedItemPosition = slidingMenuActivity.getFistItem();
        }
    }

    private void initViewTreeObserver(final View fragmentView) {
        fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // unregister listener (this is important)
                        fragmentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        setSelectedItem(mPreviousSelectedItemPosition);
                    }
                });
    }

    public void setSelectedItem(final int currentSelectedItemPosition) {
        View previousSelectedItemView = getViewByPosition(mPreviousSelectedItemPosition, getListView());
        View currentSelectedItemView = getViewByPosition(currentSelectedItemPosition, getListView());

        if (previousSelectedItemView != null) {
            TextView previousTextViewTitle = (TextView) previousSelectedItemView.findViewById(R.id.textViewTitle);
            previousTextViewTitle.setSelected(false);
        }

        if (currentSelectedItemView != null) {
            TextView currentTextViewTitle = (TextView) currentSelectedItemView.findViewById(R.id.textViewTitle);
            currentTextViewTitle.setSelected(true);
        }

        mPreviousSelectedItemPosition = currentSelectedItemPosition;
    }

    public void unselectCurrentItem() {
        View previousSelectedItemView = getViewByPosition(mPreviousSelectedItemPosition, getListView());
        if (previousSelectedItemView != null) {
            TextView previousTextViewTitle = (TextView) previousSelectedItemView.findViewById(R.id.textViewTitle);
            previousTextViewTitle.setSelected(false);
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        Fragment currentFragment = slidingMenuActivity.getCurrentFragment();
        if (currentFragment != null && currentFragment.getClass().getName().equals(SlidingMenuActivity.MENU_ITEMS[position].getItemClass().getName())) {
            slidingMenuActivity.getSlidingMenu().showContent();
            return;
        }

        Class itemClass = SlidingMenuActivity.MENU_ITEMS[position].getItemClass();
        String fragmentClassName = itemClass.getName();
        Fragment newContent = Fragment.instantiate(this.getActivity(), fragmentClassName, null);

        if (newContent instanceof ExecutableFragment) {
            if (((ExecutableFragment) newContent).isShowable()) {
                setSelectedItem(position);
                switchFragment(newContent);
            }
            ((ExecutableFragment) newContent).execute(slidingMenuActivity);
        } else {
            setSelectedItem(position);
            switchFragment(newContent);
        }

        super.onListItemClick(listView, view, position, id);
    }

    public void refreshMenuItems() {
        if (mMenuItemsAdapter != null)
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
