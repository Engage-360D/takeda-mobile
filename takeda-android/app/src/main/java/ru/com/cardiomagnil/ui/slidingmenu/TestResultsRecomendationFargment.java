package ru.com.cardiomagnil.ui.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;

public class TestResultsRecomendationFargment extends Fragment {
    private View parentView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_slidingmenu_test_recomendation, null);

        initTestResultsRecomendationFargment(parentView);

        return parentView;
    }

    private void initTestResultsRecomendationFargment(View view) {
        // textViewUrlText
        // textViewTitle
        // textViewText

        // FIXME!!! implement pages

//        TestResultPage pageData = AppState.getInstatce().getTestResultPage();
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();

        TextView textViewUrlText = (TextView) view.findViewById(R.id.textViewUrlText);
        TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        TextView textViewText = (TextView) view.findViewById(R.id.textViewText);

//        if (pageData == null) {
//            // FIXME!!! switchContent
////            slidingMenuActivity.switchContentBack();
//        }
//
//        try {
//            textViewUrlText.setText(pageData.getUrlText());
//            textViewTitle.setText(pageData.getTitle());
//            textViewText.setText(pageData.getText());
//        } catch (Exception e) {
//            ExeptionsHandler.getInstatce().handleException(getActivity(), e);
//        }
    }

}
