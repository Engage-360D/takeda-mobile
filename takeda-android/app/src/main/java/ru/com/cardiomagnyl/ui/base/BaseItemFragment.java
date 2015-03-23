package ru.com.cardiomagnyl.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.ui.slidingmenu.content.journal.JournalFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet.CabinetDataFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.pills.PillDetailsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;

public abstract class BaseItemFragment extends Fragment {
    private View mViewGroupTopBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_heart, null);
    }

    public abstract void initTopBar(ViewGroup viewGroupTopBar);

    protected void initTopBarMenuBellCabinet(ViewGroup viewGroupTopBar, boolean isMenuEnabled, boolean isBellEnabled, boolean isCabinetEnabled) {
        mViewGroupTopBar = viewGroupTopBar;

        View contentTopLeftBack = viewGroupTopBar.findViewById(R.id.contentTopLeftBack);
        View contentTopLeftMenu = viewGroupTopBar.findViewById(R.id.contentTopLeftMenu);

        if (contentTopLeftBack != null) contentTopLeftBack.setEnabled(isMenuEnabled);
        if (contentTopLeftMenu != null) contentTopLeftMenu.setEnabled(isMenuEnabled);

        initTopBarBellCabinet(viewGroupTopBar, isBellEnabled, isCabinetEnabled);
    }

    protected void initTopBarBellCabinet(ViewGroup viewGroupTopBar, boolean isBellEnabled, boolean isCabinetEnabled) {
        mViewGroupTopBar = viewGroupTopBar;

        LinearLayout linearLayoutRightHolder = (LinearLayout) viewGroupTopBar.findViewById(R.id.linearLayoutRightHolder);
        linearLayoutRightHolder.removeAllViews();

        boolean noIncidents = AppState.getInsnatce().getIncidents().isEmpty();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int spaceMedium = (int) viewGroupTopBar.getResources().getDimension(R.dimen.space_medium);
        lp.setMargins(spaceMedium, 0, 0, 0);

        addBell(linearLayoutRightHolder, isBellEnabled, lp, noIncidents);

        ImageView imageViewCabinet = new ImageView(viewGroupTopBar.getContext(), null, R.style.ImageViewTop);
        imageViewCabinet.setImageResource(R.drawable.selector_button_cabinet);
        imageViewCabinet.setLayoutParams(lp);
        imageViewCabinet.setEnabled(isCabinetEnabled && noIncidents);
        linearLayoutRightHolder.addView(imageViewCabinet);

        if (isCabinetEnabled) {
            imageViewCabinet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                        Fragment fragment = new CabinetDataFragment();
                        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                        slidingMenuActivity.replaceAllContent(fragment, false);
                    }
                }
            });
        } else {
            imageViewCabinet.setClickable(false);
        }
    }

    protected void initTopBarBellAddPill(ViewGroup viewGroupTopBar, boolean isBellEnabled, boolean isAddPillEnabled) {
        mViewGroupTopBar = viewGroupTopBar;

        LinearLayout linearLayoutRightHolder = (LinearLayout) viewGroupTopBar.findViewById(R.id.linearLayoutRightHolder);
        linearLayoutRightHolder.removeAllViews();

        boolean noIncidents = AppState.getInsnatce().getIncidents().isEmpty();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int spaceMedium = (int) viewGroupTopBar.getResources().getDimension(R.dimen.space_medium);
        lp.setMargins(spaceMedium, 0, 0, 0);

        addBell(linearLayoutRightHolder, isBellEnabled, lp, noIncidents);

        ImageView imageViewAddPill = new ImageView(viewGroupTopBar.getContext(), null, R.style.ImageViewTop);
        imageViewAddPill.setImageResource(R.drawable.selector_button_add_pill);
        imageViewAddPill.setLayoutParams(lp);
        imageViewAddPill.setEnabled(isAddPillEnabled && noIncidents);
        linearLayoutRightHolder.addView(imageViewAddPill);

        imageViewAddPill.setEnabled(isAddPillEnabled);
        imageViewAddPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                    Fragment fragment = new PillDetailsFragment();
                    SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                    slidingMenuActivity.putContentOnTop(fragment, true);
                }
            }
        });
    }

    protected void updateMissedEvents() {
        if (mViewGroupTopBar == null) return;

        TextView textViewBell = (TextView) mViewGroupTopBar.findViewById(R.id.textViewBell);
        ImageView imageViewBell = (ImageView) mViewGroupTopBar.findViewById(R.id.imageViewBell);

        if (textViewBell == null || imageViewBell == null) return;

        boolean noIncidents = AppState.getInsnatce().getIncidents().isEmpty();
        int missedEvents = AppState.getInsnatce().getTimelineEvents();
        boolean isEnabled = missedEvents > 0 && noIncidents;
        boolean isJournal = this instanceof JournalFragment;

        textViewBell.setVisibility(isEnabled ? View.VISIBLE : View.INVISIBLE);
        textViewBell.setText(getMissedEventsString(missedEvents));

        textViewBell.setEnabled(isEnabled && noIncidents && !isJournal);
        imageViewBell.setEnabled(isEnabled && noIncidents && !isJournal);
    }

    private void addBell(LinearLayout linearLayoutRightHolder, boolean isBellEnabled, LinearLayout.LayoutParams lp, boolean noIncidents) {
        View layout_bell = View.inflate(linearLayoutRightHolder.getContext(), R.layout.layout_bell, null);
        layout_bell.setLayoutParams(lp);
        linearLayoutRightHolder.addView(layout_bell);

        TextView textViewBell = (TextView) layout_bell.findViewById(R.id.textViewBell);
        ImageView imageViewBell = (ImageView) layout_bell.findViewById(R.id.imageViewBell);

        int missedEvents = AppState.getInsnatce().getTimelineEvents();
        boolean isEnabled = missedEvents > 0 && noIncidents;
        final boolean isJournal = this instanceof JournalFragment;

        textViewBell.setVisibility(isEnabled ? View.VISIBLE : View.INVISIBLE);
        textViewBell.setText(getMissedEventsString(missedEvents));

        layout_bell.setEnabled(isBellEnabled && noIncidents && !isJournal);
        textViewBell.setEnabled(isEnabled && noIncidents && !isJournal);
        imageViewBell.setEnabled(isEnabled && noIncidents && !isJournal);

        layout_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isJournal) return;

                SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                BaseItemFragment fragment = new JournalFragment();
                slidingMenuActivity.replaceAllContent(fragment, true);
                slidingMenuActivity.selectCurrentItem(fragment);
            }
        });
    }

    private String getMissedEventsString(int missedEvents) {
        return (missedEvents > 99 ? "\u221e" : String.valueOf(missedEvents));
    }

    protected void initTopBarDone(ViewGroup viewGroupTopBar, View.OnClickListener onClickListener, boolean isDoneEnabled) {
        mViewGroupTopBar = viewGroupTopBar;

        LinearLayout linearLayoutRightHolder = (LinearLayout) viewGroupTopBar.findViewById(R.id.linearLayoutRightHolder);
        linearLayoutRightHolder.removeAllViews();

        boolean noIncidents = AppState.getInsnatce().getIncidents().isEmpty();

        boolean isEnabled = onClickListener != null;

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroupTopBar.getContext());
        View layoutTopDone = layoutInflater.inflate(R.layout.layout_top_done, null);
        layoutTopDone.setEnabled(isEnabled);
        linearLayoutRightHolder.addView(layoutTopDone);

        layoutTopDone.setEnabled(isDoneEnabled && noIncidents);
        layoutTopDone.setOnClickListener(onClickListener);
    }

}